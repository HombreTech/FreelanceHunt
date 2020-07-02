package tech.hombre.domain.interaction.employerslist

import tech.hombre.domain.repository.EmployersListRepository

class GetEmployersListUseCaseImpl(private val freelancersListRepository: EmployersListRepository) :
    GetEmployersListUseCase {

    override suspend operator fun invoke(page: String, countryId: Int, cityId: Int) =
        freelancersListRepository.getEmployersList(page, countryId, cityId)
}