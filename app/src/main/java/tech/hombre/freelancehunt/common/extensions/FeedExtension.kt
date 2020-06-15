package tech.hombre.freelancehunt.common.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.FeedType


fun String.prepareFeedMessage(context: Context): String {
    return this
        .replace("<a data-toggle=\\\"modal\\\".*?a>".toRegex(), "")
        .replace("<.*?>".toRegex(), "")
        .replace(context.getString(R.string.type_project), "")
        .replace(context.getString(R.string.type_work), "")
        .replace(context.getString(R.string.type_add_message), context.getString(R.string.message))
        .trim()
        .capitalize()
}

fun feedTypeByMessage(context: Context, message: String): FeedType {
    return when (true) {
        message.contains(context.getString(R.string.type_project)) -> FeedType.PROJECT
        message.contains(context.getString(R.string.type_work)) -> FeedType.WORK
        message.contains(context.getString(R.string.type_like_message)) -> FeedType.LIKE
        message.contains(context.getString(R.string.type_add_message)) -> FeedType.FORUM_MESSAGE
        else -> FeedType.UNKNOWN
    }
}

@DrawableRes
fun getTypeIcon(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.drawable.type_unknown
        FeedType.PROJECT -> R.drawable.type_projects
        FeedType.WORK -> R.drawable.type_briefcase
        FeedType.FORUM_MESSAGE -> R.drawable.type_messages
        else -> R.drawable.type_unknown
    }
}

@ColorRes
fun getTypeColor(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.color.typeUnknown
        FeedType.PROJECT -> R.color.typeProject
        FeedType.WORK -> R.color.typeWork
        FeedType.FORUM_MESSAGE -> R.color.typeForumMessage
        else-> R.color.typeUnknown
    }
}

@StringRes
fun getTypeLabel(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.string.other
        FeedType.PROJECT -> R.string.new_project
        FeedType.WORK -> R.string.new_work
        FeedType.FORUM_MESSAGE -> R.string.new_forum_message
        else -> R.string.other
    }
}