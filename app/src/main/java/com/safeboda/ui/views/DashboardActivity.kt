package com.safeboda.ui.views

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.safeboda.R
import com.safeboda.core.network.ApiModel
import com.safeboda.databinding.ActivityDashboardBinding
import com.safeboda.ui.adapters.UserOrOrganizationAdapter
import com.safeboda.ui.interfaces.OnUserSelectedListener
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DashboardActivity :
    BindingActivity<ActivityDashboardBinding>(),
    OnUserSelectedListener,
    OnRefreshListener {

    private val userOrganizationViewModel: UserOrganizationViewModel by viewModel()
    private lateinit var adapter: UserOrOrganizationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        userOrganizationViewModel.profileModel.observe(this, Observer(::onUserModelChanged))

        adapter = UserOrOrganizationAdapter(this)
        binding.viewUserOrgProfile.recyclerView?.adapter = adapter
        binding.viewUserOrgProfile.enableSwipeToRefresh(this)

        onRefresh()
    }

    private fun onUserModelChanged(model: ApiModel<List<ListItemProfile>>) {
        adapter.setData(model.data)
        binding.viewUserOrgProfile.showViewForApiStatus(model, this)
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard

    override fun onUserOrOrgSelected(login: String) {
        Timber.d("the user or org selected is $login")
    }

    override fun onRefresh() {
        userOrganizationViewModel.fetchUserOrOrganization("android254")
    }
}