package tech.hombre.domain.repository

import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.Result

interface EmployerDetailRepository {
    suspend fun getEmployerDetail(profileId: Int): Result<EmployerDetail>
}