package tech.hombre.freelancehunt.common.widgets

import android.content.Context
import android.graphics.*
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import java.util.*


class BadgeDrawerArrowDrawable(context: Context?) : DrawerArrowDrawable(context) {
    private val backgroundPaint: Paint = Paint()
    private val textPaint: Paint
    private var text: String = ""
    private var enabled = true

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (!enabled) {
            return
        }
        val bounds: Rect = bounds
        val x: Float =
            (1 - HALF_SIZE_FACTOR) * bounds.width()
        val y: Float = HALF_SIZE_FACTOR * bounds.height()
        canvas.drawCircle(
            x,
            y,
            SIZE_FACTOR * bounds.width(),
            backgroundPaint
        )
        if (text.isEmpty()) {
            return
        }
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(text, x, y + textBounds.height() / 2, textPaint)
    }

    fun setEnabled(enabled: Boolean) {
        if (this.enabled != enabled) {
            this.enabled = enabled
            invalidateSelf()
        }
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    fun setText(text: String) {
        if (!Objects.equals(this.text, text)) {
            this.text = text
            invalidateSelf()
        }
    }

    fun getText(): String {
        return text
    }

    fun setBackgroundColor(color: Int) {
        if (backgroundPaint.color != color) {
            backgroundPaint.color = color
            invalidateSelf()
        }
    }

    fun getBackgroundColor(): Int {
        return backgroundPaint.color
    }

    fun setTextColor(color: Int) {
        if (textPaint.color != color) {
            textPaint.color = color
            invalidateSelf()
        }
    }

    fun getTextColor(): Int {
        return textPaint.color
    }

    companion object {
        private const val SIZE_FACTOR = .2f
        private const val HALF_SIZE_FACTOR =
            SIZE_FACTOR / 2
    }

    init {
        backgroundPaint.color = Color.RED
        backgroundPaint.isAntiAlias = true
        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = SIZE_FACTOR * intrinsicHeight
    }
}