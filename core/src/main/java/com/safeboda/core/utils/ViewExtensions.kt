package com.safeboda.core.utils

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.safeboda.core.R

/**
 * Responsible for creating a snackBar on a view
 *
 * @param message
 */
fun View.makeSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

/**
 * Responsible for showing a view, toggle visibility to View.VISIBLE
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Responsible for hiding a view, toggle visibility to View.GONE
 */
fun View.hide() {
    visibility = View.GONE
}

fun Activity.makeProgressDialog(message: String): AlertDialog {
    val view = layoutInflater.inflate(
        R.layout.progress_dialog,
        findViewById(android.R.id.content),
        false
    )

    view.findViewById<TextView>(R.id.messageTextView)?.text = message

    val builder = AlertDialog.Builder(this)
    builder.setView(view)
    builder.setCancelable(false)
    return builder.create()
}

fun Fragment.makeProgressDialog(message: String) = requireActivity().makeProgressDialog(message)