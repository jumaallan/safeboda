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