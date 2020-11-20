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
package com.safeboda.data.local

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.safeboda.data.local.dao.FollowersDao
import com.safeboda.data.local.dao.FollowingDao
import com.safeboda.data.local.dao.UserDao
import com.safeboda.data.local.entities.Followers
import com.safeboda.data.local.entities.Following
import com.safeboda.data.local.entities.User

@androidx.room.Database(
    entities = [
        User::class,
        Followers::class,
        Following::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun followersDao(): FollowersDao
    abstract fun followingDao(): FollowingDao
}