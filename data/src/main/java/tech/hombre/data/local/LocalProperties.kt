package tech.hombre.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tech.hombre.data.common.extensions.getCurrentDefaultLanguage
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

    fun getWorkerInterval(): Long = sharedPreferences.getString(KEY_WORKER_INTERVAL, "120").toLong()

    fun resetWorkerInterval() {
        sharedPreferences.edit().putString(KEY_WORKER_INTERVAL, "120").apply()
    }

    fun getLastFeedId(): Long = sharedPreferences.getLong(KEY_LAST_FEED_ID, 0)

    fun setLastFeedId(id: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_FEED_ID, id).apply()
    }

    fun getLastMessageId(): Long = sharedPreferences.getLong(KEY_LAST_MESSAGE_ID, 0)

    fun setLastMessageId(id: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_MESSAGE_ID, id).apply()
    }

    fun getLastProjectId(): Int = sharedPreferences.getInt(KEY_LAST_PROJECT_ID, 0)

    fun setLastProjectId(id: Int) {
        sharedPreferences.edit().putInt(KEY_LAST_PROJECT_ID, id).apply()
    }

    fun getWorkerFeedEnabled(): Boolean = sharedPreferences.getBoolean(KEY_WORKER_FEED, true)

    fun getWorkerMessagesEnabled(): Boolean = sharedPreferences.getBoolean(KEY_WORKER_MESSAGES, true)

    fun getWorkerProjectsEnabled(): Boolean = sharedPreferences.getBoolean(KEY_WORKER_PROJECTS, false)

    fun getWorkerUnmeteredEnabled(): Boolean = sharedPreferences.getBoolean(KEY_WORKER_UNMETERED, false)

    fun getAppLanguage(): String = sharedPreferences.getString(KEY_APP_LANGUAGE, getCurrentDefaultLanguage())

    fun getAppTheme(): String = sharedPreferences.getString(KEY_APP_THEME, "light")

    fun getProjectFilterOnlyMySkills(): Boolean = sharedPreferences.getBoolean(KEY_PROJECT_FILTER_MY_SKILLS, false)

    private fun setProjectFilterOnlyMySkills(value: Boolean) = sharedPreferences.edit().putBoolean(KEY_PROJECT_FILTER_MY_SKILLS, value).apply()

    fun getProjectFilterOnlyPlus(): Boolean = sharedPreferences.getBoolean(KEY_PROJECT_FILTER_ONLY_PLUS, false)

    private fun setProjectFilterOnlyMyPlus(value: Boolean) = sharedPreferences.edit().putBoolean(KEY_PROJECT_FILTER_ONLY_PLUS, value).apply()

    fun getProjectFilterSkills(): String = sharedPreferences.getString(KEY_PROJECT_FILTER_SKILLS, "")

    private fun setProjectFilterOnlySkills(value: String) = sharedPreferences.edit().putString(KEY_PROJECT_FILTER_SKILLS, value).apply()

    fun saveProjectFilter(onlyMySkills: Boolean, skills: String, onlyPlus: Boolean) {
        setProjectFilterOnlyMySkills(onlyMySkills)
        setProjectFilterOnlyMyPlus(onlyPlus)
        setProjectFilterOnlySkills(skills)
    }

    fun getProjectBidsListReversed(): Boolean = sharedPreferences.getBoolean(KEY_PROJECT_BIDS_LIST_REVERSED, false)

    fun isAutoStartPermissionsRequired(): Boolean = sharedPreferences.getBoolean(KEY_AUTOSTART_PERMISSIONS_REQUIRED, false)

    fun setAutoStartPermissionsRequired() = sharedPreferences.edit().putBoolean(KEY_AUTOSTART_PERMISSIONS_REQUIRED, true).apply()

    fun isDeveloperNotifyShowed(): Boolean = sharedPreferences.getBoolean(KEY_DEVELOPER_NOTIFY_SHOWED, false)

    fun setDeveloperNotifyShowed() = sharedPreferences.edit().putBoolean(KEY_DEVELOPER_NOTIFY_SHOWED, true).apply()

    companion object {
        const val KEY_API_TOKEN = "KEY_API_TOKEN"
        const val KEY_USER_PROFILE = "KEY_USER_PROFILE"
        const val KEY_USER_TYPE = "KEY_USER_TYPE"
        const val KEY_USER_ID = "KEY_USER_ID"
        const val KEY_LAST_FEED_ID = "KEY_LAST_FEED_ID"
        const val KEY_LAST_MESSAGE_ID = "KEY_LAST_MESSAGE_ID"
        const val KEY_LAST_PROJECT_ID = "KEY_LAST_PROJECT_ID"
        const val KEY_WORKER_FEED = "KEY_WORKER_FEED"
        const val KEY_WORKER_MESSAGES = "KEY_WORKER_MESSAGES"
        const val KEY_WORKER_PROJECTS = "KEY_WORKER_PROJECTS"
        const val KEY_WORKER_INTERVAL = "KEY_WORKER_INTERVAL"
        const val KEY_APP_LANGUAGE = "KEY_APP_LANGUAGE"
        const val KEY_APP_THEME = "KEY_APP_THEME"
        const val KEY_WORKER_UNMETERED = "KEY_WORKER_UNMETERED"
        const val KEY_PROJECT_BIDS_LIST_REVERSED = "KEY_PROJECT_BIDS_LIST_REVERSED"
        const val KEY_AUTOSTART_PERMISSIONS_REQUIRED = "KEY_AUTOSTART_PERMISSIONS_REQUIRED"
        const val KEY_DEVELOPER_NOTIFY_SHOWED = "KEY_DEVELOPER_NOTIFY_SHOWED"

        const val KEY_PROJECT_FILTER_MY_SKILLS = "KEY_PROJECT_FILTER_MY_SKILLS"
        const val KEY_PROJECT_FILTER_ONLY_PLUS = "KEY_PROJECT_FILTER_ONLY_PLUS"
        const val KEY_PROJECT_FILTER_SKILLS = "KEY_PROJECT_FILTER_SKILLS"
    }

}