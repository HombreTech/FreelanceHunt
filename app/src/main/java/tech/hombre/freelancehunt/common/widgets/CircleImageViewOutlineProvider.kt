package tech.hombre.freelancehunt.common.widgets

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView

class CircleImageViewOutlineProvider(private val imageView: ImageView) :
    ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline) {
        val bounds: Rect = calculateBounds()
        outline.setRoundRect(bounds, (bounds.width() / 2).toFloat())
    }

    private fun calculateBounds(): Rect {
        val availableWidth =
            imageView.width - imageView.paddingLeft - imageView.paddingRight
        val availableHeight =
            imageView.height - imageView.paddingTop - imageView.paddingBottom
        val sideLength = Math.min(availableWidth, availableHeight)
        val left = imageView.paddingLeft + ((availableWidth - sideLength) / 2f).toInt()
        val top = imageView.paddingTop + ((availableHeight - sideLength) / 2f).toInt()
        return Rect(left, top, left + sideLength, top + sideLength)
    }

}