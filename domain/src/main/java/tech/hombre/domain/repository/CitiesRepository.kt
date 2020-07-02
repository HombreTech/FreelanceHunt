package tech.hombre.domain.repository

import tech.hombre.domain.model.Cities
import tech.hombre.domain.model.Result

interface CitiesRepository {
    suspend fun getCities(countryId: Int): Result<Cities>
}