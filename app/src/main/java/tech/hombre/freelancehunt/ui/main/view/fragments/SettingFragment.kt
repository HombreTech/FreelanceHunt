package tech.hombre.freelancehunt.ui.main.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.Keep
import androidx.preference.PreferenceFragmentCompat
import androidx.work.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.data.local.LocalProperties
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_FEED
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_INTERVAL
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_MESSAGES
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_UNMETERED
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.framework.tasks.FeedWorker
import tech.hombre.freelancehunt.framework.tasks.ThreadsWorker
import java.util.concurrent.TimeUnit


class SettingFragment : PreferenceFragmentCompat(), KoinComponent,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val appPreferences: LocalProperties by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "preferences"
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }


    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences?.let { pref ->
            when (key) {
                KEY_WORKER_FEED -> {
                    val state = pref.getBoolean(key, false)
                    if (state) {
                        recreateTasks(true, false)
                    } else WorkManager.getInstance(requireContext())
                        .cancelUniqueWork(FeedWorker.WORK_NAME)
                }
                KEY_WORKER_MESSAGES -> {
                    val state = pref.getBoolean(key, false)
                    if (state) {
                        recreateTasks(false, true)
                    } else WorkManager.getInstance(requireContext())
                        .cancelUniqueWork(ThreadsWorker.WORK_NAME)
                }
                KEY_WORKER_INTERVAL -> {
                    recreateTasks(true, true)
                }
                KEY_WORKER_UNMETERED -> {
                    recreateTasks(true, true)
                }
                else -> {
                }
            }

        }
    }

    private fun recreateTasks(feed: Boolean, messages: Boolean) {
        val interval = appPreferences.getWorkerInterval()
        val networkType =
            if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        if (feed) WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            FeedWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<FeedWorker>(
                interval, TimeUnit.MINUTES
            ).setConstraints(constrains).build()
        )

        if (messages) WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            ThreadsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<ThreadsWorker>(
                interval, TimeUnit.MINUTES
            ).setConstraints(constrains).build()
        )

    }

    companion object {
        @Keep
        val TAG = SettingFragment::class.java.simpleName
    }

}