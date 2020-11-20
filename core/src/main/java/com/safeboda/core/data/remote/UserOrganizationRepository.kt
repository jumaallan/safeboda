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
package com.safeboda.core.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.safeboda.core.UserOrOrganizationQuery
import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.PAGE_SIZE
import com.safeboda.core.network.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserOrganizationRepository(
    private val apolloClient: ApolloClient
) {

    suspend fun fetchUserOrOrganization(
        login: String,
        cursor: String?,
        onFailure: (ApiFailure) -> Unit
    ): Flow<UserOrOrganization?> {
        val data = apolloClient.query(
            UserOrOrganizationQuery(
                login,
                PAGE_SIZE,
                Input.fromNullable(cursor)
            )
        ).toDeferred().data(onFailure)
            ?: return flowOf()
        return flowOf(
            data.repositoryOwner?.let {
                when {
                    it.asUser != null ->
                        UserOrOrganization(it.asUser.fragments.userProfileFragment)
                    it.asOrganization != null ->
                        UserOrOrganization(it.asOrganization.fragments.organizationFragment)
                    else -> null
                }
            }
        )
    }
}