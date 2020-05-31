package tech.hombre.domain.interaction.myprofile

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyProfile
import tech.hombre.domain.model.Result

interface GetMyProfileUseCase : BaseUseCase<String, MyProfile> {

    override suspend operator fun invoke(param: String): Result<MyProfile>
}