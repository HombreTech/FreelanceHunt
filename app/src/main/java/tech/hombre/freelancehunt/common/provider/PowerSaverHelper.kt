package tech.hombre.freelancehunt.common.provider

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

object PowerSaverHelper {
    enum class WhiteListedInBatteryOptimizations {
        WHITE_LISTED, NOT_WHITE_LISTED, ERROR_GETTING_STATE, IRRELEVANT_OLD_ANDROID_API
    }

    private fun getIfAppIsWhiteListedFromBatteryOptimizations(
        context: Context,
        packageName: String = context.packageName
    ): WhiteListedInBatteryOptimizations {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return WhiteListedInBatteryOptimizations.IRRELEVANT_OLD_ANDROID_API
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
            ?: return WhiteListedInBatteryOptimizations.ERROR_GETTING_STATE
        return if (pm.isIgnoringBatteryOptimizations(packageName)) WhiteListedInBatteryOptimizations.WHITE_LISTED else WhiteListedInBatteryOptimizations.NOT_WHITE_LISTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("BatteryLife", "InlinedApi")
    @RequiresPermission(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    fun prepareIntentForWhiteListingOfBatteryOptimization(
        context: Context,
        packageName: String = context.packageName,
        alsoWhenWhiteListed: Boolean = false,
        onSuccess: Unit
    ): PowerSaveIntent {
        var whiteListed = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return PowerSaveIntent(null, whiteListed)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            ) == PackageManager.PERMISSION_DENIED
        )
            return PowerSaveIntent(null, whiteListed)
        val appIsWhiteListedFromPowerSave: WhiteListedInBatteryOptimizations =
            getIfAppIsWhiteListedFromBatteryOptimizations(context, packageName)
        var intent: Intent? = null
        when (appIsWhiteListedFromPowerSave) {
            WhiteListedInBatteryOptimizations.WHITE_LISTED -> {
                whiteListed = true
                if (alsoWhenWhiteListed) intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS) else onSuccess.run {  }
            }
            WhiteListedInBatteryOptimizations.NOT_WHITE_LISTED -> intent =
                Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(Uri.parse("package:$packageName"))
            WhiteListedInBatteryOptimizations.ERROR_GETTING_STATE, WhiteListedInBatteryOptimizations.IRRELEVANT_OLD_ANDROID_API -> {
            }
        }
        return PowerSaveIntent(intent, whiteListed)
    }

    data class PowerSaveIntent(
        val intent: Intent?,
        val isWhiteListed: Boolean
    )
}