package com.safeboda.core.data.remote

import com.safeboda.core.BaseTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class UserOrganizationRepositoryTest : BaseTest() {

    private lateinit var userOrganizationRepository: UserOrganizationRepository

    @Before
    fun setUp() {
        super.setup()
        userOrganizationRepository = UserOrganizationRepository(apollo)
    }

    @Test
    fun fetchUserOrOrganization() {

        coEvery {
            userOrganizationRepository.fetchUserOrOrganization(
                any(),
                any(),
                any()
            )
        } returns flowOf()


    }
}