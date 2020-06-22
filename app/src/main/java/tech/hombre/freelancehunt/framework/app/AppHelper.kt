package tech.hombre.freelancehunt.framework.app

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.StyleRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.ThemeType
import tech.hombre.freelancehunt.ui.login.view.LoginActivity
import java.util.*

object AppHelper {

    fun updateTheme(activity: Activity, theme: String) {
        if (activity is LoginActivity) {
            return
        }
        activity.setTheme(getTheme(theme))
        applyNavBarColor(activity, theme)
    }

    @StyleRes
    private fun getTheme(theme: String): Int {
        return when (theme) {
            "light" -> R.style.AppTheme
            "dark" -> R.style.AppThemeDark
            else -> R.style.AppTheme
        }
    }

    @StyleRes
    fun getDialogTheme(theme: String): Int {
        return when (theme) {
            "light" -> R.style.DialogThemeLight
            "dark" -> R.style.DialogThemeDark
            else -> R.style.DialogThemeLight
        }
    }

    private fun applyNavBarColor(activity: Activity, theme: String) {
        val themeType = ThemeType.values().find { it.theme == theme }
        if (themeType != ThemeType.LIGHT) {
            activity.window.navigationBarColor = ViewHelper.getPrimaryColor(activity)
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && themeType == ThemeType.LIGHT) {
            activity.window.navigationBarColor = ViewHelper.getPrimaryColor(activity)
            activity.window.decorView.systemUiVisibility = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

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