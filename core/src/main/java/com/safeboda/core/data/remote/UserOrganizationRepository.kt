package com.safeboda.core.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.safeboda.core.UserOrOrganizationQuery
import com.safeboda.core.data.models.UserOrOrganization
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserOrganizationRepository(
    private val apolloClient: ApolloClient
) {

    suspend fun fetchUserOrOrganization(
        login: String,
        onFailure: (ApiFailure) -> Unit
    ): Flow<UserOrOrganization?> {
        val data = apolloClient.query(UserOrOrganizationQuery(login)).toDeferred().data(onFailure)
            ?: return flowOf()
        return flowOf(data.repositoryOwner?.let {
            when {
                it.asUser != null ->
                    UserOrOrganization(it.asUser.fragments.userProfileFragment)
                it.asOrganization != null ->
                    UserOrOrganization(it.asOrganization.fragments.organizationFragment)
                else -> null
            }
        })
    }
}