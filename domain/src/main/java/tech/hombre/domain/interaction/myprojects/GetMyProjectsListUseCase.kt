package tech.hombre.domain.interaction.myprojects

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.Result

interface GetMyProjectsListUseCase : BaseUseCase<String, MyProjectsList> {

    override suspend operator fun invoke(url: String): Result<MyProjectsList>
}