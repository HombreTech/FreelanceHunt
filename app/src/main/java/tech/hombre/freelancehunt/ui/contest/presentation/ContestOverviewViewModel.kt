package tech.hombre.freelancehunt.ui.contest.presentation

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

class ContestOverviewViewModel(
    private val getEmployerDetail: GetEmployerDetailUseCase
) :
    BaseViewModel<EmployerDetail>() {

    val _employerDetails = MutableLiveData<ViewState<EmployerDetail>>()
    val employerDetails: LiveData<ViewState<EmployerDetail>>
        get() = _employerDetails

    fun getEmployerDetails(profileId: Int) = executeUseCase {
        getEmployerDetail(profileId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

}
