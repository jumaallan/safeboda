package com.safeboda.core.dispatcher

import com.google.common.io.Resources
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.File
import java.net.HttpURLConnection

open class MockRequestDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            EXISTING_SEARCH_PARAMS -> {
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    .setBody(getJson("json/user_search.json"))
            }
            else -> throw IllegalArgumentException("Unknown Request Path ${request.path}")
        }
    }

    companion object Constants {
        const val EXISTING_SEARCH_PARAMS = "jumaallan"
    }
}

fun getJson(path: String): String {
    val uri = Resources.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}