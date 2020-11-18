package com.safeboda.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.safeboda.R
import com.safeboda.core.network.ApiModel
import com.safeboda.databinding.ActivityDashboardBinding
import com.safeboda.ui.adapters.UserOrOrganizationAdapter
import com.safeboda.ui.adapters.UsersAdapter
import com.safeboda.ui.base.BindingActivity
import com.safeboda.ui.scroller.PaginatedScrollListener
import com.safeboda.ui.viewmodel.FollowingUsersViewModel
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile
import com.safeboda.ui.viewmodel.UsersViewModel.ListItemUser
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity :
    BindingActivity<ActivityDashboardBinding>(),
    OnRefreshListener {

    private val userOrganizationViewModel: UserOrganizationViewModel by viewModel()
    private val usersViewModel: FollowingUsersViewModel by viewModel()

    private lateinit var userOrOrganizationAdapter: UserOrOrganizationAdapter
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        userOrganizationViewModel.profileModel.observe(this, Observer(::onUserModelChanged))
        usersViewModel.userModel.observe(this, { onUserFollowersFollowingModelChanged(it) })

        userOrOrganizationAdapter = UserOrOrganizationAdapter(this)
        binding.viewUserOrgProfile.recyclerView?.adapter = userOrOrganizationAdapter
        binding.viewUserOrgProfile.enableSwipeToRefresh(this)

        usersAdapter = UsersAdapter { user ->
            userOrganizationViewModel.fetchUserOrOrganization(user.login, user.avatarUrl, user.name)
        }
        binding.recyclerViewFollowersFollowing.adapter = usersAdapter
        binding.recyclerViewFollowersFollowing.addOnScrollListener(
            PaginatedScrollListener(
                usersViewModel
            )
        )

        onRefresh()
    }

    private fun onUserModelChanged(model: ApiModel<List<ListItemProfile>>) {
        userOrOrganizationAdapter.setData(model.data)
        binding.viewUserOrgProfile.showViewForApiStatus(model, this)
    }

    private fun onUserFollowersFollowingModelChanged(model: ApiModel<List<ListItemUser>>) {
        usersAdapter.submitList(model.data)
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard

    override fun onRefresh() {
        userOrganizationViewModel.fetchUserOrOrganization("jumaallan", "", "")
    }
}