package tech.hombre.freelancehunt.ui.contest.presentation

import tech.hombre.domain.interaction.contests.comments.GetContestCommentsUseCase
import tech.hombre.domain.model.ContestComment
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ContestCommentsViewModel(private val getContestComments: GetContestCommentsUseCase) :
    BaseViewModel<ContestComment>() {

    fun getContestComment(contestId: Int) = executeUseCase {
        getContestComments(contestId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}
