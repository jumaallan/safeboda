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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.safeboda.BaseTest
import com.safeboda.data.local.sample.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK], manifest=Config.NONE) // https://stackoverflow.com/questions/56821193/does-robolectric-require-java-9
internal class UserRepositoryTest : BaseTest() {

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        super.setup()
        userRepository = UserRepository(userDao, followersDao, followingDao)
    }

    @Test
    fun `test fetching user by username`() {
        runBlocking {
            userRepository.saveUser(testUser[0])
            val user = userRepository.getUserByGithubUsername(username)
            MatcherAssert.assertThat(user?.name, `is`(testUser[0].name))
        }
    }

    @Test
    fun `test fetching user followers by username`() {
        runBlocking {
            userRepository.saveUserFollowers(testFollower)
            val followers = userRepository.getFollowersByGithubUsername(username)
            Truth.assertThat(followers.size).isAtLeast(1)
        }
    }

    @Test
    fun `test fetching user following by username`() {
        runBlocking {
            userRepository.saveUserFollowing(testFollowing)
            val following = userRepository.getFollowingByGithubUsername(username)
            Truth.assertThat(following[0].name).matches(name)
        }
    }
}