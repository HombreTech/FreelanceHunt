package tech.hombre.freelancehunt.ui.menu

import android.content.Context
import androidx.fragment.app.FragmentManager
import org.koin.core.KoinComponent
import tech.hombre.domain.model.WorkspaceDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.ProjectStatus
import tech.hombre.freelancehunt.common.SIMPLE_DIALOG_CLOSE_WORKSPACE
import tech.hombre.freelancehunt.common.SIMPLE_DIALOG_EXTEND_WORKSPACE
import tech.hombre.freelancehunt.common.SIMPLE_DIALOG_REQUEST_ARBITRAGE
import tech.hombre.freelancehunt.common.extensions.toast
import tech.hombre.freelancehunt.ui.menu.model.MenuItem


class BottomMenuBuilder(val context: Context, val fm: FragmentManager, val tag: String) :
    KoinComponent {

    fun buildMenuForAddBid(isPlus: Boolean, id: Int) =
        AddBidBottomDialogFragment.newInstance(isPlus, id).show(fm, tag)

    fun buildMenuForChooseBid(projectId: Int, bidId: Int) =
        ChooseBidBottomDialogFragment.newInstance(projectId, bidId).show(fm, tag)

    fun buildMenuForCreateThread(profileId: Int) =
        CreateThreadBottomDialogFragment.newInstance(profileId).show(fm, tag)

    fun buildMenuForProposeConditions(
        workspaceId: Int,
        budget: WorkspaceDetail.Data.Attributes.Conditions.Budget,
        safe: String,
        days: Int
    ) =
        ProposeConditionsBottomDialogFragment.newInstance(workspaceId, budget, safe, days)
            .show(fm, tag)

    fun buildMenuForProjectFilter(
        onlyMySkills: Boolean,
        skills: IntArray,
        onlyForPlus: Boolean
    ) =
        ProjectFilterBottomDialogFragment.newInstance(onlyMySkills, skills, onlyForPlus)
            .show(fm, tag)

    fun buildMenuForFreelancersFilter(
        skills: IntArray,
        countryId: Int,
        cityId: Int
    ) =
        FreelancersFilterBottomDialogFragment.newInstance(skills, countryId, cityId)
            .show(fm, tag)

    fun buildMenuForExtendWorkspace(workspaceId: Int) =
        SimpleInputBottomDialogFragment.newInstance(
            SIMPLE_DIALOG_EXTEND_WORKSPACE,
            workspaceId,
            context.getString(R.string.extend_project),
            ""
        ).show(fm, tag)

    fun buildMenuForRequestArbitrage(workspaceId: Int) =
        SimpleInputBottomDialogFragment.newInstance(
            SIMPLE_DIALOG_REQUEST_ARBITRAGE,
            workspaceId,
            context.getString(R.string.request_arbitrage),
            context.getString(R.string.request_arbitrage_comment_hint)
        ).show(fm, tag)

    fun buildMenuForCompleteWorkspace(workspaceId: Int) =
        CompleteWorkspaceBottomDialogFragment.newInstance(workspaceId, true).show(fm, tag)

    fun buildMenuForIncompleteWorkspace(workspaceId: Int) =
        CompleteWorkspaceBottomDialogFragment.newInstance(workspaceId, false).show(fm, tag)

    fun buildMenuForCloseWorkspace(workspaceId: Int) =
        SimpleInputBottomDialogFragment.newInstance(
            SIMPLE_DIALOG_CLOSE_WORKSPACE,
            workspaceId,
            context.getString(R.string.close_project_without_review),
            context.getString(R.string.close_project_comment_hint)
        ).show(fm, tag)

    fun buildMenuForReviewWorkspace(workspaceId: Int) =
        ReviewWorkspaceBottomDialogFragment.newInstance(workspaceId).show(fm, tag)

    fun buildMenuForFreelancerBid(projectId: Int, bidId: Int, isRevoked: Boolean) {
        val items = arrayListOf<MenuItem>()
        if (!isRevoked)
            items.add(MenuItem(context.getString(R.string.revoke), "revoke", R.drawable.remove))
        //else items.add(MenuItem(context.getString(R.string.restore), "restore", R.drawable.project))
        items.add(
            MenuItem(
                context.getString(R.string.project_view),
                "view",
                R.drawable.overview_project
            )
        )
        if (items.isNotEmpty()) {
            ListMenuBottomDialogFragment.newInstance(
                projectId,
                bidId,
                items
            ).show(fm, tag)
        } else toast(context.getString(R.string.not_have_action))
    }

    fun buildMenuForEmployerBid(projectId: Int, bidId: Int, isRejected: Boolean) {
        val items = arrayListOf<MenuItem>()
        if (!isRejected) items.add(
            MenuItem(
                context.getString(R.string.reject),
                "reject",
                R.drawable.remove
            )
        ) /*else items.add(
            MenuItem(
                context.getString(R.string.restore),
                "restore",
                R.drawable.project
            )
        )*/

        items.add(MenuItem(context.getString(R.string.choose), "choose", R.drawable.trophy))
        ListMenuBottomDialogFragment.newInstance(
            projectId,
            bidId,
            items
        ).show(fm, tag)
    }

    fun buildMenuForWorkspace(
        workspaceId: Int,
        isConfirmed: Boolean,
        projectStatus: ProjectStatus,
        isDirectPay: Boolean,
        isEmployer: Boolean
    ) {
        val items = arrayListOf<MenuItem>()
        if (!isConfirmed) {
            if (projectStatus == ProjectStatus.CONTRACTOR_CHOSEN) items.add(
                MenuItem(
                    context.getString(R.string.confirm),
                    "accept",
                    R.drawable.handshake
                )
            )
            if (projectStatus == ProjectStatus.CONTRACTOR_CHOSEN || projectStatus == ProjectStatus.PENDING_PAYMENT_RESERVATION) items.add(
                MenuItem(
                    context.getString(R.string.reject_conditions),
                    "reject",
                    R.drawable.remove
                )
            )
            if (projectStatus == ProjectStatus.CONTRACTOR_CHOSEN) items.add(
                MenuItem(
                    context.getString(R.string.propose_conditions),
                    "propose",
                    R.drawable.briefcase_edit
                )
            )
        } else {
            if (isEmployer && (projectStatus == ProjectStatus.OPEN_FOR_PROPOSALS || projectStatus == ProjectStatus.PROJECT_ONGOING) && projectStatus != ProjectStatus.PENDING_PAYMENT_RESERVATION) items.add(
                MenuItem(
                    context.getString(R.string.extend_project),
                    "extend",
                    R.drawable.time
                )
            )

            if ((projectStatus == ProjectStatus.PROJECT_ONGOING || projectStatus == ProjectStatus.PROJECT_NOT_COMPLETED) && !isDirectPay) items.add(
                MenuItem(
                    context.getString(R.string.request_arbitrage),
                    "arbitrage",
                    R.drawable.arbitrage
                )
            )
        }

        if (isEmployer && projectStatus == ProjectStatus.PROJECT_ONGOING) {
            items.add(
                MenuItem(
                    context.getString(R.string.close_project_completed),
                    "complete",
                    R.drawable.check
                )
            )
            items.add(
                MenuItem(
                    context.getString(R.string.close_project_incompleted),
                    "incomplete",
                    R.drawable.remove
                )
            )
            items.add(
                MenuItem(
                    context.getString(R.string.close_project_without_review),
                    "close_without_review",
                    R.drawable.remove
                )
            )

        } else if (!isEmployer && projectStatus == ProjectStatus.PROJECT_COMPLETE)
            items.add(
                MenuItem(
                    context.getString(R.string.project_write_review),
                    "write_review",
                    R.drawable.reviews
                )
            )

        items.add(
            MenuItem(
                context.getString(R.string.project_view),
                "view",
                R.drawable.overview_project
            )
        )

        if (items.isNotEmpty())
            ListMenuBottomDialogFragment.newInstance(
                workspaceId,
                0,
                items
            ).show(fm, tag) else toast(context.getString(R.string.not_have_action))
    }

    fun buildMenuForProject(
        projectId: Int,
        hasBids: Boolean,
        status: ProjectStatus
    ) {
        val items = arrayListOf<MenuItem>()
        if (!hasBids)
            items.add(
                MenuItem(
                    context.getString(R.string.update),
                    "update",
                    R.drawable.edit
                )
            )
        else if (status == ProjectStatus.OPEN_FOR_PROPOSALS)
            items.add(
                MenuItem(
                    context.getString(R.string.update),
                    "amend",
                    R.drawable.edit
                )
            )

        items.add(MenuItem(context.getString(R.string.extend_project), "extend", R.drawable.time))

        if (status == ProjectStatus.OPEN_FOR_PROPOSALS)
            items.add(
                MenuItem(
                    context.getString(R.string.close_project),
                    "close",
                    R.drawable.pause
                )
            ) else if (status == ProjectStatus.CLOSED_WITHOUT_COMPLETION)
            items.add(
                MenuItem(
                    context.getString(R.string.reopen_project),
                    "reopen",
                    R.drawable.resume
                )
            )

        items.add(
            MenuItem(
                context.getString(R.string.project_view),
                "view",
                R.drawable.overview_project
            )
        )

        if (items.isNotEmpty()) {
            ListMenuBottomDialogFragment.newInstance(
                projectId,
                0,
                items
            ).show(fm, tag)
        } else toast(context.getString(R.string.not_have_action))

    }
}
