package com.safeboda.ui.viewmodel

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.ApiFailureType.PARSE_ERROR
import com.safeboda.core.network.ApiModel
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UserOrganizationViewModel(
    private val userOrganizationRepository: UserOrganizationRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val userOrganizationProfileModel: MutableLiveData<ApiModel<List<ListItemProfile>>> =
        MutableLiveData()
    val profileModel: LiveData<ApiModel<List<ListItemProfile>>>
        get() = userOrganizationProfileModel

    @UiThread
    fun fetchUserOrOrganization(login: String, avatarUrl: String?, userName: String?) {
        handleProfileLoading(login, avatarUrl, userName)
        viewModelScope.launch(coroutineDispatcher) {
            userOrganizationRepository.fetchUserOrOrganization(login, null) {
                handleProfileFailure(it, avatarUrl, userName, login)
            }.collect { profile ->
                if (profile != null) {
                    handleProfileSuccess(profile)
                } else {
                    handleProfileFailure(
                        ApiFailure(PARSE_ERROR, null, null),
                        login,
                        avatarUrl,
                        userName
                    )
                }
            }
        }
    }

    @WorkerThread
    private fun handleProfileSuccess(profile: UserOrOrganization) {
        Timber.d("Updating user profile list items.")
        userOrganizationProfileModel.postValue(ApiModel.success(successListItems(profile)))
    }

    @UiThread
    private fun handleProfileLoading(
        login: String?,
        avatarUrl: String?,
        userName: String?
    ) {
        userOrganizationProfileModel.value = ApiModel.loading(
            cachedListItems(true, login, avatarUrl, userName)
        )
    }

    @WorkerThread
    private fun handleProfileFailure(
        failure: ApiFailure,
        login: String?,
        avatarUrl: String?,
        userName: String?
    ) {
        Timber.d("Failed to fetch profile due to $failure")
        userOrganizationProfileModel.postValue(
            ApiModel.failure(failure, cachedListItems(false, login, avatarUrl, userName))
        )
    }

    /**
     * Returns the existing list items if available, otherwise returns empty list items. The loading
     * footer will be appended if isLoading is true.
     */
    private fun cachedListItems(
        isLoading: Boolean,
        login: String?,
        avatarUrl: String?,
        userName: String?
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

        if (avatarUrl.isNullOrBlank()) {
            return emptyList()
        }

        val data = mutableListOf<ListItemProfile>()

        // Profile header
        data.add(HeaderItem(avatarUrl, userName, login))

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

        data.add(Spacer("footer"))

        return data
    }

    sealed class ListItemProfile(val itemType: Int, val adapterId: Long) {

        companion object {
            const val ITEM_TYPE_HEADER = 1
            const val ITEM_TYPE_LOADING = 2
            const val ITEM_TYPE_SPACER = 3
            const val ITEM_TYPE_DIVIDER = 4

            private const val ID_HEADER = 1L
            private const val ID_LOADING = 2L
            private const val ID_DIVIDER = 3L
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

        class LoadingItem : ListItemProfile(ITEM_TYPE_LOADING, ID_LOADING)

        class ListItemDivider : ListItemProfile(ITEM_TYPE_DIVIDER, ID_DIVIDER)
    }

}