package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.employerslist.reviews.GetEmployerReviewsUseCase
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class EmployerReviewsViewModel(
    private val getEmployerReviews: GetEmployerReviewsUseCase,
    private val getProjectDetail: GetProjectDetailUseCase,
    private val getFreelancerDetail: GetFreelancerDetailUseCase
) :
    BaseViewModel<EmployerReviews>() {

    lateinit var pagination: EmployerReviews.Links

    val _projectDetails = MutableLiveData<ViewState<ProjectDetail>>()
    val projectDetails: LiveData<ViewState<ProjectDetail>>
        get() = _projectDetails

    val _freelancerDetails = MutableLiveData<ViewState<FreelancerDetail>>()
    val freelancerDetails: LiveData<ViewState<FreelancerDetail>>
        get() = _freelancerDetails

    fun getEmployerReview(url: String) = executeUseCase {
        getEmployerReviews(url)
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

    fun getFreelancerDetails(profileId: Int) = executeUseCase {
        getFreelancerDetail(profileId)
            .onSuccess {
                _freelancerDetails.value = Success(it)
            }
            .onFailure { _freelancerDetails.value = Error(it.throwable) }
    }

}
