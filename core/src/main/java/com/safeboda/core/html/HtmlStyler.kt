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
package com.safeboda.core.html

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import android.widget.TextView.BufferType.SPANNABLE
import androidx.core.text.HtmlCompat
import com.safeboda.core.R
import com.safeboda.core.images.SafebodaImageHandler
import com.safeboda.core.span.SafebodaURLSpan

object HtmlStyler {

    interface OnLinkClickListener {
        fun onLinkClicked(view: View, url: String)
    }

    /**
     * Set the given TextView's text with custom HTML styling.
     *
     * Asynchronously loads any images into the TextView if the ImageSpan exists in its text
     * when the request completes.
     */
    fun styleText(
        view: TextView,
        html: String?,
        linkClickListener: OnLinkClickListener?
    ) {
        if (linkClickListener != null) {
            view.movementMethod = LinkMovementMethod.getInstance()
        } else {
            view.movementMethod = null
        }
        if (html.isNullOrBlank()) {
            view.text = ""
        } else {
            view.setText(styledText(view, html, linkClickListener), SPANNABLE)
        }
    }

    /**
     * Return a Spanned with custom HTML spans.
     *
     * Asynchronously loads any images into the TextView if the ImageSpan exists in its text
     * when the request completes.
     */
    private fun styledText(
        view: TextView,
        html: String,
        linkClickListener: OnLinkClickListener?
    ): Spanned {
        val imageHandler =
            view.getTag(R.id.image_handler) as? SafebodaImageHandler ?: SafebodaImageHandler(view)
        view.tag = R.id.tag_handler
        return postProcessSpans(
            view.context,
            HtmlCompat.fromHtml(
                html,
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                imageHandler,
                null
            ),
            linkClickListener
        )
    }

    private fun postProcessSpans(
        context: Context,
        html: Spanned,
        linkClickListener: OnLinkClickListener?
    ): Spanned {
        if (html is Spannable) {
            val length = html.length
            val spans = html.getSpans(0, length, Any::class.java)?.apply { reverse() }
            spans?.forEach {
                when (it) {
                    is URLSpan -> {
                        val isAtMention = html[html.getSpanStart(it)] == "@"[0]
                        replaceSpan(
                            html,
                            it,
                            SafebodaURLSpan(
                                context,
                                it.url,
                                linkClickListener,
                                isAtMention
                            )
                        )
                    }
                }
            }
        }
        return html.trim() as Spannable
    }

    private fun addSpan(builder: Spannable, originalSpan: Any, newSpan: Any) {
        builder.setSpan(
            newSpan,
            builder.getSpanStart(originalSpan),
            builder.getSpanEnd(originalSpan),
            builder.getSpanFlags(originalSpan)
        )
    }

    private fun replaceSpan(builder: Spannable, originalSpan: Any, newSpan: Any) {
        addSpan(builder, originalSpan, newSpan)
        builder.removeSpan(originalSpan)
    }
}