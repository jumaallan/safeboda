package com.safeboda.ui.activity

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.data.repository.UserRepository
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import io.mockk.mockk
import org.junit.Before
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

}