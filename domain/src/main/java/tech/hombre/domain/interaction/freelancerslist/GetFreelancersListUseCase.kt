package tech.hombre.domain.interaction.freelancerslist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.FreelancersList
import tech.hombre.domain.model.Result

interface GetFreelancersListUseCase : BaseUseCase<String, FreelancersList> {

    override suspend operator fun invoke(url: String): Result<FreelancersList>
}