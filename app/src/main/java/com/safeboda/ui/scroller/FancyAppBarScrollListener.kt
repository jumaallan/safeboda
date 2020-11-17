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

class FancyAppBarScrollListener(
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