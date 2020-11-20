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