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
package com.safeboda.ui.base

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.Lifecycle.State.STARTED
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.safeboda.R
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.ApiFailureType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Base Activity class.
 */
abstract class SafebodaActivity : AppCompatActivity() {

    /**
     *  parse the error string.
     *
     * @return A human readable string if the error is for an authorized user.
     */
    fun handleApiFailure(failure: ApiFailure?): String? {
        return when (failure?.failureType) {
            UNAUTHORIZED -> {
                Toast.makeText(applicationContext, R.string.error_unauthorized, Toast.LENGTH_SHORT)
                    .show()
                clearImageCaches()
                null
            }
            NO_NETWORK -> getString(R.string.error_no_network)
            SERVER_ERROR -> getString(R.string.error_default)
            else -> failure?.message ?: getString(R.string.error_default)
        }
    }

    /**
     * Snackbars work best if displayed in a [CoordinatorLayout].
     *
     * https://material.io/develop/android/components/snackbar/
     */
    internal fun showSnackbar(
        text: String?,
        length: Int = Snackbar.LENGTH_SHORT,
        action: SnackbarAction? = null,
        container: ViewGroup? = null
    ): Boolean {
        if (text != null && (lifecycle.currentState == STARTED || lifecycle.currentState == RESUMED)) {
            val viewGroup = container ?: findViewById<ViewGroup>(android.R.id.content)
            Snackbar.make(viewGroup, text, length).apply {
                view.elevation =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_elevation).toFloat()
                view.translationZ =
                    resources.getDimensionPixelSize(R.dimen.default_elevation).toFloat()

                val snackText =
                    view.findViewById<View>(com.google.android.material.R.id.snackbar_text)
                if (snackText is TextView) {
                    snackText.maxLines = 3
                    snackText.setTextColor(
                        ContextCompat.getColor(viewGroup.context, R.color.snackbarTextColor)
                    )
                }
                if (action != null) {
                    setAction(action.actionText, action.listener)
                    setActionTextColor(
                        ContextCompat.getColor(
                            viewGroup.context,
                            R.color.snackbarActionTextColor
                        )
                    )
                }
            }.show()
            return true
        }
        return false
    }

    internal fun showSnackbar(
        @StringRes textResId: Int,
        length: Int = Snackbar.LENGTH_SHORT,
        action: SnackbarAction? = null,
    ): Boolean = showSnackbar(getString(textResId), length, action)

    internal data class SnackbarAction(
        @StringRes val actionText: Int,
        val listener: OnClickListener
    )

    @UiThread
    private fun clearImageCaches() {
        applicationContext?.let {
            Glide.get(it).clearMemory()
            GlobalScope.launch(Dispatchers.Default) {
                blockingClearImageDiskCache(it)
            }
        }
    }

    @WorkerThread
    private fun blockingClearImageDiskCache(applicationContext: Context) {
        Glide.get(applicationContext).clearDiskCache()
    }
}