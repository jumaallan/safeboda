package com.safeboda.fake

import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.fragment.UserProfileFragment

val fakeFollower = UserOrOrganization.Follower(
    UserProfileFragment.Node(
        id = "1",
        login = "akabiru",
        name = "Austin Kabiru",
        avatarUrl = "https://avatars3.githubusercontent.com/u/17295175?u=554245ab33cd1c1d2d32c87064098c18c154ca0e&v=4",
        bioHTML = "<div>Software Composer</div>"
    )
)

val fakeFollowing = UserOrOrganization.Following(
    UserProfileFragment.Node1(
        id = "1",
        login = "el-fasoh",
        name = "Elphas",
        avatarUrl = "https://avatars2.githubusercontent.com/u/9999914?v=4",
        bioHTML = "iOS"
    )
)

val fakeProfile = UserOrOrganization(
    id = "1",
    url = "https://github.com/jumaallan",
    avatarUrl = "https://avatars2.githubusercontent.com/u/25085146?u=ce5247f3c267df4cdbe2af79efee9c848d1791e0&v=4",
    bioHtml = "Happy Dev",
    companyHtml = "<div>Backbase</div>",
    email = "allanjuma@gmail.com",
    follower = listOf(fakeFollower),
    following = listOf(fakeFollowing),
    followersTotalCount = 1,
    followingTotalCount = 1,
    isDeveloperProgramMember = false,
    isVerified = true,
    isEmployee = false,
    isViewer = false,
    location = "Nairobi",
    login = "jumaallan",
    name = "Juma Allan",
    organizationsCount = 0,
    repositoriesCount = 1,
    starredRepositoriesCount = 10,
    viewerCanFollow = true,
    viewerIsFollowing = true,
    websiteUrl = "https://safeboda.com",
    status = UserOrOrganization.Status(
        "<div><g-emoji class=\"g-emoji\" alias=\"beach_umbrella\" fallback-src=\"https://github.githubassets.com/images/icons/emoji/unicode/1f3d6.png\">\uD83C\uDFD6️</g-emoji></div>",
        false,
        "Just a man, shipping code"
    ),
    isOrganization = false
)