package tech.hombre.domain.interaction.freelancerslist

import tech.hombre.domain.model.FreelancersList
import tech.hombre.domain.model.Result

interface GetFreelancersListUseCase {

    suspend operator fun invoke(page: String, skills: String, countryId: Int, cityId: Int): Result<FreelancersList>
}