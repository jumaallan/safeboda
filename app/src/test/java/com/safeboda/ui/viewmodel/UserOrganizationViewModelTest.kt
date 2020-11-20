package com.safeboda.ui.viewmodel

import com.safeboda.BaseViewModelTest
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.data.local.sample.testFollower
import com.safeboda.data.local.sample.testFollowing
import com.safeboda.data.local.sample.username
import com.safeboda.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Before
import org.junit.Test

class UserOrganizationViewModelTest : BaseViewModelTest() {

    private val userRepository = mockk<UserRepository>()
    private val userOrganizationRepository = mockk<UserOrganizationRepository>()

    private lateinit var userOrganizationViewModel: UserOrganizationViewModel

    @Before
    fun setUp() {
        userOrganizationViewModel = UserOrganizationViewModel(
            userOrganizationRepository,
            userRepository
        )
    }

    @FlowPreview
    @Test
    fun `test get user details are fetched successfully`() {
        coEvery { userRepository.getFollowersByGithubUsername(any()) } returns testFollower
        coEvery { userRepository.getFollowingByGithubUsername(any()) } returns testFollowing
        coEvery {
            userOrganizationRepository.fetchUserOrOrganization(
                any(),
                any(),
                any()
            )
        } returns emptyFlow()

        userOrganizationViewModel.fetchUserOrOrganization(username)
//        coVerify { userRepository.getFollowersByGithubUsername(username) }
        coVerify { userRepository.getFollowingByGithubUsername(username) }
        coVerify { userOrganizationRepository.fetchUserOrOrganization(username, "") { it } }

//        val data = MutableLiveData<ApiModel<List<UserOrganizationViewModel.ListItemProfile>>>()
//
//        userOrganizationViewModel.profileModel.test().assertValue(data.value?.data?.get(0))
    }
}