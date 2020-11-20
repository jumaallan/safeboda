package com.safeboda.data.local.mapper

import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.fragment.UserProfileFragment
import com.safeboda.data.local.entities.Followers
import com.safeboda.data.local.entities.Followings

fun UserOrOrganization.Follower.toResponse(userLogin: String): Followers = Followers(
    0,
    userLogin,
    this.login,
    this.name,
    this.avatarUrl,
    this.bioHtml
)

fun Followers.toResponse(): UserOrOrganization.Follower = UserOrOrganization.Follower(
    UserProfileFragment.Node(
        "", this.id.toString(), this.login, this.name, this.avatarUrl, this.bioHtml
    )
)

fun UserOrOrganization.Following.toResponse(userLogin: String): Followings = Followings(
    0,
    userLogin,
    this.login,
    this.name,
    this.avatarUrl,
    this.bioHtml
)

fun Followings.toResponse(): UserOrOrganization.Following = UserOrOrganization.Following(
    UserProfileFragment.Node1(
        "", this.id.toString(), this.login, this.name, this.avatarUrl, this.bioHtml
    )
)