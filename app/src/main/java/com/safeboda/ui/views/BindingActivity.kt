package com.safeboda.ui.views

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Abstract Activity to create a [ViewDataBinding] for the given type when setting the content view.
 */
abstract class BindingActivity<T : ViewDataBinding> : SafebodaActivity() {

    abstract val layoutResId: Int

    protected lateinit var dataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutResId)
    }

    override fun onDestroy() {
        if (::dataBinding.isInitialized) {
            dataBinding.unbind()
        }
        super.onDestroy()
    }

//    internal fun initAppBarLayout(title: String? = null, subtitle: String? = null) {
//        dataBinding.root.findViewById<AppBarLayout>(R.id.app_bar_layout)?.let { appBarLayout ->
//            appBarLayout.title?.text = title ?: ""
//            appBarLayout.subtitle?.text = subtitle ?: ""
//            appBarLayout.subtitle?.visibility = if (subtitle.isNullOrBlank()) GONE else VISIBLE
//            val backIcon =
//                ContextCompat.getDrawable(this, R.drawable.ic_arrow_left_24)?.mutate()?.also {
//                    it.setTint(ContextCompat.getColor(this, R.color.textPrimary))
//                }
//            appBarLayout.toolbar?.navigationIcon = backIcon
//            appBarLayout.toolbar?.setNavigationOnClickListener { onBackPressed() }
//        }
//    }
}