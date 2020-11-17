package com.safeboda.core.network

import com.safeboda.core.network.ApiFailureType.RESPONSE_ERROR

/**
 * Wrapper class to hold http request failure data.
 */
data class ApiFailure(val failureType: ApiFailureType, val message: String?, val code: Int?) {
    val showTryAgain
        get() = (failureType != RESPONSE_ERROR) && (code == null || code < 400 || code > 499)
}
