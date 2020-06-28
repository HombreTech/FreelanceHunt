package tech.hombre.data.repository.projectslist

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
    override suspend fun getProjectsList(url: String, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int): Result<ProjectsList> {
        return fetchData(
            apiDataProvider = {
                projectsApi.getProjectsList(url, onlyMySkills.toInt(), onlyForPlus.toInt(), skills, employerId).getData(
                    fetchFromCacheAction = { ProjectsListDao.getProjectsList(url) },
                    cacheAction = { ProjectsListDao.saveProjectsList(it) })
            },
            dbDataProvider = { ProjectsListDao.getProjectsList(url) }
        )
    }
}