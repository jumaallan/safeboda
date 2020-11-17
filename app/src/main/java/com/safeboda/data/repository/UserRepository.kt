package com.safeboda.data.repository

import androidx.lifecycle.LiveData
import com.safeboda.data.local.dao.UserDao
import com.safeboda.data.local.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) {

    fun getUserByUserID(userID: String): LiveData<User> = userDao.getUserByUserID(userID)

    fun fetchUsers(): Flow<List<User>> = userDao.fetchUsers()
}