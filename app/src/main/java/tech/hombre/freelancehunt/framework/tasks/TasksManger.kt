package tech.hombre.freelancehunt.framework.tasks

import android.content.Context
import androidx.work.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.hombre.data.local.LocalProperties
import java.util.concurrent.TimeUnit

class TasksManger(val context: Context) : KoinComponent {

    private val appPreferences: LocalProperties by inject()

    fun setupTasks() {
        val networkType =
            if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        if (appPreferences.getWorkerFeedEnabled())
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                FeedWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<FeedWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).build()
            )
        if (appPreferences.getWorkerMessagesEnabled())
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                ThreadsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<ThreadsWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).build()
            )
        if (appPreferences.getWorkerProjectsEnabled())
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                ProjectsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<ProjectsWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).build()
            )
    }

    fun recreateTasks(feed: Boolean, messages: Boolean, projects: Boolean) {
        val interval = appPreferences.getWorkerInterval()
        val networkType =
            if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        if (feed) WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            FeedWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<FeedWorker>(
                interval, TimeUnit.MINUTES
            ).setConstraints(constrains).build()
        )

        if (messages) WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ThreadsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<ThreadsWorker>(
                interval, TimeUnit.MINUTES
            ).setConstraints(constrains).build()
        )

        if (projects) WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ProjectsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<ProjectsWorker>(
                interval, TimeUnit.MINUTES
            ).setConstraints(constrains).build()
        )

    }

}