package tech.hombre.freelancehunt.routing

import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.ProjectDetail

interface Navigator {

    fun showLoginActivity()

    fun showMainActivity()

    fun showProfileActivity()

    fun showFreelancerDetails(details: FreelancerDetail.Data)

    fun showFreelancerDetails(profileId: Int)

    fun showEmployerDetails(details: EmployerDetail.Data)

    fun showEmployerDetails(profileId: Int)

    fun showProjectDetails(details: ProjectDetail.Data)

    fun showContestDetails(contestId: Int)

}