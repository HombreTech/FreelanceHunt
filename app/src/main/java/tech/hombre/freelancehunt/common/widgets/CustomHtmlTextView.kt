package tech.hombre.freelancehunt.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import org.sufficientlysecure.htmltextview.HtmlTextView
import org.sufficientlysecure.htmltextview.OnClickATagListener
import tech.hombre.freelancehunt.common.provider.SchemeParser


class CustomHtmlTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HtmlTextView(context, attrs, defStyleAttr), OnClickATagListener {
    init {
        setOnClickATagListener(this)
    }

    fun setHtmlText(text: String, compress: Boolean = true, matchWidth: Boolean = true): Boolean = run {
        val getter = HtmlHttpImageGetter(this, null, matchWidth).apply {
            if (compress) enableCompressImage(true, 70)
        }
        try {
            setHtml(text.replace("<!--(.*?)-->".toRegex(RegexOption.DOT_MATCHES_ALL), "$1"), getter)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    override fun onClick(widget: View?, spannedText: String?, href: String?): Boolean {
        SchemeParser.launchUri(context, href.toString())
        return false
    }
}