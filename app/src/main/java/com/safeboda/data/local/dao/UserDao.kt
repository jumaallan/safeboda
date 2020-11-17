package com.safeboda.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.safeboda.data.local.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * FROM User WHERE userId =:userID")
    fun getUserByUserID(userID: String): LiveData<User>

    @Query("SELECT * FROM User")
    fun fetchUsers(): Flow<List<User>>
}