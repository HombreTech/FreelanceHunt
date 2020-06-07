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

    fun setCurrentUserType(type: String) {
        sharedPreferences.edit().putString(KEY_USER_TYPE, type).apply()
    }

    fun resetPreferences() {
        sharedPreferences.edit().clear() .apply()
    }

    fun getCurrentUserId(): Int = sharedPreferences.getInt(KEY_USER_ID, -1)

    fun setCurrentUserId(id: Int) {
        sharedPreferences.edit().putInt(KEY_USER_ID, id).apply()
    }

    fun getCurrentUserType(): String = sharedPreferences.getString(KEY_USER_TYPE, "") ?: ""

    fun getCurrentUserProfile(): MyProfile.Data.Attributes? {
        val json = sharedPreferences.getString(KEY_USER_PROFILE, "")
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MyProfile.Data.Attributes>() {}.type
            Gson().fromJson(json, type)
        } else null
    }

    fun getFeedWorkerInterval(): Long = sharedPreferences.getLong(KEY_WORKER_FEED_INTERVAL, 15)

    fun setFeedWorkerInterval(minutes: Long) {
        sharedPreferences.edit().putLong(KEY_WORKER_FEED_INTERVAL, minutes).apply()
    }

    fun getMessagesWorkerInterval(): Long = sharedPreferences.getLong(KEY_WORKER_MESSAGES_INTERVAL, 15)

    fun setMessagesWorkerInterval(minutes: Long) {
        sharedPreferences.edit().putLong(KEY_WORKER_MESSAGES_INTERVAL, minutes).apply()
    }

    fun getLastFeedId(): Long = sharedPreferences.getLong(KEY_LAST_FEED_ID, -1)

    fun setLastFeedId(id: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_FEED_ID, id).apply()
    }

    fun getLastMessageId(): Int = sharedPreferences.getInt(KEY_LAST_MESSAGE_ID, -1)

    fun setLastMessageId(id: Int) {
        sharedPreferences.edit().putInt(KEY_LAST_MESSAGE_ID, id).apply()
    }

    companion object {
        private const val KEY_API_TOKEN = "KEY_API_TOKEN"
        private const val KEY_USER_PROFILE = "KEY_USER_PROFILE"
        private const val KEY_USER_TYPE = "KEY_USER_TYPE"
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_LAST_FEED_ID = "KEY_LAST_FEED_ID"
        private const val KEY_LAST_MESSAGE_ID = "KEY_LAST_MESSAGE_ID"
        private const val KEY_WORKER_FEED_INTERVAL = "KEY_WORKER_FEED_INTERVAL"
        private const val KEY_WORKER_MESSAGES_INTERVAL = "KEY_WORKER_MESSAGES_INTERVAL"
    }

}