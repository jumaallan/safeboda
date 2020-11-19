package com.safeboda.core.data.models

import com.safeboda.core.fragment.OrganizationFragment
import com.safeboda.core.fragment.UserProfileFragment

/**
 * Shared response from [UserOrOrganizationQuery] and [ViewerUserProfileQuery].
 */
data class UserOrOrganization(
    val id: String,
    val url: String,
    val avatarUrl: String,
    val bioHtml: String,
    val companyHtml: String,
    val email: String,
    val follower: List<Follower>,
    val following: List<Following>,
    val followersTotalCount: Int,
    val followingTotalCount: Int,
    val isDeveloperProgramMember: Boolean,
    val isVerified: Boolean,
    val isEmployee: Boolean,
    val isViewer: Boolean,
    val location: String,
    val login: String,
    val name: String,
    val organizationsCount: Int,
    val repositoriesCount: Int,
    val starredRepositoriesCount: Int,
    val viewerCanFollow: Boolean,
    val viewerIsFollowing: Boolean,
    val websiteUrl: String,
    val status: Status?,
    val isOrganization: Boolean
) {

    companion object {
        const val INVALID_COUNT = -1
    }

    constructor(fragment: OrganizationFragment) : this(
        fragment.id,
        fragment.url as String,
        fragment.avatarUrl as String,
        fragment.descriptionHTML ?: "",
        "",
        fragment.organizationEmail ?: "",
        emptyList(),
        emptyList(),
        INVALID_COUNT,
        INVALID_COUNT,
        false,
        fragment.isVerified,
        false,
        false,
        fragment.location ?: "",
        fragment.login,
        fragment.name ?: "",
        INVALID_COUNT,
        fragment.organizationRepositories.totalCount,
        INVALID_COUNT,
        false,
        false,
        (fragment.websiteUrl ?: "") as String,
        null,
        true
    )

    constructor(fragment: UserProfileFragment) : this(
        fragment.id,
        fragment.url as String,
        fragment.avatarUrl as String,
        fragment.bioHTML as String,
        fragment.companyHTML as String,
        fragment.userEmail,
        fragment.followers.nodes.orEmpty().mapNotNull { Follower(it) },
        fragment.following.nodes.orEmpty().mapNotNull { Following(it) },
        fragment.followers.totalCount,
        fragment.following.totalCount,
        fragment.isDeveloperProgramMember,
        false,
        fragment.isEmployee,
        fragment.isViewer,
        fragment.location ?: "",
        fragment.login,
        fragment.name ?: "",
        fragment.organizations.totalCount,
        fragment.repositories.totalCount,
        fragment.starredRepositories.totalCount,
        fragment.viewerCanFollow,
        fragment.viewerIsFollowing,
        (fragment.websiteUrl ?: "") as String,
        fragment.status?.let {
            Status(
                (it.emojiHTML ?: "") as String,
                it.indicatesLimitedAvailability,
                it.message ?: ""
            )
        },
        false
    )

    data class Follower(private val fragment: UserProfileFragment.Node?) {
        val id: String = fragment?.id ?: ""
        val login: String = fragment?.login ?: ""
        val name: String = fragment?.name ?: ""
        val avatarUrl: String = (fragment?.avatarUrl ?: "") as String
        val bioHtml: String = (fragment?.bioHTML ?: "") as String
    }

    data class Following(private val fragment: UserProfileFragment.Node1?) {
        val id: String = fragment?.id ?: ""
        val login: String = fragment?.login ?: ""
        val name: String = fragment?.name ?: ""
        val avatarUrl: String = (fragment?.avatarUrl ?: "") as String
        val bioHtml: String = (fragment?.bioHTML ?: "") as String
    }

    data class Status(
        val emojiHtml: String,
        val indicatesLimitedAvailability: Boolean,
        val message: String
    )
}