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
package com.safeboda.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.safeboda.R
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.data.repository.UserRepository
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.inject
import org.koin.test.KoinTest
import org.koin.test.mock.declare

@LargeTest
@RunWith(AndroidJUnit4::class)
class DashboardActivityTest : KoinTest {

    private val userOrganizationViewModel: UserOrganizationViewModel by inject()

    private val userRepository = mockk<UserRepository>()
    private val userOrganizationRepository = mockk<UserOrganizationRepository>()

    @Before
    fun setup() {
        declare {
            UserOrganizationViewModel(
                userOrganizationRepository,
                userRepository
            )
        }
    }

    @Test
    fun testBlankPlaceholder_isDisplayed_whenNoUsers_haveBeenSearched() {

        ActivityScenario.launch(DashboardActivity::class.java)

        Screen.onScreen<SafebodaEmptyUserScreen> {
            noUserTitle.isDisplayed()
            noUserDescription.hasAnyText()
        }

        Screen.idle(3000)
    }

    class SafebodaEmptyUserScreen : Screen<SafebodaEmptyUserScreen>() {
        val noUserTitle = KTextView { withId(R.id.empty_state_title) }
        val noUserDescription = KTextView { withId(R.id.empty_state_description) }
    }
}