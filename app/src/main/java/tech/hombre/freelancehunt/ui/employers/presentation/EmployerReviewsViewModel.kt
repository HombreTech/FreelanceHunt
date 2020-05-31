package tech.hombre.freelancehunt.ui.employers.presentation

import tech.hombre.domain.interaction.employerslist.reviews.GetEmployerReviewsUseCase
import tech.hombre.domain.model.EmployerReviews
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class EmployerReviewsViewModel(private val getEmployerReviews: GetEmployerReviewsUseCase) :
    BaseViewModel<EmployerReviews>() {

    lateinit var pagination: EmployerReviews.Links

    fun getEmployerReview(url: String) = executeUseCase {
        getEmployerReviews(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

}
