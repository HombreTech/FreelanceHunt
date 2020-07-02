package tech.hombre.domain.interaction.freelancerslist

import tech.hombre.domain.repository.FreelancersRepository

class GetFreelancersListUseCaseImpl(private val freelancersRepository: FreelancersRepository) :
    GetFreelancersListUseCase {

    override suspend operator fun invoke(page: String, skills: String, countryId: Int, cityId: Int) =
        freelancersRepository.getFreelancersList(page, skills, countryId, cityId)
}