/*
 * Copyright 2020 Safeboda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.safeboda.ui.viewmodel

import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeboda.R
import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.data.models.UserOrOrganization.Follower
import com.safeboda.core.data.models.UserOrOrganization.Following
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.ApiFailureType.PARSE_ERROR
import com.safeboda.core.network.ApiModel
import com.safeboda.data.local.entities.User
import com.safeboda.data.local.mapper.toResponse
import com.safeboda.data.repository.UserRepository
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.*
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.MenuButtonItem.ButtonType.ORGANIZATIONS
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.MenuButtonItem.ButtonType.REPOSITORIES
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UserOrganizationViewModel(
    private val userOrganizationRepository: UserOrganizationRepository,
    private val userRepository: UserRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val userOrganizationProfileModel: MutableLiveData<ApiModel<List<ListItemProfile>>> =
        MutableLiveData()
    val profileModel: LiveData<ApiModel<List<ListItemProfile>>>
        get() = userOrganizationProfileModel

    @UiThread
    fun fetchUserOrOrganization(login: String) {
        handleProfileLoading(login)
        viewModelScope.launch(coroutineDispatcher) {
            userOrganizationRepository.fetchUserOrOrganization(login, null) {
                handleProfileFailure(it, login)
            }.collect { profile ->
                when {
                    profile != null -> {
                        handleProfileSuccess(profile)
                    }
                    else -> {
                        handleProfileFailure(
                            ApiFailure(PARSE_ERROR, null, null),
                            login
                        )
                    }
                }
            }
        }
    }

    private suspend fun handleProfileSuccess(profile: UserOrOrganization) {
        Timber.d("Updating user profile list items.")
        userOrganizationProfileModel.postValue(ApiModel.success(successListItems(profile)))
        // save to local cache
        userRepository.saveUser(
            User(
                0,
                profile.url,
                profile.avatarUrl,
                profile.bioHtml,
                profile.companyHtml,
                profile.email,
                profile.followersTotalCount,
                profile.followingTotalCount,
                profile.isDeveloperProgramMember,
                profile.isVerified,
                profile.isEmployee,
                profile.isViewer,
                profile.location,
                profile.login,
                profile.name,
                profile.organizationsCount,
                profile.repositoriesCount,
                profile.starredRepositoriesCount,
                profile.viewerCanFollow,
                profile.viewerIsFollowing,
                profile.websiteUrl,
                profile.isOrganization
            )
        )

        userRepository.saveUserFollowers(
            profile.follower.map { it.toResponse(profile.login) }
        )

        userRepository.saveUserFollowing(
            profile.following.map { it.toResponse(profile.login) }
        )
    }

    @UiThread
    private fun handleProfileLoading(
        login: String?,
    ) {
        userOrganizationProfileModel.value = ApiModel.loading(
            cachedListItems(true, login)
        )
    }

    @WorkerThread
    private fun handleProfileFailure(
        failure: ApiFailure,
        login: String?
    ) {
        Timber.d("Failed to fetch profile due to $failure")

        // try to see if we have a cached record for the user, if not show error

        userOrganizationProfileModel.postValue(
            ApiModel.failure(failure, cachedListItems(false, login))
        )
    }

    /**
     * Returns the existing list items if available, otherwise returns empty list items. The loading
     * footer will be appended if isLoading is true.
     */
    private fun cachedListItems(
        isLoading: Boolean,
        login: String?
    ): List<ListItemProfile> {

        userOrganizationProfileModel.value?.data?.also {
            if (it.isNotEmpty()) {
                return if (!isLoading && it.last().itemType == ListItemProfile.ITEM_TYPE_LOADING) {
                    it.dropLast(1)
                } else {
                    it
                }
            }
        }

        val data = mutableListOf<ListItemProfile>()

        // Profile header
        data.add(HeaderItem(null, null, login))

        // Loading
        if (isLoading) {
            data.add(LoadingItem())
        }

        return data
    }

    /**
     * Returns a parsed list from the server response.
     */
    private fun successListItems(profile: UserOrOrganization): List<ListItemProfile> {
        val data = mutableListOf<ListItemProfile>()

        data.add(HeaderItem(profile))

        data.add(ListItemDivider())

        if (profile.repositoriesCount >= 0) {
            data.add(
                MenuButtonItem(
                    profile,
                    R.string.repositories,
                    profile.repositoriesCount,
                    REPOSITORIES
                )
            )
        }

        data.add(ListItemDivider())

        if (profile.organizationsCount >= 0) {
            data.add(
                MenuButtonItem(
                    profile,
                    R.string.organizations,
                    profile.organizationsCount,
                    ORGANIZATIONS
                )
            )
        }

        if (profile.following.isNotEmpty()) {
            data.add(FollowingItem(profile.following, profile.followingTotalCount))
        }

        if (profile.follower.isNotEmpty()) {
            data.add(FollowersItem(profile.follower, profile.followersTotalCount))
        }

        data.add(Spacer("footer"))

        return data
    }

    sealed class ListItemProfile(val itemType: Int, val adapterId: Long) {

        companion object {
            const val ITEM_TYPE_HEADER = 1
            const val ITEM_TYPE_FOLLOWERS = 2
            const val ITEM_TYPE_FOLLOWING = 3
            const val ITEM_TYPE_MENU_BUTTON = 4
            const val ITEM_TYPE_LOADING = 5
            const val ITEM_TYPE_SPACER = 6
            const val ITEM_TYPE_DIVIDER = 7

            private const val ID_HEADER = 1L
            private const val ID_FOLLOWERS = 2L
            private const val ID_FOLLOWING = 3L
            private const val ID_LOADING = 4L
            private const val ID_DIVIDER = 5L
        }

        class Spacer(id: String) : ListItemProfile(ITEM_TYPE_SPACER, id.hashCode().toLong())

        data class HeaderItem(
            val avatarUrl: String?,
            val name: String?,
            val login: String?,
            val websiteUrl: String?,
            val bioHtml: String?,
            val companyHtml: String?,
            val emojiHtml: String?,
            val statusMessage: String?,
            val location: String?,
            val followersCount: Int,
            val followingCount: Int,
            val isFollowing: Boolean,
            val showFollowButton: Boolean,
            val userId: String
        ) :
            ListItemProfile(ITEM_TYPE_HEADER, ID_HEADER) {

            constructor(profile: UserOrOrganization) :
                    this(
                        profile.avatarUrl,
                        profile.name,
                        profile.login,
                        profile.websiteUrl,
                        profile.bioHtml,
                        profile.companyHtml,
                        profile.status?.emojiHtml,
                        profile.status?.message,
                        profile.location,
                        profile.followersTotalCount,
                        profile.followingTotalCount,
                        profile.viewerIsFollowing,
                        !profile.isOrganization && !profile.isViewer,
                        profile.id
                    )

            constructor(avatarUrl: String?, userName: String?, login: String?) :
                    this(
                        avatarUrl,
                        userName,
                        login,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        -1,
                        -1,
                        false,
                        false,
                        ""
                    )
        }

        data class FollowersItem(
            val followers: List<Follower>?,
            val followersTotalCount: Int
        ) :
            ListItemProfile(ITEM_TYPE_FOLLOWERS, ID_FOLLOWERS)

        data class FollowingItem(
            val following: List<Following>,
            val followingTotalCount: Int
        ) :
            ListItemProfile(ITEM_TYPE_FOLLOWING, ID_FOLLOWING)

        data class MenuButtonItem(
            val profile: UserOrOrganization,
            @StringRes val text: Int,
            val value: Int,
            val type: ButtonType
        ) :
            ListItemProfile(ITEM_TYPE_MENU_BUTTON, type.hashCode().toLong()) {

            enum class ButtonType {
                REPOSITORIES, ORGANIZATIONS
            }
        }

        class LoadingItem : ListItemProfile(ITEM_TYPE_LOADING, ID_LOADING)

        class ListItemDivider : ListItemProfile(ITEM_TYPE_DIVIDER, ID_DIVIDER)
    }
}