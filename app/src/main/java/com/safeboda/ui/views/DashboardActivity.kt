package com.safeboda.ui.views

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.safeboda.R
import com.safeboda.core.span.LabelColor
import com.safeboda.databinding.ActivityDashboardBinding
import com.safeboda.ui.interfaces.OnUserSelectedListener

class DashboardActivity : BindingActivity<ActivityDashboardBinding>(), OnUserSelectedListener,
    OnRefreshListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.statusBackground.background =
            LabelColor.backgroundDrawable(this, LabelColor.GRAY)
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