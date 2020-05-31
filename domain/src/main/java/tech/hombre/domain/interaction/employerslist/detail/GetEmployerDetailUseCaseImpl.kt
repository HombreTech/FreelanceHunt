package tech.hombre.domain.interaction.employerslist.detail

import tech.hombre.domain.repository.EmployerDetailRepository

class GetEmployerDetailUseCaseImpl(private val freelancersRepository: EmployerDetailRepository) :
    GetEmployerDetailUseCase {

    override suspend operator fun invoke(profileId: Int) =
        freelancersRepository.getEmployerDetail(profileId)
}