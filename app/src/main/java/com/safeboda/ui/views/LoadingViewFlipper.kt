package com.safeboda.ui.views

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.appbar.AppBarLayout
import com.safeboda.core.R
import com.safeboda.core.network.ApiModel
import com.safeboda.core.network.ApiRequestStatus.*
import com.safeboda.ui.scroller.FancyAppBarScrollListener
import kotlinx.android.parcel.Parcelize

/**
 * Custom view to toggle between loading, empty, error and successful UI states smoothly.
 */
class LoadingViewFlipper @JvmOverloads constructor(
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
        val title: String,
        val description: String? = null,
        val imageDrawable: Drawable? = null,
        val buttonTextResId: Int? = null,
        val buttonAction: () -> Unit = { }
    )

    val loadingView: View
    val contentView: ViewGroup
    val emptyView: View
    val recyclerView: RecyclerView?
    private val swipeRefreshLayout: SwipeRefreshLayout?
    private var refreshCallback: OnRefreshListener? = null
    private var fancyAppBarScrollListener: FancyAppBarScrollListener? = null

    // Error and empty states share the same view.
    private val emptyTitleTextView: TextView
    private val emptyDescriptionTextView: TextView
    private val emptyImageView: ImageView
    private val emptyButton: Button

    init {
        // Allows overrides of the main contentView, emptyText and errorText in xml.
        val customViewResId: Int
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingViewFlipper,
            0, 0
        ).apply {
            try {
                customViewResId = getResourceId(
                    R.styleable.LoadingViewFlipper_contentView,
                    R.layout.default_recycler_view
                )
            } finally {
                recycle()
            }
        }

        // Inflation order matters for displaying children.
        val inflater = LayoutInflater.from(context)
        loadingView = inflater.inflate(R.layout.default_loading_view, this, true)
        contentView = inflater.inflate(customViewResId, this, true) as ViewGroup
        recyclerView = contentView.findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        swipeRefreshLayout = contentView.findViewById(R.id.swipe_container)
        swipeRefreshLayout?.isEnabled = false
        swipeRefreshLayout?.setProgressBackgroundColorSchemeResource(R.color.blue)
        swipeRefreshLayout?.setColorSchemeResources(android.R.color.white)
        swipeRefreshLayout?.setProgressViewOffset(
            true,
            swipeRefreshLayout.progressViewStartOffset,
            swipeRefreshLayout.progressViewEndOffset
        )
        emptyView = inflater.inflate(R.layout.default_empty_view, this, true)
        emptyTitleTextView = emptyView.findViewById(R.id.empty_state_title)
        emptyDescriptionTextView = emptyView.findViewById(R.id.empty_state_description)
        emptyImageView = emptyView.findViewById(R.id.empty_state_image)
        emptyButton = emptyView.findViewById(R.id.empty_button)

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

    override fun onSaveInstanceState(): Parcelable? {
        return ViewFlipperState(displayedChild, super.onSaveInstanceState())
    }

    override fun onDetachedFromWindow() {
        fancyAppBarScrollListener?.let {
            recyclerView?.removeOnScrollListener(it)
            fancyAppBarScrollListener = null
        }
        super.onDetachedFromWindow()
    }

    fun enableSwipeToRefresh(listener: OnRefreshListener) {
        refreshCallback = listener
        swipeRefreshLayout?.isEnabled = true
        swipeRefreshLayout?.setOnRefreshListener(listener)
    }

    fun addFancyAppBarScrollListener(appBarLayout: AppBarLayout?) {
        appBarLayout?.let {
            fancyAppBarScrollListener = FancyAppBarScrollListener(it)
            recyclerView?.addOnScrollListener(fancyAppBarScrollListener!!)
        }
    }

    fun showViewForApiStatus(
        model: ApiModel<Any>,
        activity: Activity?,
        emptyModel: EmptyModel = EmptyModel(context.getString(R.string.default_empty_text))
    ) {
        val isEmptyModel = model.data == null || (model.data as? List<*>)?.isNullOrEmpty() == true
        when (model.status) {
            LOADING -> if (isEmptyModel) {
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
                                    error,
                                    null,
                                    null,
                                    R.string.try_again,
                                    refreshable::onRefresh
                                )
                            )
                        } else {
                            showEmpty(EmptyModel(error))
                        }
                    } else {
                        showContent(false)
                        activity.showSnackbar(error)
                    }
                }
            }
            SUCCESS -> if (isEmptyModel) {
                showEmpty(emptyModel)
            } else {
                showContent(false)
            }
        }
    }

    fun refreshViewForItemCount(count: Int, model: EmptyModel) {
        if (count == 0) {
            showEmpty(model)
        } else {
            showContent(false)
        }
    }

    fun showLoading() {
        ensureLoadingView()
        if (displayedChild != POSITION_LOADING) {
            displayedChild = POSITION_LOADING
        }
    }

    fun showContent(isLoading: Boolean) {
        ensureContentView(isLoading)
        if (displayedChild != POSITION_CONTENT) {
            displayedChild = POSITION_CONTENT
        }
    }

    fun showEmpty(model: EmptyModel = EmptyModel(context.getString(R.string.default_empty_text))) {
        ensureEmptyView(model)
        if (displayedChild != POSITION_EMPTY) {
            displayedChild = POSITION_EMPTY
        }
    }

    private fun ensureLoadingView() {
        fancyAppBarScrollListener?.elevate()
    }

    private fun ensureContentView(isLoading: Boolean) {
        if (!isLoading) hidePullToRefresh()
    }

    private fun ensureEmptyView(model: EmptyModel) {
        emptyTitleTextView.text = model.title
        if (model.imageDrawable != null) {
            emptyImageView.setImageDrawable(model.imageDrawable)
            emptyImageView.visibility = View.VISIBLE
        } else {
            emptyImageView.visibility = View.GONE
        }

        if (model.description != null) {
            emptyDescriptionTextView.text = model.description
            emptyDescriptionTextView.visibility = View.VISIBLE
        } else {
            emptyDescriptionTextView.visibility = View.GONE
        }

        if (model.buttonTextResId != null) {
            emptyButton.setText(model.buttonTextResId)
            emptyButton.visibility = View.VISIBLE
            emptyButton.setOnClickListener { model.buttonAction() }
        } else {
            emptyButton.visibility = View.GONE
        }
        fancyAppBarScrollListener?.elevate()
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
