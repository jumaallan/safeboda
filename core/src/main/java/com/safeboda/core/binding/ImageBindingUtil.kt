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