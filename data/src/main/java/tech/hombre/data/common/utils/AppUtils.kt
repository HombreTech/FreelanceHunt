package tech.hombre.data.common.utils

import android.content.Context
import android.os.Build
import java.util.*

fun getCurrentLocale(context: Context): Locale? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0]
    } else {
        context.resources.configuration.locale
    }
}

fun getCurrentLocaleLang(context: Context): String {
    return getCurrentLocale(context)?.language ?: "ru"
}