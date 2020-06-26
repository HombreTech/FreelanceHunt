package tech.hombre.domain.interaction.skills

import tech.hombre.domain.repository.SkillsRepository

class GetSkillsUseCaseImpl(private val skillsRepository: SkillsRepository) :
    GetSkillsUseCase {

    override suspend operator fun invoke(param: String) = skillsRepository.getSkills()
}