package tech.hombre.domain.interaction.cities

import tech.hombre.domain.model.Cities
import tech.hombre.domain.model.Result

interface GetCitiesUseCase {

    suspend operator fun invoke(countryId: Int): Result<Cities>
}