package tech.hombre.freelancehunt.routing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.EXTRA_3
import tech.hombre.freelancehunt.ui.base.BaseActivity
import tech.hombre.freelancehunt.ui.contest.view.ContestDetailActivity
import tech.hombre.freelancehunt.ui.employers.view.EmployerDetailActivity
import tech.hombre.freelancehunt.ui.freelancers.view.FreelancerDetailActivity
import tech.hombre.freelancehunt.ui.login.view.LoginActivity
import tech.hombre.freelancehunt.ui.main.view.activities.MainActivity
import tech.hombre.freelancehunt.ui.project.view.NewProjectActivity
import tech.hombre.freelancehunt.ui.project.view.ProjectDetailActivity
import tech.hombre.freelancehunt.ui.project.view.UpdateProjectActivity
import tech.hombre.freelancehunt.ui.threads.view.ThreadMessagesActivity
import java.io.Serializable

class AppNavigator(private val activity: AppCompatActivity) : Navigator {

    companion object {
        const val SCREEN_TYPE = "screen_type"
        const val FROM_NOTIFICATION = "from_notification"
    }

    override fun showMainActivity(screenType: ScreenType, fromNotification: Boolean) =
        navigateTo(getIntent<MainActivity>().apply {
            putExtra(SCREEN_TYPE, screenType)
            putExtra(FROM_NOTIFICATION, fromNotification)
        })

    override fun showLoginActivity() = navigateTo(getIntent<LoginActivity>().apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    })

    override fun showProfileActivity() = navigateTo(getIntent<MainActivity>())

    override fun showFreelancerDetails(details: FreelancerDetail.Data) =
        navigateTo(getIntent<FreelancerDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showFreelancerDetails(profileId: Int) =
        navigateTo(getIntent<FreelancerDetailActivity>().apply {
            putExtra(EXTRA_1, profileId)
            putExtra(EXTRA_2, true)
        })

    override fun showEmployerDetails(details: EmployerDetail.Data) =
        navigateTo(getIntent<EmployerDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showEmployerDetails(profileId: Int) =
        navigateTo(getIntent<EmployerDetailActivity>().apply {
            putExtra(EXTRA_1, profileId)
            putExtra(EXTRA_2, true)
        })

    override fun showProjectDetails(details: ProjectDetail.Data) =
        navigateTo(getIntent<ProjectDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showProjectDetails(projectId: Int) =
        navigateTo(getIntent<ProjectDetailActivity>().apply {
            putExtra(EXTRA_1, projectId)
            putExtra(EXTRA_2, true)
        })

    override fun showContestDetails(details: ContestDetail.Data) =
        navigateTo(getIntent<ContestDetailActivity>().apply {
            putExtra(EXTRA_1, details)
        })

    override fun showContestDetails(contestId: Int) =
        navigateTo(getIntent<ContestDetailActivity>().apply {
            putExtra(EXTRA_1, contestId)
            putExtra(EXTRA_2, true)
        })

    override fun showThread(threadId: Int, threadUrl: String) =
        navigateTo(getIntent<ThreadMessagesActivity>().apply {
            putExtra(EXTRA_1, threadId)
            putExtra(EXTRA_2, threadUrl)
        })

    override fun showNewProjectDialog(freelancerId: Int, isPersonal: Boolean, freelancerLogin: String) =
        navigateTo(getIntent<NewProjectActivity>().apply {
            putExtra(EXTRA_1, isPersonal)
            putExtra(EXTRA_2, freelancerId)
            putExtra(EXTRA_3, freelancerLogin)
        })


    override fun showUpdateProjectDialog(
        projectId: Int,
        isAmend: Boolean,
        projectDetails: ProjectDetail.Data?
    ) =
        navigateTo(getIntent<UpdateProjectActivity>().apply {
            putExtra(EXTRA_1, projectId)
            putExtra(EXTRA_2, isAmend)
            putExtra(EXTRA_3, projectDetails)
        })

    private fun navigateTo(intent: Intent) = activity.startActivity(intent)

    private inline fun <reified T : BaseActivity> getIntent() = Intent(activity, T::class.java)
}

enum class ScreenType : Serializable {
    MAIN,
    THREADS
}
