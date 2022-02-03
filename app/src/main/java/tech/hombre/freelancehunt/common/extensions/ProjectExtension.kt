package tech.hombre.freelancehunt.common.extensions

import android.content.Context
import androidx.annotation.ColorRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.BidStatus
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


fun getTitleBySafeType(context: Context, type: SafeType): String {
    return context.getString(when (type) {
        SafeType.EMPLOYER -> R.string.safe_type_2
        SafeType.DEVELOPER -> R.string.safe_type_3
    })
}

fun getBidStatus(status: String): BidStatus {
    return BidStatus.values().find { it.status == status } ?: BidStatus.ACTIVE
}

fun getTitleByBidStatus(context: Context, status: BidStatus): String {
    return context.getString(when (status) {
        BidStatus.ACTIVE -> R.string.bid_active
        BidStatus.REVOKED -> R.string.bid_revoked
        BidStatus.REJECTED -> R.string.bid_rejected
    })
}

@ColorRes
fun getColorByFreelancerStatus(status: BidStatus): Int {
    return when (status) {
        BidStatus.ACTIVE -> R.color.status_bid_active
        BidStatus.REVOKED -> R.color.status_bid_revoked
        BidStatus.REJECTED -> R.color.status_bid_rejected
    }
}