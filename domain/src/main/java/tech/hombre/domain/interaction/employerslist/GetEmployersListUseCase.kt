package tech.hombre.domain.interaction.employerslist

import tech.hombre.domain.model.EmployersList
import tech.hombre.domain.model.Result

interface GetEmployersListUseCase {

    suspend operator fun invoke(page: String, countryId: Int, cityId: Int): Result<EmployersList>
}