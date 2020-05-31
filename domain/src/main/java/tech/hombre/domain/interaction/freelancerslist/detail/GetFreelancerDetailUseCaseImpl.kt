package tech.hombre.domain.interaction.freelancerslist.detail

import tech.hombre.domain.repository.FreelancerDetailRepository

class GetFreelancerDetailUseCaseImpl(private val freelancersRepository: FreelancerDetailRepository) :
    GetFreelancerDetailUseCase {

    override suspend operator fun invoke(profileId: Int) =
        freelancersRepository.getFreelancerDetail(profileId)
}