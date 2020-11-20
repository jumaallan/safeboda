package com.safeboda.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.safeboda.BaseTest
import com.safeboda.data.local.sample.testUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])// https://stackoverflow.com/questions/56821193/does-robolectric-require-java-9
internal class UserDaoTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun `test inserting and retrieving users`() = runBlockingTest {
        userDao.insert(testUser)
        val user = userDao.getUserByGithubUsername("jumaallan")
        assertThat(user.name, `is`(testUser[0].name))
    }
}