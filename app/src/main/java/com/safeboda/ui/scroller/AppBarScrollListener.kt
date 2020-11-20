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
package com.safeboda.ui.scroller

import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.appbar.AppBarLayout
import com.safeboda.core.R
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.min

class AppBarScrollListener(
    private val appBarLayout: AppBarLayout
) : OnScrollListener() {

    companion object {
        private const val ELEVATION_MULTIPLIER_UNKNOWN = -1F
    }

    /**
     * Value between 0 and 1 to control the [AppBarLayout]'s elevation as a fraction of
     * [maxElevationPx].
     */
    private var elevationMultiplier: Float
    private val maxElevationPx: Float =
        appBarLayout.resources.getDimensionPixelSize(R.dimen.app_bar_elevation).toFloat()
    private val maxScrollY = appBarLayout.resources.getDimensionPixelSize(R.dimen.app_bar_elevation)

    init {
        appBarLayout.elevation = 0F
        elevationMultiplier = ELEVATION_MULTIPLIER_UNKNOWN
    }

    /**
     * Temporarily elevate the [AppBarLayout] until next scroll.
     */
    fun elevate() {
        appBarLayout.elevation = maxElevationPx
        elevationMultiplier = ELEVATION_MULTIPLIER_UNKNOWN
        Timber.d("Temporarily elevated app bar.")
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val llm = recyclerView.layoutManager as LinearLayoutManager
        val firstVisiblePosition = llm.findFirstVisibleItemPosition()

        if (firstVisiblePosition == RecyclerView.NO_POSITION) return

        if (elevationMultiplier == ELEVATION_MULTIPLIER_UNKNOWN ||
            (dy > 0 && elevationMultiplier < 1) || // Scrolling down
            (dy < 0 && firstVisiblePosition == 0) // Scrolling up
        ) {
            elevationMultiplier = if (firstVisiblePosition == 0) {
                val firstViewTop = recyclerView.getChildAt(0).let {
                    it.top - ((it.layoutParams as? MarginLayoutParams)?.topMargin ?: 0)
                }
                min(abs(firstViewTop), maxScrollY).toFloat() / maxScrollY
            } else {
                1F // Not at the top. Show the elevation completely.
            }
            appBarLayout.elevation = maxElevationPx * elevationMultiplier
            Timber.d("Calculated top nav elevation for dy:$dy")
        }
    }
}