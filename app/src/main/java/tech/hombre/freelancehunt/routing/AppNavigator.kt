package tech.hombre.freelancehunt.routing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.ui.base.BaseActivity
import tech.hombre.freelancehunt.ui.contest.view.ContestDetailActivity
import tech.hombre.freelancehunt.ui.employers.view.EmployerDetailActivity
import tech.hombre.freelancehunt.ui.freelancers.view.FreelancerDetailActivity
import tech.hombre.freelancehunt.ui.login.view.LoginActivity
import tech.hombre.freelancehunt.ui.main.view.activities.MainActivity
import tech.hombre.freelancehunt.ui.project.view.ProjectDetailActivity
import java.io.Serializable

class AppNavigator(private val activity: AppCompatActivity) : Navigator {

    companion object {
        const val SCREEN_TYPE = "screen_type"
    }

    override fun showMainActivity() = navigateTo(getIntent<MainActivity>().apply {
        putExtra(SCREEN_TYPE, ScreenType.MAIN)
    })

    override fun showLoginActivity() = navigateTo(getIntent<LoginActivity>().apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    })

    override fun showProfileActivity() = navigateTo(getIntent<MainActivity>())

    override fun showFreelancerDetails(details: FreelancerDetail.Data) =
        navigateTo(getIntent<FreelancerDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showEmployerDetails(details: EmployerDetail.Data) =
        navigateTo(getIntent<EmployerDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showProjectDetails(details: ProjectDetail.Data) =
        navigateTo(getIntent<ProjectDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showContestDetails(contestId: Int) =
        navigateTo(getIntent<ContestDetailActivity>().apply {
            putExtra(EXTRA_1, contestId)
        })

    private fun navigateTo(intent: Intent) = activity.startActivity(intent)

    private inline fun <reified T : BaseActivity> getIntent() = Intent(activity, T::class.java)
}

enum class ScreenType : Serializable {
    MAIN,
    PROFILE
}
