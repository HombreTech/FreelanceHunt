package tech.hombre.freelancehunt.common.extensions

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.ProjectStatus
import tech.hombre.freelancehunt.common.SafeType

fun getProjectStatus(id: Int): ProjectStatus {
    return ProjectStatus.values().find { it.id == id } ?: ProjectStatus.PROJECT_NOT_COMPLETED
}

@ColorRes
fun getColorByProjectStatus(status: ProjectStatus): Int {
    return when (status) {
        ProjectStatus.OPEN_FOR_PROPOSALS -> R.color.status_project_open
        ProjectStatus.PENDING_PAYMENT_RESERVATION -> R.color.status_project_pending
        ProjectStatus.CONTRACTOR_CHOSEN -> R.color.status_project_open
        ProjectStatus.PROJECT_ONGOING -> R.color.status_project_pending
        ProjectStatus.PROJECT_UNDER_ARBITRAGE -> R.color.status_project_pending
        ProjectStatus.PROJECT_COMPLETE -> R.color.status_project_completed
        ProjectStatus.CLOSED_WITHOUT_COMPLETION -> R.color.status_project_expired
        ProjectStatus.PROJECT_EXPIRED -> R.color.status_project_expired
        ProjectStatus.RULES_VIOLATED -> R.color.status_project_closed_by_moderator
        ProjectStatus.PROJECT_NOT_COMPLETED -> R.color.status_project_open
        ProjectStatus.PROJECT_UNPAYED -> R.color.status_project_pending
        ProjectStatus.CLOSED_WITHOUT_REVIEW -> R.color.status_project_completed
        ProjectStatus.BLOCKED_BY_USERS -> R.color.status_project_closed_by_moderator
        ProjectStatus.CLOSED_BY_MODERATOR -> R.color.status_project_closed_by_moderator
        ProjectStatus.CLOSED_BECAUSE_OF_BUDGET -> R.color.status_project_closed_by_moderator
    }
}

@StringRes
fun getTitleBySafeType(type: SafeType): Int {
    return when (type) {
        SafeType.DIRECT_PAYMENT -> R.string.safe_type_1
        SafeType.EMPLOYER -> R.string.safe_type_2
        SafeType.DEVELOPER -> R.string.safe_type_3
        SafeType.SPLIT -> R.string.safe_type_4
        SafeType.EMPLOYER_CASHLESS -> R.string.safe_type_5
    }
}