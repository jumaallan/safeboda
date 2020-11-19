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
package com.safeboda.core.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber

object DeepLinkRouter {

    fun handleRoute(context: Context?, uri: Uri) {

        if (context == null) {
            return
        }

        Timber.d("Handing route for $uri with segments ${uri.pathSegments}")

        val schemeUri = if (uri.scheme.isNullOrBlank()) Uri.parse("https://$uri") else uri
        // Launch external links using a VIEW intent. This allows opening native apps for
        // mailto urls or links supported by other third-party apps.
        launchExternalUrl(context, schemeUri)
    }

    private fun launchExternalUrl(context: Context, uri: Uri): Boolean {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                }
            )
        } catch (ex: ActivityNotFoundException) {
            return false
        }
        return true
    }
}