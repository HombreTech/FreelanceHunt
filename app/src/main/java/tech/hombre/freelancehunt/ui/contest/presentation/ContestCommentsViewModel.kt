package tech.hombre.freelancehunt.ui.contest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.contests.comments.GetContestCommentsUseCase
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class ContestCommentsViewModel(
    private val getContestComments: GetContestCommentsUseCase,
    private val getFreelancerDetail: GetFreelancerDetailUseCase,
    private val getEmployerDetail: GetEmployerDetailUseCase
) :
    BaseViewModel<ContestComment>() {

    val _employerDetails = MutableLiveData<ViewState<EmployerDetail>>()
    val employerDetails: LiveData<ViewState<EmployerDetail>>
        get() = _employerDetails

    val _freelancerDetails = MutableLiveData<ViewState<FreelancerDetail>>()
    val freelancerDetails: LiveData<ViewState<FreelancerDetail>>
        get() = _freelancerDetails

    fun getContestComment(contestId: Int) = executeUseCase {
        getContestComments(contestId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getEmployerDetails(profileId: Int) = executeUseCase {
        getEmployerDetail(profileId)
            .onSuccess {
                _employerDetails.value = Success(it)
            }
            .onFailure { _employerDetails.value = Error(it.throwable) }
    }

    fun getFreelancerDetails(profileId: Int) = executeUseCase {
        getFreelancerDetail(profileId)
            .onSuccess {
                _freelancerDetails.value = Success(it)
            }
            .onFailure { _freelancerDetails.value = Error(it.throwable) }
    }
}
