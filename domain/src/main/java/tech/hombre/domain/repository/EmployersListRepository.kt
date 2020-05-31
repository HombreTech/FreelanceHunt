package tech.hombre.domain.repository

import tech.hombre.domain.model.EmployersList
import tech.hombre.domain.model.Result

interface EmployersListRepository {
    suspend fun getEmployersList(url: String): Result<EmployersList>
}