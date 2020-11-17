package com.safeboda.core.network

import com.safeboda.core.settings.Settings
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val settings: Settings,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var chainRequest = chain.request()
        chainRequest =
            chainRequest.newBuilder().header("Authorization", "Bearer $settings.bearerToken.orEmpty()")
                .build()
        return chain.proceed(chainRequest)
    }
}