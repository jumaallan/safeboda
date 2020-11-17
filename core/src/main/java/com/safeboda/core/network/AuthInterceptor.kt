package com.safeboda.core.network

import com.safeboda.core.settings.Settings
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val settings: Settings,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = settings.bearerToken.orEmpty()
        var chainRequest = chain.request()
        chainRequest =
            chainRequest.newBuilder().header("Authorization", "Bearer $token")
                .build()
        return chain.proceed(chainRequest)
    }
}