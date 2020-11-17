package com.safeboda.ui.interfaces

import com.safeboda.core.network.ApiRequestStatus
import com.safeboda.core.network.Page

interface PaginatedViewModel {
    fun loadHead()
    fun loadNextPage()
    val requestStatus: ApiRequestStatus
    var currentPage: Page
}