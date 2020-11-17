package com.safeboda.ui.views

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.safeboda.R
import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.data.remote.UserOrganizationRepository
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.ApiFailureType
import com.safeboda.core.span.LabelColor
import com.safeboda.core.utils.toast
import com.safeboda.databinding.ActivityDashboardBinding
import com.safeboda.ui.interfaces.OnUserSelectedListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class DashboardActivity :
    BindingActivity<ActivityDashboardBinding>(),
    OnUserSelectedListener,
    OnRefreshListener {

    private val userOrganizationRepository: UserOrganizationRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.statusBackground.background =
            LabelColor.backgroundDrawable(this, LabelColor.GRAY)

        lifecycleScope.launch {
            userOrganizationRepository.fetchUserOrOrganization("jumaallan") {
                toast("failed")
            }.collect { profile ->
                if (profile != null) {
                    handleProfileSuccess(profile)
                    binding.headerItem = profile
                } else {
                    handleProfileFailure(
                        ApiFailure(ApiFailureType.PARSE_ERROR, null, null)
                    )
                }
            }
        }

    }

    private fun handleProfileSuccess(profile: UserOrOrganization) {
        Timber.d("Updating user profile list items.")
    }

    private fun handleProfileFailure(
        failure: ApiFailure
    ) {
        Timber.d("Failed to fetch profile due to $failure")
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard

    override fun onUserOrOrgSelected(login: String) {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        TODO("Not yet implemented")
    }
}