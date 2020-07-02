package tech.hombre.domain.repository

import tech.hombre.domain.model.EmployersList
import tech.hombre.domain.model.Result

interface EmployersListRepository {
    suspend fun getEmployersList(page: String, countryId: Int, cityId: Int): Result<EmployersList>
}