package tech.hombre.domain.interaction.employerslist.detail

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.Result

interface GetEmployerDetailUseCase : BaseUseCase<Int, EmployerDetail> {

    override suspend operator fun invoke(profileId: Int): Result<EmployerDetail>
}