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
