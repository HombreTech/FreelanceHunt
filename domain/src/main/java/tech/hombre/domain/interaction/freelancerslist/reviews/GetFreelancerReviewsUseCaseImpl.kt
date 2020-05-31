package tech.hombre.domain.interaction.freelancerslist.reviews

import tech.hombre.domain.repository.FreelancerReviewsRepository

class GetFreelancerReviewsUseCaseImpl(private val freelancersRepository: FreelancerReviewsRepository) :
    GetFreelancerReviewsUseCase {

    override suspend operator fun invoke(url: String) =
        freelancersRepository.getFreelancerReviews(url)
}