package tech.hombre.freelancehunt.framework.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.data.local.LocalProperties
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.domain.repository.FeedListRepository
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.MAX_LINES
import tech.hombre.freelancehunt.framework.notifications.AndroidNotificationService
import tech.hombre.freelancehunt.framework.notifications.SimpleNotification
import tech.hombre.freelancehunt.routing.ScreenType

class FeedWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val feedRepository: FeedListRepository by inject()

    private val appPreferences: LocalProperties by inject()

    private val notificationService: AndroidNotificationService by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.e("FeedWorker", "doWork")
            if (appPreferences.getCurrentUserId() != -1) {
                feedRepository.getFeedSimpleList().onSuccess { feeds ->
                    val lastChecked = appPreferences.getLastFeedId()
                    val new = feeds.data.filter { it.attributes.is_new && it.id > lastChecked }
                    if (new.isNotEmpty()) {
                        Log.e("FeedWorker", "Notify")
                        appPreferences.setLastFeedId(new.first().id)
                        notificationService.notify(
                            SimpleNotification(
                                context.getString(R.string.notify_new_feed)
                                ,
                                new.map { "<b>${it.attributes.from?.login}</b> <i>${it.attributes.message}</i>" }
                                    .chunked(MAX_LINES)[0],
                                ScreenType.MAIN,
                                new.size - MAX_LINES
                            )
                        )
                    }
                }.onFailure { Log.e("FeedWorker", it.throwable.message, it.throwable) }
            } else Log.e("FeedWorker", "Skip")
            Result.success()
        } catch (e: Exception) {
            Log.e("FeedWorker", e.message, e)
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "FeedWorker"
    }


}


