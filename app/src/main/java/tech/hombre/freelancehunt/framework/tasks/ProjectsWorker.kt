package tech.hombre.freelancehunt.framework.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.hombre.data.local.LocalProperties
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.domain.repository.ProjectsListRepository
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.MAX_LINES
import tech.hombre.freelancehunt.common.NOTIFY_PROJECTS_ID
import tech.hombre.freelancehunt.framework.notifications.AndroidNotificationService
import tech.hombre.freelancehunt.framework.notifications.SimpleNotification
import tech.hombre.freelancehunt.routing.ScreenType

class ProjectsWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val projectsRepository: ProjectsListRepository by inject()

    private val appPreferences: LocalProperties by inject()

    private val notificationService: AndroidNotificationService by inject()

    private val tasksManger: TasksManger by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("ProjectsWorker", "doWork")
            if (appPreferences.getCurrentUserId() != -1) {
                val onlyMySkills = appPreferences.getProjectFilterOnlyMySkills()
                val onlyForPlus = appPreferences.getProjectFilterOnlyPlus()
                val skills = appPreferences.getProjectFilterSkills()
                projectsRepository.getSimpleProjectsList(1, onlyMySkills, onlyForPlus, skills, null)
                    .onSuccess { projects ->
                        val lastChecked = appPreferences.getLastProjectId()
                        val new = projects.data.filter { it.id > lastChecked }
                        if (new.isNotEmpty()) {
                            Log.d("ProjectsWorker", "Notify")
                            appPreferences.setLastProjectId(new.first().id)
                            notificationService.notify(
                                SimpleNotification(
                                    String.format(
                                        context.getString(R.string.notify_new_projects),
                                        new.size
                                    ),
                                    new.map { "<b>${it.attributes.name}</b> <i>${it.attributes.skills[0].name}</i>" }
                                        .chunked(MAX_LINES)[0],
                                    ScreenType.MAIN,
                                    new.size - MAX_LINES,
                                    NOTIFY_PROJECTS_ID
                                )
                            )
                        }
                    }.onFailure { Log.e("ProjectsWorker", it.throwable.message, it.throwable) }
            } else Log.d("ProjectsWorker", "Skip")
            tasksManger.recreateTasks(projects = true)
            Result.success()
        } catch (e: Exception) {
            Log.e("ProjectsWorker", e.message, e)
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "ProjectsWorker"
    }


}


