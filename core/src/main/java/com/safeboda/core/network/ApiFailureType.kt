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