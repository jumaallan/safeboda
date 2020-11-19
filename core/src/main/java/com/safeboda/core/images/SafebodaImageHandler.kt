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
package com.safeboda.core.images

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html.ImageGetter
import android.text.style.ImageSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy.SampleSizeRounding.QUALITY
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.safeboda.core.R
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Loads images into an [ImageSpan] asynchronously.
 */
class SafebodaImageHandler(view: TextView) : ImageGetter {

    private val safeTextView = WeakReference(view)

    override fun getDrawable(source: String?): Drawable? {
        val view = safeTextView.get()
        if (source != null && view != null) {
            val maxWidth = if (view.layoutParams?.width ?: 0 > 0) {
                view.layoutParams.width
            } else {
                view.resources.displayMetrics.widthPixels
            }

            Timber.d("Loading $source with max width: $maxWidth")
            val levelListDrawable = LevelListDrawable()
            val loadingDrawable =
                ColorDrawable(ContextCompat.getColor(view.context, R.color.imageLoading))
            val loadingSize =
                view.context.resources.getDimensionPixelSize(R.dimen.loading_image_size)
            loadingDrawable.setBounds(0, 0, loadingSize, loadingSize)
            levelListDrawable.setBounds(0, 0, loadingSize, loadingSize)
            levelListDrawable.addLevel(0, 0, loadingDrawable)

            GlideApp.with(view)
                .asDrawable()
                .downsample(MaxWidthStrategy)
                .load(source)
                .into(ImageLoadedCallback(view, levelListDrawable, maxWidth, source))

            return levelListDrawable
        }
        return null
    }

    object MaxWidthStrategy : DownsampleStrategy() {
        override fun getScaleFactor(
            sourceWidth: Int,
            sourceHeight: Int,
            requestedWidth: Int,
            requestedHeight: Int
        ): Float {
            if (sourceWidth > requestedWidth) {
                return requestedWidth.toFloat() / sourceWidth
            }
            return 1F
        }

        override fun getSampleSizeRounding(
            sourceWidth: Int,
            sourceHeight: Int,
            requestedWidth: Int,
            requestedHeight: Int
        ): SampleSizeRounding = QUALITY
    }

    private class ImageLoadedCallback(
        view: TextView,
        drawable: LevelListDrawable,
        maxWidth: Int,
        private val source: String
    ) : CustomTarget<Drawable>(maxWidth, Target.SIZE_ORIGINAL), Drawable.Callback {

        val safeTextView = WeakReference(view)
        val levelListDrawable = WeakReference(drawable)
        var gifDrawable: GifDrawable? = null

        override fun onLoadCleared(placeholder: Drawable?) {
            Timber.d("Clearing image references for $source")
            safeTextView.clear()
            levelListDrawable.get()?.callback = null
            levelListDrawable.clear()
            gifDrawable?.callback = null
            gifDrawable?.stop()
            gifDrawable = null
        }

        override fun onResourceReady(
            resource: Drawable,
            transition: Transition<in Drawable>?
        ) {
            levelListDrawable.get()?.let { drawable ->
                val width = resource.intrinsicWidth
                val height = resource.intrinsicHeight
                drawable.setBounds(0, 0, width, height)
                resource.setBounds(0, 0, width, height)
                drawable.addLevel(1, 1, resource)
                drawable.level = 1
                drawable.callback = this

                if (resource is GifDrawable) {
                    Timber.d("Starting gif for source $source")
                    gifDrawable?.stop()
                    gifDrawable = resource
                    gifDrawable?.start()
                }

                safeTextView.get()?.text = safeTextView.get()?.text
                safeTextView.get()?.invalidate()
            }
        }

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            safeTextView.get()?.removeCallbacks(what)
        }

        override fun invalidateDrawable(who: Drawable) {
            Timber.d("Invalidating a parent text view for drawable $who")
            safeTextView.get()?.invalidate()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            safeTextView.get()?.postDelayed(what, `when`)
        }
    }
}