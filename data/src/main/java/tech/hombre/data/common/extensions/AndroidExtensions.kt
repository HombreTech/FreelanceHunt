package tech.hombre.data.common.extensions

import android.os.Build
import android.os.LocaleList
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hombre.data.networking.GENERAL_NETWORK_ERROR
import tech.hombre.domain.model.ApiError
import java.util.*

fun String.isJson(): Boolean {
    try {
        JSONObject(this)
    } catch (ex: JSONException) {
        try {
            JSONArray(this)
        } catch (e: JSONException) {
            return false
        }
    }
    return true
}

fun <T : Any?> String.toPOJO(`class`: Class<T>): T? {
    return if (this.isJson()) Gson().fromJson(this, `class`) else
        null
}

fun String.getApiError(): ApiError {
    return if (this.isJson()) {
        try {
            Gson().fromJson(this, ApiError::class.java)
        } catch (exception: Exception) {
            ApiError(error = ApiError.Error(title = GENERAL_NETWORK_ERROR))
        }
    } else {
        ApiError(error = ApiError.Error(title = GENERAL_NETWORK_ERROR))
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun getCurrentDefaultLanguage(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocaleList.getDefault()[0].language
    } else {
        Locale.getDefault().language
    }
}