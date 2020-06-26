package tech.hombre.data.repository.skills

import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.data.database.model.SkillsEntity
import tech.hombre.data.networking.SkillsApi
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.SkillList
import tech.hombre.domain.repository.SkillsRepository

class SkillsRepositoryImpl(
    private val skillsApi: SkillsApi,
    private val skillsDao: SkillsDao
) : BaseRepository<SkillList, SkillsEntity>(),
    SkillsRepository {
    override suspend fun getSkills(): Result<SkillList> {
        return fetchData(
            apiDataProvider = {
                skillsApi.getSkillsList().getData(
                    fetchFromCacheAction = { skillsDao.getSkills() },
                    cacheAction = { skillsDao.saveSkills(it) })
            },
            dbDataProvider = { skillsDao.getSkills() },
            fromCache = true
        )
    }
}