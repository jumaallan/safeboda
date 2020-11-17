package com.safeboda.ui.views

import android.os.Bundle
import com.safeboda.R
import com.safeboda.core.span.LabelColor
import com.safeboda.databinding.ActivityDashboardBinding

class DashboardActivity : BindingActivity<ActivityDashboardBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.statusBackground.background =
            LabelColor.backgroundDrawable(this, LabelColor.GRAY)
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard
}