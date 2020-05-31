package tech.hombre.domain.repository

import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.Result

interface FreelancerDetailRepository {

    suspend fun getFreelancerDetail(profileId: Int): Result<FreelancerDetail>
}