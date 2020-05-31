package tech.hombre.domain.interaction.employerslist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.EmployersList
import tech.hombre.domain.model.Result

interface GetEmployersListUseCase : BaseUseCase<String, EmployersList> {

    override suspend operator fun invoke(url: String): Result<EmployersList>
}