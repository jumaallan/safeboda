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

val testUser = listOf(
    User(
        0,
        "https://github.com/jumaallan",
        "https://avatars2.githubusercontent.com/u/25085146?u=ce5247f3c267df4cdbe2af79efee9c848d1791e0&v=4",
        "<div>Android @ Backbase</div>",
        "<div>Backbase</div>",
        "allanjuma@gmail.com",
        610,
        13,
        false,
        true,
        false,
        false,
        "Nairobi",
        "jumaallan",
        "Juma Allan",
        16,
        45,
        108,
        false,
        false,
        "https://androidstudy.com",
        false,
        "<div><g-emoji class=\"g-emoji\" alias=\"beach_umbrella\" fallback-src=\"https://github.githubassets.com/images/icons/emoji/unicode/1f3d6.png\">🏖️</g-emoji></div>",
        false,
        "Just a man, shipping code"
    )
)

val testFollower = listOf(
    Followers(
        0,
        "jumaallan",
        "akabiru",
        "Kabiru Mwenja",
        "https://avatars3.githubusercontent.com/u/17295175?u=554245ab33cd1c1d2d32c87064098c18c154ca0e&v=4",
        "<div>Software Composer</div>"
    )
)

val testFollowing = listOf(
    Followings(
        0,
        "jumaallan",
        "akabiru",
        "Kabiru Mwenja",
        "https://avatars3.githubusercontent.com/u/17295175?u=554245ab33cd1c1d2d32c87064098c18c154ca0e&v=4",
        "<div>Software Composer</div>"
    )
)