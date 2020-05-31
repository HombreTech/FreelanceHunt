package tech.hombre.freelancehunt.common.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.FeedType


fun String.prepareFeedMessage(context: Context): String {
    return this
        .replace("<.*?>".toRegex(), "")
        .replace(context.getString(R.string.type_project), "")
        .replace(context.getString(R.string.type_work), "")
        .capitalize()
}

fun feedTypeByMessage(context: Context, message: String): FeedType {
    return when (true) {
        message.contains(context.getString(R.string.type_project)) -> FeedType.PROJECT
        message.contains(context.getString(R.string.type_work)) -> FeedType.WORK
        else -> FeedType.UNKNOWN
    }
}

@DrawableRes
fun getTypeIcon(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.drawable.type_unknown
        FeedType.PROJECT -> R.drawable.type_projects
        FeedType.WORK -> R.drawable.type_briefcase
    }
}

@ColorRes
fun getTypeColor(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.color.typeUnknown
        FeedType.PROJECT -> R.color.typeProject
        FeedType.WORK -> R.color.typeWork
    }
}

@StringRes
fun getTypeLabel(type: FeedType): Int {
    return when (type) {
        FeedType.UNKNOWN -> R.string.other
        FeedType.PROJECT -> R.string.new_project
        FeedType.WORK -> R.string.new_work
    }
}