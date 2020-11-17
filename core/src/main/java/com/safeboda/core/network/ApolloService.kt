package com.safeboda.core.network

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.*
import com.safeboda.core.network.ApiFailureType.*
import kotlinx.coroutines.Deferred
import java.net.UnknownHostException

internal const val PAGE_SIZE = 30

private fun onApolloException(e: ApolloException): ApiFailure {
    when (e) {
        is ApolloNetworkException -> {
            return if (e.cause is UnknownHostException) {
                ApiFailure(NO_NETWORK, null, null)
            } else {
                ApiFailure(HTTP_ERROR, null, null)
            }
        }
        is ApolloHttpException -> {
            return when (val code = e.code()) {
                401 -> ApiFailure(UNAUTHORIZED, e.message(), code)
                in 500..599 -> ApiFailure(SERVER_ERROR, e.message(), code)
                else -> ApiFailure(HTTP_ERROR, e.message(), code)
            }
        }
        is ApolloParseException -> return ApiFailure(PARSE_ERROR, null, null)
        is ApolloCanceledException -> return ApiFailure(CANCELED, null, null)
        else -> return ApiFailure(UNKNOWN, null, null)
    }
}

private val <T> Response<T>.error: ApiFailure?
    get() {
        if (data == null || errors.orEmpty().isNotEmpty()) {
            return ApiFailure(
                RESPONSE_ERROR,
                errors?.firstOrNull()?.message,
                null
            )
        }
        return null
    }

suspend fun <T> Deferred<Response<T>>.data(onFailure: (ApiFailure) -> Unit): T? {
    try {
        val response = this.await()
        response.error?.let {
            onFailure(it)
            return null
        }
        return response.data
    } catch (e: ApolloException) {
        onFailure(onApolloException(e))
        return null
    }
}
