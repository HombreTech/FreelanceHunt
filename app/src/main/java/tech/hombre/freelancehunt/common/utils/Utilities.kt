package tech.hombre.freelancehunt.common.utils

import android.os.Build
import android.text.InputFilter
import android.text.Spanned

object Utilities {

    class InputFilterMinMax : InputFilter {
        private var min: Int = 0
        private var max: Int = 0

        constructor(min: Int, max: Int) {
            this.min = min
            this.max = max
        }

        constructor(min: String, max: String) {
            this.min = Integer.parseInt(min)
            this.max = Integer.parseInt(max)
        }

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(min, max, input))
                    return null
            } catch (nfe: NumberFormatException) {
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }

    fun logDeviceInfo(): String {
        val result = StringBuilder()
        result.appendln("Board: ${Build.BOARD}")
        result.appendln("Brand: ${Build.BRAND}")
        result.appendln("Device: ${Build.DEVICE}")
        result.appendln("Display: ${Build.DISPLAY}")
        result.appendln("Hardware: ${Build.HARDWARE}")
        result.appendln("Manufacturer: ${Build.MANUFACTURER}")
        result.appendln("Product: ${Build.PRODUCT}")
        result.appendln("Version.Release: ${Build.VERSION.RELEASE}")
        result.appendln("Version.Codename: ${Build.VERSION.CODENAME}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result.appendln("Version.BaseOS: ${Build.VERSION.BASE_OS}")
            result.appendln("Version.SecurityPatch: ${Build.VERSION.SECURITY_PATCH}")
        }
        return result.toString()
    }
}