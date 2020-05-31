package tech.hombre.domain.repository

import tech.hombre.domain.model.FreelancersList
import tech.hombre.domain.model.Result

interface FreelancersRepository {
    suspend fun getFreelancersList(url: String): Result<FreelancersList>
}