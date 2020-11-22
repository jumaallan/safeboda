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
        emojiHtml = "<div><g-emoji class=\"g-emoji\" alias=\"beach_umbrella\" fallback-src=\"https://github.githubassets.com/images/icons/emoji/unicode/1f3d6.png\">\uD83C\uDFD6️</g-emoji></div>",
        indicatesLimitedAvailability = false,
        message = "Just a man, shipping code"
    ),
    isOrganization = false
)