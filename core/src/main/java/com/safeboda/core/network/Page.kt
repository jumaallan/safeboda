package com.safeboda.core.network

data class Page(
    val hasNextPage: Boolean,
    val endCursor: String?,
    val isHead: Boolean
) {
    companion object {
        val first = Page(false, null, true)
    }
}