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
package com.safeboda.ui.views

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.safeboda.R
import com.safeboda.core.network.ApiModel
import com.safeboda.core.network.ApiRequestStatus.*
import com.safeboda.ui.base.SafebodaActivity
import com.safeboda.ui.scroller.AppBarScrollListener
import kotlinx.android.parcel.Parcelize
import com.safeboda.core.R as core

/**
 * Custom view to toggle between loading, empty, error and successful UI states smoothly.
 */
class GithubUserOrganizationProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewAnimator(context, attrs) {

    companion object {
        @VisibleForTesting
        const val POSITION_LOADING = 0

        @VisibleForTesting
        const val POSITION_CONTENT = 1

        @VisibleForTesting
        const val POSITION_EMPTY = 2
        private const val FADE_DURATION_MS = 200L
    }

    data class EmptyModel(
        val title: String
    )

    private val loadingView: View
    private val contentView: ViewGroup
    private val emptyView: View
    val recyclerView: RecyclerView?
    private val swipeRefreshLayout: SwipeRefreshLayout?
    private var refreshCallback: OnRefreshListener? = null
    private var appBarScrollListener: AppBarScrollListener? = null

    // Error and empty states share the same view.
    private val emptyTitleTextView: TextView

    init {
        // Allows overrides of the main contentView, emptyText and errorText in xml.
        val customViewResId: Int
        context.theme.obtainStyledAttributes(
            attrs,
            core.styleable.LoadingViewFlipper,
            0, 0
        ).apply {
            try {
                customViewResId = getResourceId(
                    core.styleable.LoadingViewFlipper_contentView,
                    R.layout.user_organization_recycler_view
                )
            } finally {
                recycle()
            }
        }

        // Inflation order matters for displaying children.
        val inflater = LayoutInflater.from(context)
        loadingView = inflater.inflate(core.layout.default_loading_view, this, true)
        contentView = inflater.inflate(customViewResId, this, true) as ViewGroup
        recyclerView = contentView.findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        swipeRefreshLayout = contentView.findViewById(R.id.swipe_container)
        swipeRefreshLayout?.isEnabled = false
        swipeRefreshLayout?.setProgressBackgroundColorSchemeResource(core.color.blue)
        swipeRefreshLayout?.setColorSchemeResources(android.R.color.white)
        swipeRefreshLayout?.setProgressViewOffset(
            true,
            swipeRefreshLayout.progressViewStartOffset,
            swipeRefreshLayout.progressViewEndOffset
        )
        emptyView = inflater.inflate(core.layout.default_empty_view, this, true)
        emptyTitleTextView = emptyView.findViewById(core.id.empty_state_title)

        inAnimation = AlphaAnimation(0.0f, 1.0f).apply { duration = FADE_DURATION_MS }
        outAnimation = AlphaAnimation(1.0f, 0.0f).apply { duration = FADE_DURATION_MS }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is ViewFlipperState) {
            // Temporarily disable the animation when restoring the view.
            inAnimation = null
            outAnimation = null
            displayedChild = state.displayedChild
            inAnimation = AlphaAnimation(0.0f, 1.0f).apply { duration = FADE_DURATION_MS }
            outAnimation = AlphaAnimation(1.0f, 0.0f).apply { duration = FADE_DURATION_MS }
            super.onRestoreInstanceState(state.baseState)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onSaveInstanceState(): Parcelable? =
        ViewFlipperState(displayedChild, super.onSaveInstanceState())

    override fun onDetachedFromWindow() {
        appBarScrollListener?.let {
            recyclerView?.removeOnScrollListener(it)
            appBarScrollListener = null
        }
        super.onDetachedFromWindow()
    }

    fun enableSwipeToRefresh(listener: OnRefreshListener) {
        refreshCallback = listener
        swipeRefreshLayout?.isEnabled = true
        swipeRefreshLayout?.setOnRefreshListener(listener)
    }

    fun showViewForApiStatus(
        model: ApiModel<Any>,
        activity: Activity?,
        emptyModel: EmptyModel = EmptyModel(context.getString(core.string.default_empty_text))
    ) {
        val isEmptyModel = model.data == null || (model.data as? List<*>)?.isNullOrEmpty() == true
        when (model.status) {
            LOADING ->
                if (isEmptyModel) {
                    showLoading()
                } else {
                    showContent(true)
                }
            FAILURE -> if (activity is SafebodaActivity) {
                activity.handleApiFailure(model.apiFailure)?.also { error ->
                    if (isEmptyModel) {
                        val refreshable = refreshCallback
                        if (model.apiFailure?.showTryAgain == true && refreshable != null) {
                            // Add a refresh button for no network failures.
                            showEmpty(
                                EmptyModel(
                                    error
                                )
                            )
                        } else {
                            showEmpty(EmptyModel(error))
                        }
                    } else {
                        showContent(false)
                        showEmpty(
                            EmptyModel(
                                context.resources.getString(R.string.default_empty_text)
                            )
                        )
                        activity.showSnackbar(error)
                    }
                }
            }
            SUCCESS ->
                if (isEmptyModel) {
                    showEmpty(emptyModel)
                } else {
                    showContent(false)
                }
        }
    }

    fun showLoading() {
        ensureLoadingView()
        if (displayedChild != POSITION_LOADING) {
            displayedChild = POSITION_LOADING
        }
    }

    private fun showContent(isLoading: Boolean) {
        ensureContentView(isLoading)
        if (displayedChild != POSITION_CONTENT) {
            displayedChild = POSITION_CONTENT
        }
    }

    fun showEmpty(
        model: EmptyModel = EmptyModel(
            context.getString(core.string.default_empty_text)
        )
    ) {
        ensureEmptyView(model)
        if (displayedChild != POSITION_EMPTY) {
            displayedChild = POSITION_EMPTY
        }
    }

    private fun ensureLoadingView() {
        appBarScrollListener?.elevate()
    }

    private fun ensureContentView(isLoading: Boolean) {
        if (!isLoading) hidePullToRefresh()
    }

    private fun ensureEmptyView(model: EmptyModel) {
        emptyTitleTextView.text = model.title
        appBarScrollListener?.elevate()
        hidePullToRefresh()
    }

    private fun hidePullToRefresh() {
        if (swipeRefreshLayout?.isEnabled == true) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @Parcelize
    data class ViewFlipperState(val displayedChild: Int, val baseState: Parcelable?) : Parcelable
}