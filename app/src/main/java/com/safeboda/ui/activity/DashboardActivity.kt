package com.safeboda.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.safeboda.R
import com.safeboda.core.network.ApiModel
import com.safeboda.databinding.ActivityDashboardBinding
import com.safeboda.ui.adapters.UserOrOrganizationAdapter
import com.safeboda.ui.base.BindingActivity
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity :
    BindingActivity<ActivityDashboardBinding>(),
    OnRefreshListener {

    private val userOrganizationViewModel: UserOrganizationViewModel by viewModel()

    private lateinit var userOrOrganizationAdapter: UserOrOrganizationAdapter
//    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

//        binding.appBarLayout.title.text = getString(R.string.menu_search)
//        binding.appBarLayout.toolbar.inflateMenu(R.menu.menu_search)

        userOrganizationViewModel.profileModel.observe(this, Observer(::onUserModelChanged))

        userOrOrganizationAdapter = UserOrOrganizationAdapter(this)
        binding.viewUserOrgProfile.recyclerView?.adapter = userOrOrganizationAdapter
        binding.viewUserOrgProfile.enableSwipeToRefresh(this)

        onRefresh()
    }

    private fun onUserModelChanged(model: ApiModel<List<ListItemProfile>>) {
        userOrOrganizationAdapter.setData(model.data)
        binding.viewUserOrgProfile.showViewForApiStatus(model, this)
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard

    override fun onRefresh() {
        userOrganizationViewModel.fetchUserOrOrganization("jumaallan", "", "")
    }
}