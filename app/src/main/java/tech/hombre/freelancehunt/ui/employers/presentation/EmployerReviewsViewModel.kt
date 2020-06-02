package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.interaction.employerslist.reviews.GetEmployerReviewsUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class EmployerReviewsViewModel(
    private val getEmployerReviews: GetEmployerReviewsUseCase,
    private val getProjectDetail: GetProjectDetailUseCase,
    private val getEmployerDetail: GetEmployerDetailUseCase
) :
    BaseViewModel<EmployerReviews>() {

    lateinit var pagination: EmployerReviews.Links

    val _projectDetails = MutableLiveData<ViewState<ProjectDetail>>()
    val projectDetails: LiveData<ViewState<ProjectDetail>>
        get() = _projectDetails

    val _employerDetails = MutableLiveData<ViewState<EmployerDetail>>()
    val employerDetails: LiveData<ViewState<EmployerDetail>>
        get() = _employerDetails

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

    fun getEmployerDetails(profileId: Int) = executeUseCase {
        getEmployerDetail(profileId)
            .onSuccess {
                _employerDetails.value = Success(it)
            }
            .onFailure { _employerDetails.value = Error(it.throwable) }
    }

}
