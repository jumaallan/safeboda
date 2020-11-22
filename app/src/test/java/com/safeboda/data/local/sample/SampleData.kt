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
package com.safeboda.data.local.sample

import com.safeboda.data.local.entities.Followers
import com.safeboda.data.local.entities.Followings
import com.safeboda.data.local.entities.User

const val username = "jumaallan"
const val name = "Kabiru Mwenja"

val testUser = listOf(
    User(
        0,
        url = "https://github.com/jumaallan",
        avatarUrl = "https://avatars2.githubusercontent.com/u/25085146?u=ce5247f3c267df4cdbe2af79efee9c848d1791e0&v=4",
        bioHtml = "<div>Android @ Backbase</div>",
        companyHtml = "<div>Backbase</div>",
        email = "allanjuma@gmail.com",
        followersTotalCount = 610,
        followingTotalCount = 13,
        isDeveloperProgramMember = false,
        isVerified = true,
        isEmployee = false,
        isViewer = false,
        location = "Nairobi",
        login = "jumaallan",
        name = "Juma Allan",
        organizationsCount = 16,
        repositoriesCount = 45,
        starredRepositoriesCount = 108,
        viewerCanFollow = false,
        viewerIsFollowing = false,
        websiteUrl = "https://androidstudy.com",
        isOrganization = false,
        emojiHtml = "<div><g-emoji class=\"g-emoji\" alias=\"beach_umbrella\" fallback-src=\"https://github.githubassets.com/images/icons/emoji/unicode/1f3d6.png\">🏖️</g-emoji></div>",
        indicatesLimitedAvailability = false,
        message = "Just a man, shipping code"
    )
)

val testFollower = listOf(
    Followers(
        0,
        userLogin = "jumaallan",
        login = "akabiru",
        name = "Kabiru Mwenja",
        avatarUrl = "https://avatars3.githubusercontent.com/u/17295175?u=554245ab33cd1c1d2d32c87064098c18c154ca0e&v=4",
        bioHtml = "<div>Software Composer</div>"
    )
)

val testFollowing = listOf(
    Followings(
        0,
        userLogin = "jumaallan",
        login = "akabiru",
        name = "Kabiru Mwenja",
        avatarUrl = "https://avatars3.githubusercontent.com/u/17295175?u=554245ab33cd1c1d2d32c87064098c18c154ca0e&v=4",
        bioHtml = "<div>Software Composer</div>"
    )
)