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
package com.safeboda.ui.viewmodel

import com.jraska.livedata.test
import com.safeboda.BaseViewModelTest
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.data.local.sample.username
import com.safeboda.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
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
    fun `test get user or organisation details are fetched successfully`() {
        coEvery {
            userOrganizationRepository.fetchUserOrOrganization(
                username,
                any(),
                any()
            )
        } returns flowOf()
        userOrganizationViewModel.fetchUserOrOrganization(username)
        coVerify { userOrganizationRepository.fetchUserOrOrganization(username, any(), any()) }
        userOrganizationViewModel.profileModel.test().assertValue { it.data?.isNotEmpty() }
    }
}