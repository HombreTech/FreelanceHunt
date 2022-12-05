package tech.hombre.freelancehunt.framework.tasks

import android.content.Context
import android.util.Log
import androidx.work.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.hombre.data.local.LocalProperties
import java.util.concurrent.TimeUnit

class TasksManger(val context: Context) : KoinComponent {

    private val appPreferences: LocalProperties by inject()

    fun setupTasks() {
        Log.d("TasksManager", "Setup tasks")
        val networkType =
            if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        val interval = appPreferences.getWorkerInterval()

        if (appPreferences.getWorkerFeedEnabled())
            WorkManager.getInstance(context).enqueueUniqueWork(
                FeedWorker.WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<FeedWorker>()
                    .setInitialDelay(interval, TimeUnit.MINUTES)
                    .setConstraints(constrains)
                    .build()
            )
        if (appPreferences.getWorkerMessagesEnabled())
            WorkManager.getInstance(context).enqueueUniqueWork(
                ThreadsWorker.WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<ThreadsWorker>()
                    .setInitialDelay(interval, TimeUnit.MINUTES)
                    .setConstraints(constrains)
                    .build()
            )
        if (appPreferences.getWorkerProjectsEnabled())
            WorkManager.getInstance(context).enqueueUniqueWork(
                ProjectsWorker.WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<ProjectsWorker>()
                    .setInitialDelay(interval, TimeUnit.MINUTES)
                    .setConstraints(constrains)
                    .build()
            )
    }

    fun recreateTasks(feed: Boolean = false, messages: Boolean = false, projects: Boolean = false) {
        Log.d("TasksManager", "recreate tasks [feed = $feed, messages = $messages, projects = $projects]")
        val interval = appPreferences.getWorkerInterval()
        val networkType =
            if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        if (feed) WorkManager.getInstance(context).enqueueUniqueWork(
            FeedWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<FeedWorker>()
                .setInitialDelay(interval, TimeUnit.MINUTES)
                .setConstraints(constrains)
                .build()
        )

        if (messages) WorkManager.getInstance(context).enqueueUniqueWork(
            ThreadsWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<ThreadsWorker>()
                .setInitialDelay(interval, TimeUnit.MINUTES)
                .setConstraints(constrains)
                .build()
        )

        if (projects) WorkManager.getInstance(context).enqueueUniqueWork(
            ProjectsWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<ProjectsWorker>()
                .setInitialDelay(interval, TimeUnit.MINUTES)
                .setConstraints(constrains)
                .build()
        )

    }

}