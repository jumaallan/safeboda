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
package com.safeboda.core.network

import com.safeboda.core.network.ApiFailureType.RESPONSE_ERROR

/**
 * Wrapper class to hold http request failure data.
 */
data class ApiFailure(val failureType: ApiFailureType, val message: String?, val code: Int?) {
    val showTryAgain
        get() = (failureType != RESPONSE_ERROR) && (code == null || code < 400 || code > 499)
}