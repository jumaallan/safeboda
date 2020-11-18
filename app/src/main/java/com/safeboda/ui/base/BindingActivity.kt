package com.safeboda.ui.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Abstract Activity to create a [ViewDataBinding] for the given type when setting the content view.
 */
abstract class BindingActivity<T : ViewDataBinding> : SafebodaActivity() {

    abstract val layoutResId: Int

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
    }

    override fun onDestroy() {
        if (::binding.isInitialized) {
            binding.unbind()
        }
        super.onDestroy()
    }
}