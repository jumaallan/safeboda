package com.safeboda.core.binding

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.databinding.BindingAdapter

@BindingAdapter(
    value = ["marginStart", "marginEnd", "marginTop", "marginBottom"],
    requireAll = false
)
fun setMargins(
    view: View,
    marginStart: Float,
    marginEnd: Float,
    marginTop: Float,
    marginBottom: Float
) {
    if (view.layoutParams is MarginLayoutParams) {
        val params = view.layoutParams as MarginLayoutParams
        if (marginStart > 0) {
            params.marginStart = Math.round(marginStart)
        }
        if (marginEnd > 0) {
            params.marginEnd = Math.round(marginEnd)
        }
        if (marginTop > 0) {
            params.topMargin = Math.round(marginTop)
        }
        if (marginBottom > 0) {
            params.bottomMargin = Math.round(marginBottom)
        }
    }
}

@BindingAdapter(value = ["overrideHeight"])
fun overrideHeight(view: View, height: Int) {
    view.layoutParams.height = height
}