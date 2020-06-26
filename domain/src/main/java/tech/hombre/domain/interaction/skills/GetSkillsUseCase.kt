package tech.hombre.domain.interaction.skills

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.SkillList

interface GetSkillsUseCase : BaseUseCase<String, SkillList> {

    override suspend operator fun invoke(param: String): Result<SkillList>
}