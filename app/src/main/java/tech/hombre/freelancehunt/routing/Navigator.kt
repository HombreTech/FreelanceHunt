package tech.hombre.freelancehunt.routing

import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.ProjectDetail

interface Navigator {

    fun showLoginActivity()

    fun showMainActivity(screenType: ScreenType = ScreenType.MAIN, fromNotification: Boolean = false)

    fun showProfileActivity()

    fun showFreelancerDetails(details: FreelancerDetail.Data)

    fun showFreelancerDetails(profileId: Int)

    fun showEmployerDetails(details: EmployerDetail.Data)

    fun showEmployerDetails(profileId: Int)

    fun showProjectDetails(details: ProjectDetail.Data)

    fun showProjectDetails(projectId: Int)

    fun showContestDetails(contestId: Int)

    fun showContestDetails(details: ContestDetail.Data)

    fun showThread(threadId: Int, threadUrl: String)

}