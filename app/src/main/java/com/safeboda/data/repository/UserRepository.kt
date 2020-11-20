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
package com.safeboda.data.repository

import com.safeboda.data.local.dao.FollowersDao
import com.safeboda.data.local.dao.FollowingDao
import com.safeboda.data.local.dao.UserDao
import com.safeboda.data.local.entities.Followers
import com.safeboda.data.local.entities.Followings
import com.safeboda.data.local.entities.User

class UserRepository(
    private val userDao: UserDao,
    private val followersDao: FollowersDao,
    private val followingDao: FollowingDao,
) {

    suspend fun saveUser(user: User) = userDao.insert(user)

    suspend fun getUserByGithubUsername(login: String): User? =
        userDao.getUserByGithubUsername(login)

    suspend fun saveUserFollowers(followers: List<Followers>) = followersDao.insert(followers)

    suspend fun getFollowersByGithubUsername(userLogin: String): List<Followers> =
        followersDao.getFollowersByGithubUsername(userLogin)

    suspend fun saveUserFollowing(following: List<Followings>) = followingDao.insert(following)

    suspend fun getFollowingByGithubUsername(userLogin: String): List<Followings> =
        followingDao.getFollowingByGithubUsername(userLogin)
}