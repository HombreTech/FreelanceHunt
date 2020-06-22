package tech.hombre.freelancehunt.framework.app

import android.content.Context
import android.os.Build
import java.util.*

object AppHelper {

    fun updateAppLanguage(context: Context, lang: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, lang)
        }
        updateResourcesLegacy(context, lang)
    }

    private fun updateResources(context: Context, language: String) {
        val locale = getLocale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String) {
        val locale = getLocale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun getLocale(language: String): Locale {
        var locale: Locale? = null
        if (language.equals("zh-rCN", ignoreCase = true)) {
            locale = Locale.SIMPLIFIED_CHINESE
        } else if (language.equals("zh-rTW", ignoreCase = true)) {
            locale = Locale.TRADITIONAL_CHINESE
        }
        if (locale != null) return locale
        val split = language.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        locale = if (split.size > 1) {
            Locale(split[0], split[1])
        } else {
            Locale(language)
        }
        return locale
    }
}