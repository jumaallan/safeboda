package com.safeboda.data.local

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.safeboda.core.data.DateConverter
import com.safeboda.data.local.dao.UserDao
import com.safeboda.data.local.entities.User

@androidx.room.Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun userDao(): UserDao
}