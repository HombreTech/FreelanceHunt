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

    override fun onClick(widget: View?, href: String?) {
        SchemeParser.launchUri(context, href.toString())
    }

    fun setHtmlText(text: String, fillWidth: Boolean = true) = run {
        val getter = HtmlHttpImageGetter(this, null, fillWidth).apply {
            enableCompressImage(true, 70)
        }
        setHtml(text, getter)
    }
}