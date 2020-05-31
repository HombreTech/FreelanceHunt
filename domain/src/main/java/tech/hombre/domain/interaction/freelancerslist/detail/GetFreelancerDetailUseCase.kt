package tech.hombre.domain.interaction.freelancerslist.detail

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.Result

interface GetFreelancerDetailUseCase : BaseUseCase<Int, FreelancerDetail> {

    override suspend operator fun invoke(profileId: Int): Result<FreelancerDetail>
}