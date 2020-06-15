package tech.hombre.freelancehunt.ui.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import tech.hombre.freelancehunt.common.provider.SchemeParser


class LinksParserActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate(intent)
    }

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

    private fun onCreate(intent: Intent?) {
        if (intent == null || intent.action == null) {
            finish()
            return
        }
        if (intent.action == Intent.ACTION_VIEW) {
            if (intent.data != null) {
                onUriReceived(intent.data)
            }
        }
        finish()
    }

    private fun onUriReceived(uri: Uri?) {
        SchemeParser.launchUri(this, uri.toString())
    }
}