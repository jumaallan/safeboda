package com.safeboda.core.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.safeboda.core.FollowQuery
import com.safeboda.core.data.models.User
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.PAGE_SIZE
import com.safeboda.core.network.Page
import com.safeboda.core.network.data
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FollowersFollowingRepository(
    private val apolloClient: ApolloClient,
    private var deferredResponse: Deferred<Response<*>>? = null
) {

    suspend fun fetchFollowers(
        userId: String,
        cursor: String?,
        onFailure: (ApiFailure) -> Unit
    ): Flow<Pair<List<User>, Page>> {
        cancelQuery()

        val query = apolloClient.query(FollowQuery(userId, PAGE_SIZE, Input.fromNullable(cursor)))
            .toDeferred()
        deferredResponse = query
        val data = query.data(onFailure) ?: return flowOf()

        return flowOf(
            Pair(
                data.node?.asUser?.followers?.nodes.orEmpty().filterNotNull().map {
                    User(it.fragments.userListItemFragment)
                },
                Page(
                    data.node?.asUser?.followers?.pageInfo?.hasNextPage ?: false,
                    data.node?.asUser?.followers?.pageInfo?.endCursor,
                    false
                )
            )
        )
    }

    suspend fun fetchFollowing(
        userId: String,
        cursor: String?,
        onFailure: (ApiFailure) -> Unit
    ): Flow<Pair<List<User>, Page>> {
        cancelQuery()

        val query = apolloClient.query(FollowQuery(userId, PAGE_SIZE, Input.fromNullable(cursor)))
            .toDeferred()
        deferredResponse = query
        val data = query.data(onFailure) ?: return flowOf()

        return flowOf(
            Pair(
                data.node?.asUser?.following?.nodes.orEmpty().filterNotNull().map {
                    User(it.fragments.userListItemFragment)
                },
                Page(
                    data.node?.asUser?.following?.pageInfo?.hasNextPage ?: false,
                    data.node?.asUser?.following?.pageInfo?.endCursor,
                    false
                )
            )
        )
    }

    private fun cancelQuery() {
        deferredResponse?.cancel()
    }
}