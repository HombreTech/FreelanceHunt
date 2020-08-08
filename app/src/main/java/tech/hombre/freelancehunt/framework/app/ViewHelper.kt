package tech.hombre.freelancehunt.framework.app

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import tech.hombre.freelancehunt.R

object ViewHelper {

    @ColorInt
    fun getPrimaryColor(context: Context): Int {
        return getColorAttr(context, R.attr.colorPrimary)
    }

    @ColorInt
    fun getAccentColor(context: Context): Int {
        return getColorAttr(context, R.attr.colorAccent)
    }

    @ColorInt
    fun getColorAttr(context: Context, attr: Int): Int {
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, Color.LTGRAY)
        typedArray.recycle()
        return color
    }
}