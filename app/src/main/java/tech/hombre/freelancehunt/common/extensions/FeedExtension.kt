package tech.hombre.freelancehunt.common.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.FeedType


fun String.prepareFeedMessage(context: Context): String {
    return this
        .replace("<a data-toggle=\\\"modal\\\".*?a>".toRegex(RegexOption.DOT_MATCHES_ALL), "")
        .replace("<a href=\"https:\\/\\/freelancehunt.com\\/mailbox.*>(.*?)<\\/a>".toRegex(RegexOption.DOT_MATCHES_ALL), "$1")
        .replace("<.*?>".toRegex(RegexOption.DOT_MATCHES_ALL), "")
        .replace(context.getString(R.string.type_project).toRegex(), "")
        .replace(context.getString(R.string.type_project_1).toRegex(), "")
        .replace(context.getString(R.string.type_work).toRegex(), "")
        .replace(context.getString(R.string.type_add_message).toRegex(), context.getString(R.string.message))
        .replace(context.getString(R.string.type_add_message_1).toRegex(), context.getString(R.string.message))
        .replace(context.getString(R.string.type_new_vacancy).toRegex(), "")
        .replace(context.getString(R.string.type_add_message_2).toRegex(), context.getString(R.string.message))
        .replace(context.getString(R.string.type_like_message).toRegex(), "")
        .replace(context.getString(R.string.type_forum_message).toRegex(), "")
        .replace(context.getString(R.string.type_forum_message_2).toRegex(), "")
        .replace(context.getString(R.string.type_invited_project).toRegex(), "")
        .replace("(${context.getString(R.string.type_appreciated)}) ".toRegex(), "$1 " + "★".repeat(this.split("star.svg").size) + " ")
        .trim()
        .capitalize()
}

fun feedTypeByMessage(context: Context, message: String): FeedType {
    return when (true) {
        message.contains(context.getString(R.string.type_project).toRegex()) -> FeedType.PROJECT
        message.contains(context.getString(R.string.type_project_1).toRegex()) -> FeedType.PROJECT
        message.contains(context.getString(R.string.type_work).toRegex()) -> FeedType.WORK
        message.contains(context.getString(R.string.type_like_message).toRegex()) -> FeedType.LIKE
        message.contains(context.getString(R.string.type_add_message).toRegex()) -> FeedType.MESSAGE
        message.contains(context.getString(R.string.type_invited_project).toRegex()) -> FeedType.PERSONAL_PROJECT
        message.contains(context.getString(R.string.type_forum_message).toRegex()) -> FeedType.FORUM_MESSAGE
        message.contains(context.getString(R.string.type_forum_message_2).toRegex()) -> FeedType.FORUM_MESSAGE
        message.contains(context.getString(R.string.type_appreciated).toRegex()) -> FeedType.APPRECIATED
        message.contains(context.getString(R.string.type_new_vacancy).toRegex()) -> FeedType.VACANCY
        else -> FeedType.UNKNOWN
    }
}

@DrawableRes
fun getTypeIcon(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.drawable.type_unknown
        FeedType.PROJECT -> R.drawable.type_projects
        FeedType.WORK -> R.drawable.type_briefcase
        FeedType.MESSAGE -> R.drawable.type_messages
        FeedType.LIKE -> R.drawable.type_messages
        FeedType.FORUM_MESSAGE -> R.drawable.type_messages
        FeedType.PERSONAL_PROJECT -> R.drawable.project_small
        FeedType.APPRECIATED -> R.drawable.type_appreciated
        FeedType.VACANCY -> R.drawable.type_new_vacancy
        else -> R.drawable.type_unknown
    }
}

@ColorRes
fun getTypeColor(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.color.typeUnknown
        FeedType.PROJECT -> R.color.typeProject
        FeedType.WORK -> R.color.typeWork
        FeedType.MESSAGE -> R.color.typeForumMessage
        FeedType.LIKE -> R.color.typeForumMessage
        FeedType.FORUM_MESSAGE -> R.color.typeForumMessage
        FeedType.PERSONAL_PROJECT -> R.color.typeWork
        FeedType.VACANCY -> R.color.typeWork
        else-> R.color.typeUnknown
    }
}

@StringRes
fun getTypeLabel(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.string.other
        FeedType.PROJECT -> R.string.new_project
        FeedType.WORK -> R.string.new_work
        FeedType.MESSAGE -> R.string.new_forum_message
        FeedType.LIKE -> R.string.new_forum_message
        FeedType.FORUM_MESSAGE -> R.string.new_forum_message
        FeedType.PERSONAL_PROJECT -> R.string.new_personal_project
        FeedType.APPRECIATED -> R.string.contests
        FeedType.VACANCY -> R.string.vacancy
        else -> R.string.other
    }
}