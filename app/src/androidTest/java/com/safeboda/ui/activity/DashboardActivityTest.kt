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

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.screen.Screen.Companion.idle
import com.agoda.kakao.text.KTextView
import com.safeboda.R
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.data.repository.UserRepository
import com.safeboda.fake.fakeFollowing
import com.safeboda.fake.fakeProfile
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.mock.declare

class RecyclerviewScrollActions(private val original: ScrollToAction = ScrollToAction()) :
    ViewAction by original {

    override fun getConstraints(): Matcher<View> = CoreMatchers.anyOf(
        CoreMatchers.allOf(
            withEffectiveVisibility(Visibility.VISIBLE),
            isDescendantOfA(isAssignableFrom(RecyclerView::class.java))
        ),
        original.constraints
    )
}

@LargeTest
@RunWith(AndroidJUnit4::class)
class DashboardActivityTest : KoinTest {

    private val userRepository = mockk<UserRepository>(relaxUnitFun = true)
    private val userOrganizationRepository = mockk<UserOrganizationRepository>()

    @After
    fun tearDown() {
        clearMocks(userRepository, userOrganizationRepository)
    }

    @Test
    fun testBlankPlaceholder_isDisplayed_whenNoUsers_haveBeenSearched() {
        declare {
            UserOrganizationViewModel(
                userOrganizationRepository,
                userRepository
            )
        }
        ActivityScenario.launch(DashboardActivity::class.java)

        Screen.onScreen<SafebodaEmptyUserScreen> {
            noUserTitle.isDisplayed()
            noUserDescription.hasAnyText()
        }
    }

    @Test
    fun testData_isDisplayed_whenPositiveResults_areReturned() = runBlocking {

        coEvery {
            userOrganizationRepository.fetchUserOrOrganization(
                any(),
                any(),
                any()
            )
        } returns flowOf(fakeProfile)

        declare {
            UserOrganizationViewModel(
                userOrganizationRepository,
                userRepository
            )
        }

        ActivityScenario.launch(DashboardActivity::class.java)

        Screen.onScreen<SafebodaEmptyUserScreen> {
            searchView.click()
            searchText {
                typeText("jumaallan\n")
            }

            profileName {
                isDisplayed()
                hasText(fakeProfile.name)
            }

            profileLogin {
                isDisplayed()
                hasText(fakeProfile.login)
            }

            profileStatus {
                isDisplayed()
                hasText(fakeProfile.status!!.message)
            }

            profileBio {
                isDisplayed()
                hasText(fakeProfile.bioHtml)
            }

            profileCompany {
                isDisplayed()
                hasText(fakeProfile.companyHtml)
            }

            profileLocation {
                isDisplayed()
                hasText(fakeProfile.location)
            }

            profileLink {
                isDisplayed()
                hasText(fakeProfile.websiteUrl)
            }

            this.followingList {
                act {
                    RecyclerviewScrollActions()
                }

                idle(500)

                isDisplayed()

                firstChild<Item> {
                    isVisible()
                    name { hasText(fakeFollowing.name) }
                    login { hasText(fakeFollowing.login) }
                    bio { hasText(fakeFollowing.bioHtml) }
                }
            }
        }

        Unit
    }

    class SafebodaEmptyUserScreen : Screen<SafebodaEmptyUserScreen>() {
        val noUserTitle = KTextView { withId(R.id.empty_state_title) }
        val noUserDescription = KTextView { withId(R.id.empty_state_description) }
        val searchView = KView { withId(R.id.search_item) }
        val searchText = KEditText { withId(R.id.search_src_text) }
        val profileName = KTextView { withId(R.id.user_profile_name) }
        val profileLogin = KTextView { withId(R.id.user_profile_login) }
        val profileStatus = KTextView { withId(R.id.user_profile_status_message) }
        val profileBio = KTextView { withId(R.id.user_profile_bio) }
        val profileCompany = KTextView { withId(R.id.user_profile_company) }
        val profileLocation = KTextView { withId(R.id.user_profile_location) }
        val profileLink = KTextView { withId(R.id.user_profile_link) }
        val followingList: KRecyclerView = KRecyclerView(
            {
                withId(R.id.user_following_recycler_view)
            },
            itemTypeBuilder = {
                itemType(::Item)
            }
        )
    }

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val name = KTextView { withId(R.id.user_name) }
        val login = KTextView { withId(R.id.user_login) }
        val bio = KTextView { withId(R.id.user_bio) }
    }
}