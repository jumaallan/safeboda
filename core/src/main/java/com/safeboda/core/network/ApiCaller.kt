package com.safeboda.core.network

import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): NetworkResult<T> = withContext(dispatcher) {
    try {
        NetworkResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Timber.e(throwable)
        when (throwable) {
            is IOException -> NetworkResult.NetworkError
            is HttpException -> {
                val code = throwable.code()

                val errorResponse = convertErrorBody(throwable)
                NetworkResult.ServerError(code, errorResponse)
            }
            else -> {
                NetworkResult.ServerError(null, null)
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? = try {
    throwable.response()?.errorBody()?.charStream()?.let {
        val gson = GsonBuilder()
            .create()
        gson.fromJson(it, ErrorResponse::class.java)
    }
} catch (exception: Exception) {
    Timber.e(exception)
    null
}