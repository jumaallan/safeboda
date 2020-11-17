package com.safeboda.core.utils

import android.app.Activity
import android.view.View
import android.widget.Toast

/**
 * Responsible for creating a toast
 *
 * @param message
 * @param duration
 */
fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.toast(string: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, duration).show()
}

/**
 * Responsible for creating a snackBar on an Activity
 *
 * @param message
 */
fun Activity.makeSnackbar(message: String) {
    findViewById<View>(android.R.id.content).makeSnackbar(message)
}