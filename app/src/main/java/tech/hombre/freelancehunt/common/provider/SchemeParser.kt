package tech.hombre.freelancehunt.common.provider

import android.content.Context
import android.content.Intent
import android.net.Uri
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.ui.contest.view.ContestDetailActivity
import tech.hombre.freelancehunt.ui.project.view.ProjectDetailActivity
import tech.hombre.freelancehunt.ui.threads.view.ThreadMessagesActivity

object SchemeParser {

    fun launchUri(context: Context, url: String) {
        val pattern = Regex("//freelancehunt..*?.*?/(.*?)/.*?(\\d+)")
        val results = pattern.find(url)?.groupValues
        if (results != null) {
            val type = results[1]
            val id = results[2].toInt()
            when (type) {
                "project" -> ProjectDetailActivity.startActivity(context, id)
                "contest" -> ContestDetailActivity.startActivity(context, id)
                "mailbox" -> ThreadMessagesActivity.startActivity(context, id, url)
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