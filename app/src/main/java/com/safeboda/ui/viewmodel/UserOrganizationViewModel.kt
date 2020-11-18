package com.safeboda.ui.viewmodel

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.core.network.ApiModel
import com.safeboda.data.local.entities.User
import com.safeboda.data.repository.UserRepository
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UserOrganizationViewModel(
    private val userRepository: UserRepository,
    private val userOrganizationRepository: UserOrganizationRepository
) : ViewModel() {

    private val _userOrOrganization: MutableLiveData<UserOrOrganization> = MutableLiveData()
    val userOrOrganization: LiveData<UserOrOrganization>
        get() = _userOrOrganization

    private val _profileModel: MutableLiveData<ApiModel<List<ListItemProfile>>> = MutableLiveData()
    val profileModel: LiveData<ApiModel<List<ListItemProfile>>>
        get() = _profileModel

    @UiThread
    fun fetchUserOrOrganization(login: String) {
//        handleProfileLoading(login)

        viewModelScope.launch(Dispatchers.IO) {
            userOrganizationRepository.fetchUserOrOrganization(login) {
//                handleProfileFailure(it, login)
            }.collect { profile ->
                if (profile != null) {
                    handleProfileSuccess(profile)
                } else {
//                    handleProfileFailure(
//                        ApiFailure(PARSE_ERROR, null, null),
//                        login
//                    )
                }
            }
        }
    }

//    @UiThread
//    private fun handleProfileLoading(
//        login: String?
//    ) {
//        _profileModel.value = ApiModel.loading(
//            cachedListItems(true, login)
//        )
//    }

    @WorkerThread
    private fun handleProfileSuccess(profile: UserOrOrganization) {
        Timber.d("Updating user profile list items.")
        _userOrOrganization.postValue(profile)
        _profileModel.postValue(ApiModel.success(successListItems(profile)))
    }

//    @WorkerThread
//    private fun handleProfileFailure(
//        failure: ApiFailure,
//        login: String?
//    ) {
//        Timber.d("Failed to fetch profile due to $failure")
//        _profileModel.postValue(
//            ApiModel.failure(failure, cachedListItems(false, login, avatarUrl, userName))
//        )
//    }

    fun getUserByUserID(userID: String): LiveData<User> = userRepository.getUserByUserID(userID)

    fun fetchUsers(): LiveData<List<User>> = userRepository.fetchUsers().asLiveData()

    /**
     * Returns a parsed list from the server response.
     */
    private fun successListItems(profile: UserOrOrganization): List<ListItemProfile> {
        val data = mutableListOf<ListItemProfile>()

        // Profile header
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

        }

        class LoadingItem : ListItemProfile(ITEM_TYPE_LOADING, ID_LOADING)

        class ListItemDivider : ListItemProfile(ITEM_TYPE_DIVIDER, ID_DIVIDER)
    }
}