package tech.hombre.freelancehunt.ui.employers.view.pager

import android.os.Bundle
import androidx.annotation.Keep
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.ui.base.BaseFragment

class PagerEmployerProjects : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_employer_projects

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
        @Keep
        val TAG = PagerEmployerProjects::class.java.simpleName

        fun newInstance(profileId: Int): PagerEmployerProjects {
            val fragment = PagerEmployerProjects()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}