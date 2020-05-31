package tech.hombre.freelancehunt.common.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import tech.hombre.freelancehunt.common.widgets.glide.GlideApp
import tech.hombre.freelancehunt.common.widgets.glide.SvgSoftwareLayerSetter


open class CustomImageView(context: Context, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {

    private lateinit var progressBar: CircularProgressDrawable

    private fun initProgressBar() {
        progressBar =
            CircularProgressDrawable(
                context
            )
        /*progressBar.colorSchemeColors = intArrayOf(
            ViewHelper.getPrimaryDarkColor(context)
        )*/
        progressBar.strokeWidth = 5f
        progressBar.centerRadius = 30f
        progressBar.start()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode) return
        initProgressBar()
        outlineProvider = CircleImageViewOutlineProvider(this)
    }

    fun setUrl(url: String, fit: Boolean = false, isCircle: Boolean = false) {
        val options = if (fit) with(RequestOptions()) {
            fitCenter()
            format(DecodeFormat.PREFER_ARGB_8888)
            override(900)
        } else with(RequestOptions()) {
            if (isCircle) circleCrop() else centerCrop()
            format(DecodeFormat.PREFER_ARGB_8888)
        }
        GlideApp
            .with(context)
            .load(url)
            .apply(options)
            .placeholder(progressBar)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }

    fun setUrlSVG(url: String) {
        GlideApp
            .with(context)
            .load(url)
            .placeholder(progressBar)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .listener(SvgSoftwareLayerSetter())
            .into(this)
    }

    fun clear() {
        setImageResource(0)
    }


}