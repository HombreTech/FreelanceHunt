package tech.hombre.freelancehunt.ui.main.view.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.work.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.data.local.LocalProperties
import tech.hombre.data.local.LocalProperties.Companion.KEY_APP_LANGUAGE
import tech.hombre.data.local.LocalProperties.Companion.KEY_APP_THEME
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_FEED
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_INTERVAL
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_MESSAGES
import tech.hombre.data.local.LocalProperties.Companion.KEY_WORKER_UNMETERED
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.IS_PREMIUM
import tech.hombre.freelancehunt.common.SKU_PREMIUM
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.framework.billing.BillingClientModule
import tech.hombre.freelancehunt.framework.tasks.FeedWorker
import tech.hombre.freelancehunt.framework.tasks.ThreadsWorker
import tech.hombre.freelancehunt.ui.login.view.LoginActivity
import java.util.concurrent.TimeUnit


class SettingFragment : PreferenceFragmentCompat(), KoinComponent,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val appPreferences: LocalProperties by inject()

    private val billingClient: BillingClientModule by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "preferences"
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        setPreferencesFromResource(R.xml.preferences, rootKey)
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
                    val interval = appPreferences.getWorkerInterval()
                    if (interval >= 60) {
                        recreateTasks(true, true)
                    } else {
                        if (IS_PREMIUM) {
                            recreateTasks(true, true)
                        } else {
                            resetWorkerInterval(true)
                            premiumDialog()
                        }
                    }
                }
                KEY_WORKER_UNMETERED -> {
                    recreateTasks(true, true)
                }
                KEY_APP_LANGUAGE -> {
                    recreateActivity()
                }
                KEY_APP_THEME -> {
                    recreateActivity()
                }
                else -> {
                }
            }

        }
    }

    private fun recreateActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        with(intent) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        activity?.finish()
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

    private fun premiumDialog() {
        with(
            AlertDialog.Builder(
                requireContext(),
                AppHelper.getDialogTheme(appPreferences.getAppTheme())
            )
        ) {
            setTitle(getString(R.string.freelancehunt_premium))
            setMessage(getString(R.string.premium_caption))
            setPositiveButton(android.R.string.yes) { dialog: DialogInterface, _: Int ->
                billingClient.launchBilling(requireActivity(), SKU_PREMIUM)
            }
            setNegativeButton(android.R.string.no) { dialog: DialogInterface, _: Int ->
            }
            show()
        }
    }

    private fun resetWorkerInterval(reset: Boolean) {
        if (reset) preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(
            this
        )
        appPreferences.resetWorkerInterval()
        val intervalList =
            preferenceManager.findPreference<Preference>(KEY_WORKER_INTERVAL) as ListPreference?
        intervalList?.value = appPreferences.getWorkerInterval().toString()
        if (reset) preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    companion object {
        @Keep
        val TAG = SettingFragment::class.java.simpleName
    }

}