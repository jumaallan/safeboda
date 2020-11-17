package com.safeboda.ui.model

data class HeaderItem(
    val avatarUrl: String?,
    val name: String?,
    val login: String?,
    val websiteUrl: String?,
    val bioHtml: CharSequence?,
    val companyHtml: CharSequence?,
    val emojiHtml: CharSequence?,
    val statusMessage: String?,
    val location: String?,
    val followersCount: Int,
    val followingCount: Int,
    val isFollowing: Boolean,
    val showFollowButton: Boolean,
    val userId: String
)