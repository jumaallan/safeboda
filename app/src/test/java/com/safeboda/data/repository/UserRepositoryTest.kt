package com.safeboda.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.safeboda.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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

        }
    }

}