package tech.hombre.freelancehunt.common.provider

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.routing.AppNavigator


object SchemeParser : KoinComponent {

    val appNavigator: AppNavigator by inject()

    fun launchUri(context: Context, url: String) {
        val pattern = Regex("//freelancehunt.com.*?/(.*?)/.*?(\\d+)")
        val results = pattern.find(url)?.groupValues
        if (results != null) {
            val type = results[1]
            val id = results[2].toInt()
            when (type) {
                "project" -> appNavigator.showProjectDetails(id)
                "contest" -> appNavigator.showContestDetails(id)
                "mailbox" -> appNavigator.showThread(id, url)
                else -> notImplementedUrl(context, url)
            }
        } else openUrl(context, url)
    }

    private fun openUrl(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(Intent.createChooser(browserIntent, context.getString(R.string.open)))
    }

    private fun notImplementedUrl(context: Context, url: String) {
        openUrl(context, url)
    }

}