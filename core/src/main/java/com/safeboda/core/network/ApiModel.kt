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

import com.safeboda.core.network.ApiRequestStatus.*

/**
 * A wrapper class to hold a model type with its given API status (i.e. LOADING, ERROR, SUCCESS).
 */
data class ApiModel<out T>(
    val status: ApiRequestStatus,
    val data: T?,
    val apiFailure: ApiFailure?
) {
    companion object {
        fun <T> success(data: T): ApiModel<T> = ApiModel(SUCCESS, data, null)
        fun <T> failure(apiFailure: ApiFailure, data: T?): ApiModel<T> =
            ApiModel(FAILURE, data, apiFailure)
        fun <T> loading(data: T?): ApiModel<T> = ApiModel(LOADING, data, null)
    }
}