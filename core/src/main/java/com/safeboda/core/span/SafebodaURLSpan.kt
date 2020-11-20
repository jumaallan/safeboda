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
package com.safeboda.core.span

import android.content.Context
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.safeboda.core.R
import com.safeboda.core.html.HtmlStyler

class SafebodaURLSpan(
    context: Context,
    url: String,
    private val linkClickListener: HtmlStyler.OnLinkClickListener?,
    private val isAtMention: Boolean
) : URLSpan(url) {

    private val linkColor = ContextCompat.getColor(context, R.color.link)
    private val atMentionColor = ContextCompat.getColor(context, R.color.textPrimary)

    override fun onClick(widget: View) {
        linkClickListener?.onLinkClicked(widget, url)
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.color = if (isAtMention) atMentionColor else linkColor
        textPaint.isFakeBoldText = isAtMention
        textPaint.isUnderlineText = !isAtMention
    }
}