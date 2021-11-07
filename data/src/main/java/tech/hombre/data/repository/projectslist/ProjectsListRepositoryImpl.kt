package tech.hombre.data.repository.projectslist

import tech.hombre.data.BuildConfig
import tech.hombre.data.common.extensions.toInt
import tech.hombre.data.database.dao.ProjectsListDao
import tech.hombre.data.database.model.ProjectsListEntity
import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectsListRepository

class ProjectsListRepositoryImpl(
    private val projectsApi: ProjectsApi,
    private val ProjectsListDao: ProjectsListDao
) : BaseRepository<ProjectsList, ProjectsListEntity>(),
    ProjectsListRepository {
    override suspend fun getProjectsList(page: Int, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int?): Result<ProjectsList> {
        val url = BuildConfig.BASE_URL + "projects?page[number]=$page&filter[only_my_skills]=${onlyMySkills.toInt()}&filter[only_for_plus]=${onlyForPlus.toInt()}&filter[skill_id]=$skills&filter[employer_id]=$employerId"
        return fetchData(
            apiDataProvider = {
                projectsApi.getProjectsList(page, onlyMySkills.toInt(), onlyForPlus.toInt(), skills, employerId).getData(
                    fetchFromCacheAction = { ProjectsListDao.getProjectsList(url) },
                    cacheAction = { ProjectsListDao.saveProjectsList(it) })
            },
            dbDataProvider = { ProjectsListDao.getProjectsList(url) }
        )
    }

    override suspend fun getSimpleProjectsList(page: Int, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int?): Result<ProjectsList> {
        return fetchData(
            dataProvider = { projectsApi.getSimpleProjectsList(page, onlyMySkills.toInt(), onlyForPlus.toInt(), skills, employerId).getData() }
        )
    }
}