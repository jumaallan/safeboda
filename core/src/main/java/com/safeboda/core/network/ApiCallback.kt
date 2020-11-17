package com.safeboda.core.network

import androidx.annotation.WorkerThread
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.*
import com.safeboda.core.network.ApiFailureType.*
import timber.log.Timber
import java.net.UnknownHostException

abstract class ApiCallback<T, R : ApiResponse<T>>(private val modelFactory: (data: T) -> R) :
    ApolloCall.Callback<T>() {

    override fun onResponse(apolloResponse: Response<T>) {
        if (!apolloResponse.errors.isNullOrEmpty()) {
            val error = apolloResponse.errors?.first()
            handleFailure(ApiFailure(RESPONSE_ERROR, error?.message, null))
        } else {
            val data = apolloResponse.data
            if (data == null) {
                handleFailure(ApiFailure(PARSE_ERROR, null, null))
            } else {
                val response = modelFactory(data)
                handleSuccess(response)
            }
        }
    }

    override fun onFailure(e: ApolloException) {
        when (e) {
            is ApolloNetworkException -> {
                Timber.e(e.cause, "Request failed with no network connection")
                if (e.cause is UnknownHostException) {
                    handleFailure(ApiFailure(NO_NETWORK, null, null))
                } else {
                    handleFailure(ApiFailure(HTTP_ERROR, null, null))
                }
            }
            is ApolloHttpException -> {
                Timber.e(e, "Request failed with response ${e.code()}")
                when (val code = e.code()) {
                    401 -> handleFailure(ApiFailure(UNAUTHORIZED, e.message(), code))
                    in 500..599 -> handleFailure(ApiFailure(SERVER_ERROR, e.message(), code))
                    else -> handleFailure(ApiFailure(HTTP_ERROR, e.message(), code))
                }
            }
            is ApolloParseException -> {
                Timber.e(e, "Response failed to parse")
                handleFailure(ApiFailure(PARSE_ERROR, null, null))
            }
            is ApolloCanceledException -> {
                Timber.e(e, "Request canceled")
                handleFailure(ApiFailure(CANCELED, null, null))
            }
            else -> {
                Timber.e(e, "Request failed")
                handleFailure(ApiFailure(UNKNOWN, null, null))
            }
        }
    }

    /**
     * Called when a request completed successfully (code 2xx) and parsed successfully.
     */
    @WorkerThread
    abstract fun handleSuccess(response: R)

    /**
     * Called when a request failed to complete or the response had errors.
     * Note: This is not called when a request is canceled.
     */
    @WorkerThread
    abstract fun handleFailure(failure: ApiFailure)
}
