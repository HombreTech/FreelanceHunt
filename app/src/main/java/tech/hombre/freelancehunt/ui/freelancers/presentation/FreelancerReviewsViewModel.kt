package tech.hombre.freelancehunt.ui.freelancers.presentation

import tech.hombre.domain.interaction.freelancerslist.reviews.GetFreelancerReviewsUseCase
import tech.hombre.domain.model.FreelancerReviews
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class FreelancerReviewsViewModel(private val getFreelancerReviews: GetFreelancerReviewsUseCase) :
    BaseViewModel<FreelancerReviews>() {

    lateinit var pagination: FreelancerReviews.Links

    fun getFreelancerReview(url: String) = executeUseCase {
        getFreelancerReviews(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

}
