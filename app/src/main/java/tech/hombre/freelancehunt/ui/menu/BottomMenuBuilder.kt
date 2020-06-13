package tech.hombre.freelancehunt.ui.menu

import android.content.Context
import androidx.fragment.app.FragmentManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.ui.menu.model.MenuItem


class BottomMenuBuilder(val fm: FragmentManager, val tag: String) : KoinComponent {

    val context: Context by inject()

    fun buildMenuForAddBid(isPlus: Boolean, id: Int) =
        AddBidBottomDialogFragment.newInstance(isPlus, id).show(fm, tag)

    fun buildMenuForFreelancerBid(projectId: Int, bidId: Int, isRevoked: Boolean) {
        val items = arrayListOf<MenuItem>()
        if (!isRevoked)
            items.add(MenuItem(context.getString(R.string.revoke), "revoke", R.drawable.project))
        //else items.add(MenuItem(context.getString(R.string.restore), "restore", R.drawable.project))
        ListMenuBottomDialogFragment.newInstance(
            projectId,
            bidId,
            items
        ).show(fm, tag)
    }

    fun buildMenuForEmployerBid(projectId: Int, bidId: Int, isRejected: Boolean) {
        val items = arrayListOf<MenuItem>()
        if (!isRejected) items.add(
            MenuItem(
                context.getString(R.string.reject),
                "reject",
                R.drawable.project
            )
        ) /*else items.add(
            MenuItem(
                context.getString(R.string.restore),
                "restore",
                R.drawable.project
            )
        )*/

        items.add(MenuItem(context.getString(R.string.choose), "choose", R.drawable.project))
        ListMenuBottomDialogFragment.newInstance(
            projectId,
            bidId,
            items
        ).show(fm, tag)
    }

}
