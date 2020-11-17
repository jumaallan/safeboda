package com.safeboda.ui.scroller

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.safeboda.core.network.ApiRequestStatus.*
import timber.log.Timber

class PaginatedScrollListener(private val viewModel: com.safeboda.ui.interfaces.PaginatedViewModel) :
    OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            when {
                firstVisibleItemPosition >= 0 && viewModel.requestStatus == SUCCESS && viewModel.currentPage.hasNextPage && visibleItemCount * 2 + firstVisibleItemPosition >= totalItemCount // we are within one screen-size of the end of the list
                -> {
                    Timber.d("Triggering page ${viewModel.currentPage.endCursor}")
                    viewModel.loadNextPage()
                }
            }
        }
    }
}