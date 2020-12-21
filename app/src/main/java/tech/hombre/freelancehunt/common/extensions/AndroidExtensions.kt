package tech.hombre.freelancehunt.common.extensions

import android.content.Context
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.text.format.DateUtils.SECOND_IN_MILLIS
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tech.hombre.data.common.coroutine.CoroutineContextProvider
import tech.hombre.freelancehunt.App
import tech.hombre.freelancehunt.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min


inline fun <T> LiveData<T>.subscribe(
    owner: LifecycleOwner,
    crossinline onDataReceived: (T) -> Unit
) =
    observe(owner, Observer { onDataReceived(it) })

fun snackbar(@StringRes message: Int, rootView: View) =
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()

fun snackbar(message: String, rootView: View) =
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()

fun toast(message: String) =
    Toast.makeText(App.instance, message, Toast.LENGTH_SHORT).show()

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun String.parseFullDate(isIso: Boolean = false): Date? {
    return try {
        val timezone = if (SDK_INT >= android.os.Build.VERSION_CODES.N) "X" else "Z"
        SimpleDateFormat(
            if (isIso) "yyyy-MM-dd'T'HH:mm:ss$timezone" else "yyyy-MM-dd HH:mm:ss",
            Locale.forLanguageTag("ru")
        ).parse(this)
    } catch (e: Exception) {
        return null
    }
}

fun String.parseSimpleDate(): Date? {
    return try {
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.forLanguageTag("ru")
        ).parse(this)
    } catch (e: Exception) {
        return null
    }
}

fun Date?.getTimeAgo(): CharSequence {
    this?.let {
        val now = System.currentTimeMillis()
        return getRelativeTimeSpanString(this.time, now, SECOND_IN_MILLIS)
    }
    return "-"
}

fun Date?.getSimpleTimeAgo(context: Context): String {
    this?.let {
        return calcTimeAgo(context, this.time)
    }
    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(this)
}


private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val WEEK_MILLIS = 7 * DAY_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS.toLong()
private const val YEAR_MILLIS = 12 * MONTH_MILLIS

fun calcTimeAgo(context: Context, time: Long): String {
    var time = time
    if (time < 1000000000000L) {
        time *= 1000
    }
    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return ""
    }
    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> {
            context.getString(R.string.just_now)
        }
        diff < 2 * MINUTE_MILLIS -> {
            1.getEnding(context, R.array.ending_minutes)
        }
        diff < 50 * MINUTE_MILLIS -> {
            (diff / MINUTE_MILLIS).toInt().getEnding(context, R.array.ending_minutes)
        }
        diff < 90 * MINUTE_MILLIS -> {
            1.getEnding(context, R.array.ending_hours)
        }
        diff < 24 * HOUR_MILLIS -> {
            (diff / HOUR_MILLIS).toInt().getEnding(context, R.array.ending_hours)
        }
        diff < 48 * HOUR_MILLIS -> {
            context.getString(R.string.yesterday)
        }
        diff < WEEK_MILLIS -> {
            (diff / DAY_MILLIS).toInt().getEnding(context, R.array.ending_days)
        }
        diff < MONTH_MILLIS -> {
            (diff / WEEK_MILLIS).toInt().getEnding(context, R.array.ending_weeks)
        }
        diff < YEAR_MILLIS -> {
            (diff / MONTH_MILLIS).toInt().getEnding(context, R.array.ending_month)
        }
        diff > YEAR_MILLIS -> {
            if ((diff / YEAR_MILLIS) > 1) {
                val month = (diff / MONTH_MILLIS) - ((diff / YEAR_MILLIS) * 12)
                if (month > 0) (diff / YEAR_MILLIS).toInt()
                    .getEnding(context, R.array.ending_years) + " и " + month.toInt()
                    .getEnding(context, R.array.ending_month) else (diff / YEAR_MILLIS).toInt()
                    .getEnding(context, R.array.ending_years)
            } else (diff / YEAR_MILLIS).toInt().getEnding(context, R.array.ending_years)
        }
        else -> DateFormat.getDateInstance(DateFormat.MEDIUM).format(time)
    }
}

fun calculateAge(birthDate: Date): Int {
    val now = System.currentTimeMillis()
    val timeBetween = now - birthDate.time
    val yearsBetween = timeBetween / 3.15576e+10
    return floor(yearsBetween).toInt()
}

fun String.collapse(limit: Int): String {
    return if (this.length > limit)
        this.substring(
            0,
            limit - 1
        ) + "…" else this

}

fun getDrawableIdByName(name: String) =
    App.instance.resources.getIdentifier(name, "drawable", App.instance.packageName)

inline fun View.onClick(crossinline onClick: () -> Unit) {
    setOnClickListener { onClick() }
}

fun FragmentManager.switch(
    containerId: Int,
    newFrag: Fragment,
    tag: String,
    animation: Boolean = true
) {
    var current = findFragmentByTag(tag)
    beginTransaction()
        .apply {
            primaryNavigationFragment?.let { hide(it) }
            if (current == null) {
                current = newFrag
                add(containerId, current!!, tag)
            } else {
                show(current!!)
            }
        }
        .setTransition(if (animation) FragmentTransaction.TRANSIT_FRAGMENT_OPEN else FragmentTransaction.TRANSIT_NONE)
        .setPrimaryNavigationFragment(current)
        .setReorderingAllowed(true)
        .commitNowAllowingStateLoss()
}

fun Boolean.toVisibleState(): Int {
    return if (this) View.VISIBLE else View.GONE
}

fun String.extension(): String {
    return this.substringAfterLast('.', "")
}

fun Long.humanReadableBytes(): String {
    val b = when (this) {
        Long.MIN_VALUE -> Long.MAX_VALUE
        else -> abs(this)
    }
    return when {
        b < 1024L -> "$this B"
        b <= 0xfffccccccccccccL shr 40 -> "%.1f KB".format(Locale.UK, this / 1024.0)
        b <= 0xfffccccccccccccL shr 30 -> "%.1f MB".format(Locale.UK, this / 1048576.0)
        b <= 0xfffccccccccccccL shr 20 -> "%.1f GB".format(Locale.UK, this / 1.073741824E9)
        b <= 0xfffccccccccccccL shr 10 -> "%.1f TB".format(Locale.UK, this / 1.099511627776E12)
        b <= 0xfffccccccccccccL -> "%.1f PiB".format(Locale.UK, (this shr 10) / 1.099511627776E12)
        else -> "%.1f EB".format(Locale.UK, (this shr 20) / 1.099511627776E12)
    }
}

fun FragmentActivity.showFragment(
    fragment: Fragment, @IdRes container: Int
) {
    supportFragmentManager.beginTransaction()
        .replace(container, fragment)
        .setPrimaryNavigationFragment(fragment)
        .setReorderingAllowed(true)
        .commitAllowingStateLoss()
}

fun Int.getEnding(context: Context, @ArrayRes endsId: Int): String {
    val ends = context.resources.getStringArray(endsId)
    val cases = intArrayOf(2, 0, 1, 1, 1, 2)
    val a = (this % 100 in 5..19)
    val b = cases[min(this % 10, 5)]
    return this.toString() + " " + ends[if (a) 2 else b]
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        }
        false
    }
}

fun getMimeType(filepath: String?): String {
    if (filepath == null) return ""

    val url = filepath.replace(" ", "")
    var type: String? = null

    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
    }

    if (type == null) {
        val cR = App.instance.contentResolver
        type = cR.getType(Uri.parse(url))
    }

    if (type == null) {
        type = "*/*"
    }
    return type
}

fun View.blink(
    times: Int = Animation.INFINITE,
    duration: Long = 100L,
    offset: Long = 20L,
    minAlpha: Float = 0.0f,
    maxAlpha: Float = 1.0f,
    repeatMode: Int = Animation.REVERSE
) {
    startAnimation(AlphaAnimation(minAlpha, maxAlpha).also {
        it.duration = duration
        it.startOffset = offset
        it.repeatMode = repeatMode
        it.repeatCount = times
    })
}

inline fun ViewModel.launch(
    coroutineContext: CoroutineContext = CoroutineContextProvider().main,
    crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    return viewModelScope.launch(coroutineContext) { block() }
}