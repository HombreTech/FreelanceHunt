package tech.hombre.freelancehunt.framework.notifications

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.CHANNEL_ID
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.routing.AppNavigator
import tech.hombre.freelancehunt.ui.login.view.LoginActivity
import tech.hombre.freelancehunt.ui.main.view.activities.MainActivity


class AndroidNotificationService constructor(val appContext: Application) : NotificationService {

    override fun notify(msg: SimpleNotification) {

        if (!isAppIsInBackground(appContext)) return

        val notificationIntent = Intent(
            appContext,
            LoginActivity::class.java
        ).apply {
            putExtra(AppNavigator.SCREEN_TYPE, msg.screenType)
            putExtra(EXTRA_1, true)
        }

        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val contentIntent: PendingIntent? = PendingIntent.getActivity(
            appContext,
            msg.id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        fun createNotification(): Notification? {
            val styleBox = NotificationCompat.InboxStyle()
            msg.messages.forEach { line ->
                styleBox.addLine(HtmlCompat.fromHtml(line, HtmlCompat.FROM_HTML_MODE_LEGACY))
            }
            if (msg.count > 0) styleBox.setSummaryText(
                appContext.getString(R.string.more) + msg.count
            )
            return NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(contentIntent)
                .setContentTitle(msg.title)
                .setStyle(styleBox)
                .setChannelId(CHANNEL_ID).build().apply { flags = Notification.FLAG_AUTO_CANCEL }
        }

        val mNotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(CHANNEL_ID, appContext.getString(R.string.app_name), importance)
            mNotificationManager.createNotificationChannel(mChannel)
        }

        mNotificationManager.notify(msg.id, createNotification())
    }

    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses =
            am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

}