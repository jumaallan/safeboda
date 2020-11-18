package com.safeboda.ui.viewmodel

import com.safeboda.core.data.models.User
import com.safeboda.core.data.remote.FollowersFollowingRepository
import com.safeboda.core.network.ApiModel
import com.safeboda.core.network.Page
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class FollowersUsersViewModel(
    private val followersFollowingRepository: FollowersFollowingRepository,
    defaultScheduler: CoroutineDispatcher = Dispatchers.IO
) : UsersViewModel(defaultScheduler) {

    override suspend fun fetchData(root: String, endCursor: String?): Flow<Pair<List<User>, Page>> =
        followersFollowingRepository.fetchFollowers(root, endCursor) {
            _userModel.postValue(ApiModel.failure(it, userModel.value?.data))
        }
}