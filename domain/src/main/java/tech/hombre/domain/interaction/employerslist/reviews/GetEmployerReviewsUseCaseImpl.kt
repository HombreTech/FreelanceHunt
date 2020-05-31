package tech.hombre.domain.interaction.employerslist.reviews

import tech.hombre.domain.repository.EmployerReviewsRepository

class GetEmployerReviewsUseCaseImpl(private val employersRepository: EmployerReviewsRepository) :
    GetEmployerReviewsUseCase {

    override suspend operator fun invoke(url: String) = employersRepository.getEmployerReviews(url)
}