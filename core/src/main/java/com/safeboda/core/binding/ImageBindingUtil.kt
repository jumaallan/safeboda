package com.safeboda.core.binding

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.safeboda.core.R
import kotlin.math.roundToInt

@BindingAdapter(value = ["roundedCornerImageUrl", "roundedCornerRadius"], requireAll = false)
fun loadRoundedCornerImage(view: ImageView, imageUrl: String?, radius: Float) {
    when {
        view.context != null && !TextUtils.isEmpty(imageUrl) -> {
            val cornerRadius = if (radius > 0) {
                radius.roundToInt()
            } else {
                view.resources
                    .getDimensionPixelSize(R.dimen.corner_radius_small)
            }
            Glide.with(view.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(RoundedCorners(cornerRadius))
                .into(view)
        }
    }
}