package com.safeboda.ui.views

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.Lifecycle.State.STARTED
import com.google.android.material.snackbar.Snackbar
import com.safeboda.core.R
import com.bumptech.glide.Glide
import com.safeboda.core.network.ApiFailure
import com.safeboda.core.network.ApiFailureType
import com.safeboda.core.store.RecentSearchStore
import com.safeboda.core.utils.toast
/**
 * Base Activity class.
 */
abstract class SafebodaActivity : AppCompatActivity() {

    internal fun logout() {
        clearImageCaches()
        RecentSearchStore.delete(this, GlobalScope)
        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finishAffinity()
        overridePendingTransition(0, 0)
    }

    /**
     * Log the user out if they are unauthorized, otherwise parse the error string.
     *
     * @return A human readable string if the error is for an authorized user.
     */
    fun handleApiFailure(failure: ApiFailure?): String? {
        return when (failure?.failureType) {
            ApiFailureType.UNAUTHORIZED -> {
                // Log the user out. Show a toast since the activity is finishing.
                toast(R.string.error_unauthorized)
                logout()
                null
            }
            ApiFailureType.NO_NETWORK -> getString(R.string.error_no_network)
            ApiFailureType.SERVER_ERROR -> getString(R.string.error_default)
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
        container: ViewGroup? = null
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
