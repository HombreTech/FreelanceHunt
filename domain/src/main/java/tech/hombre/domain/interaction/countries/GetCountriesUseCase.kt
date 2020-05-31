package tech.hombre.domain.interaction.countries

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.Result

interface GetCountriesUseCase : BaseUseCase<String, Countries> {

    override suspend operator fun invoke(param: String): Result<Countries>
}