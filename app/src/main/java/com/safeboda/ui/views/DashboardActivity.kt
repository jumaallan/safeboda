package com.safeboda.ui.views

import android.os.Bundle
import com.safeboda.R
import com.safeboda.databinding.ActivityDashboardBinding

class DashboardActivity : BindingActivity<ActivityDashboardBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard
}