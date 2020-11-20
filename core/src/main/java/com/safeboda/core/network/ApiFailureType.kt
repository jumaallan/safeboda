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

enum class ApiFailureType {
    NO_NETWORK, // No network connection
    RESPONSE_ERROR, // Request completed successfully with an error
    HTTP_ERROR, // Request failed with an non-2xx http error
    SERVER_ERROR, // Request failed with a 5xx http error
    PARSE_ERROR, // Request failed to parse
    CANCELED, // Request failed because it was canceled
    UNAUTHORIZED, // Request failed due to unauthorized token
    UNKNOWN
}