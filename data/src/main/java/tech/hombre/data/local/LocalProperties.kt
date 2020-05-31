package tech.hombre.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tech.hombre.domain.model.MyProfile

class LocalProperties(private val sharedPreferences: SharedPreferences) {

    fun setAccessToken(accessToken: String?) {
        sharedPreferences.edit()
            .putString(KEY_API_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(KEY_API_TOKEN, "") ?: ""
    }

    fun setCurrentUserProfile(profile: MyProfile.Data.Attributes) {
        sharedPreferences.edit().putString(KEY_USER_PROFILE, Gson().toJson(profile))
            .apply()
    }

    fun getCurrentUserProfile(): MyProfile.Data.Attributes? {
        val json = sharedPreferences.getString(KEY_USER_PROFILE, "")
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MyProfile.Data.Attributes>() {}.type
            Gson().fromJson(json, type)
        } else null
    }

    companion object {
        private const val KEY_API_TOKEN = "KEY_API_TOKEN"
        private const val KEY_USER_PROFILE = "KEY_USER_PROFILE"
    }

}