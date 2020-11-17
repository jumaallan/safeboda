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
    val followersTotalCount: Int,
    val followersFacepile: FollowersFacepile?,
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
        INVALID_COUNT,
        null,
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
        fragment.followers.totalCount,
        FollowersFacepile(fragment.followers),
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

    data class FollowersFacepile(private val fragment: UserProfileFragment.Followers?) {
        val followers: List<Follower> = fragment?.nodes.orEmpty().map(::Follower)

        data class Follower(private val fragment: UserProfileFragment.Node?) {
            val id: String = fragment?.id ?: ""
            val login: String = fragment?.login ?: ""
            val avatarUrl: String = (fragment?.avatarUrl ?: "") as String
        }
    }

    data class Status(
        val emojiHtml: String,
        val indicatesLimitedAvailability: Boolean,
        val message: String
    )
}