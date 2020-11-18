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