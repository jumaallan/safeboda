package com.safeboda.core.binding

import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.databinding.BindingAdapter
import kotlin.math.roundToInt

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

@BindingAdapter(
    value = ["screenWidthRatio", "screenHeightRatio", "isRelativeToOrientation"],
    requireAll = false
)
fun setRatio(view: View, widthRatio: Float, heightRatio: Float, isRelativeToOrientation: Boolean) {
    val res = view.resources
    if (widthRatio > 0) {
        val screenWidth =
            if (isRelativeToOrientation) res.displayMetrics.widthPixels else getFixedScreenWidth(res)
        view.layoutParams.width = (screenWidth * widthRatio).roundToInt()
    }
    if (heightRatio > 0) {
        val screenHeight =
            if (isRelativeToOrientation) {
                res.displayMetrics.heightPixels
            } else {
                getFixedScreenHeight(res)
            }
        view.layoutParams.height = (screenHeight * heightRatio).roundToInt()
    }
}

fun getFixedScreenWidth(res: Resources): Int {
    val metrics = res.displayMetrics
    return if (res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) metrics.widthPixels else metrics.heightPixels
}

fun getFixedScreenHeight(res: Resources): Int {
    val metrics = res.displayMetrics
    return if (res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) metrics.heightPixels else metrics.widthPixels
}