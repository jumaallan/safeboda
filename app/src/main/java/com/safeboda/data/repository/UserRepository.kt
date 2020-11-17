package com.safeboda.data.repository

import androidx.lifecycle.LiveData
import com.safeboda.data.local.dao.UserDao
import com.safeboda.data.local.entities.User
import com.safeboda.data.remote.api.GithubAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
    private val githubAPI: GithubAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getUserByUserID(userID: String): LiveData<User> = userDao.getUserByUserID(userID)

    fun fetchUsers(): Flow<List<User>> = userDao.fetchUsers()
}