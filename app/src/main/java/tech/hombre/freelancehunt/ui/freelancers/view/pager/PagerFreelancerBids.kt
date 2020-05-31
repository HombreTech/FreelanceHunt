package tech.hombre.freelancehunt.ui.freelancers.view.pager

import android.os.Bundle
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.ui.base.BaseFragment

class PagerFreelancerBids : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_freelancer_bids

    private var profileId = 0

    override fun viewReady() {
        arguments?.let {
            profileId = it.getInt(EXTRA_1)
            if (profileId != 0) {
                //initOverview(details!!)
            }
        }
    }

    private fun initOverview(details: FreelancerDetail.Data) {

    }

    companion object {
        val TAG = PagerFreelancerBids::class.java.simpleName

        fun newInstance(profileId: Int): PagerFreelancerBids {
            val fragment = PagerFreelancerBids()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}