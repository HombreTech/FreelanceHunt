package tech.hombre.domain.repository

import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.Result

interface CountriesRepository {
    suspend fun getCountries(): Result<Countries>
}