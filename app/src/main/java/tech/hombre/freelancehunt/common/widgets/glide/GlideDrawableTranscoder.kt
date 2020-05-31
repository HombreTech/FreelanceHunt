package tech.hombre.freelancehunt.common.widgets.glide

import android.graphics.Bitmap
import android.graphics.Picture
import android.graphics.RectF
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder

class GlideDrawableTranscoder : ResourceTranscoder<GlideDecodedResource, PictureDrawable> {
    override fun transcode(
        toTranscode: Resource<GlideDecodedResource>,
        options: Options
    ): Resource<PictureDrawable>? {
        val data = toTranscode.get()

        return if (data.svg != null) {
            val picture = data.svg.renderToPicture()
            val drawable = PictureDrawable(picture)
            SimpleResource(drawable)
        } else if (data.bitmap != null)
            SimpleResource(PictureDrawable(renderToPicture(data.bitmap)))
        else null
    }

    private fun renderToPicture(bitmap: Bitmap): Picture {
        val picture = Picture()
        val canvas = picture.beginRecording(bitmap.width, bitmap.height)
        canvas.drawBitmap(
            bitmap,
            null,
            RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat()),
            null
        )
        picture.endRecording();

        return picture
    }
}