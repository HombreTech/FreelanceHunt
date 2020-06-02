package tech.hombre.freelancehunt.ui.freelancers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.interaction.freelancerslist.reviews.GetFreelancerReviewsUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class FreelancerReviewsViewModel(
    private val getFreelancerReviews: GetFreelancerReviewsUseCase,
    private val getProjectDetail: GetProjectDetailUseCase,
    private val getEmployerDetail: GetEmployerDetailUseCase
) :
    BaseViewModel<FreelancerReviews>() {

    lateinit var pagination: FreelancerReviews.Links

    val _projectDetails = MutableLiveData<ViewState<ProjectDetail>>()
    val projectDetails: LiveData<ViewState<ProjectDetail>>
        get() = _projectDetails

    val _employerDetails = MutableLiveData<ViewState<EmployerDetail>>()
    val employerDetails: LiveData<ViewState<EmployerDetail>>
        get() = _employerDetails

    fun getFreelancerReview(url: String) = executeUseCase {
        getFreelancerReviews(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getProjectDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess { _projectDetails.value = Success(it) }
            .onFailure { _projectDetails.value = Error(it.throwable) }
    }

    fun getEmployerDetails(profileId: Int) = executeUseCase {
        getEmployerDetail(profileId)
            .onSuccess {
                _employerDetails.value = Success(it)
            }
            .onFailure { _employerDetails.value = Error(it.throwable) }
    }

}
