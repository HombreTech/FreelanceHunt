package tech.hombre.data.repository.myprojectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.MyProjectsListRepository

class MyProjectsListRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<MyProjectsList, DomainMapper<MyProjectsList>>(),
    MyProjectsListRepository {
    override suspend fun getMyProjectsList(url: String): Result<MyProjectsList> {
        return fetchData(
            dataProvider = { projectsApi.getMyProjectsList(url).getData() }
        )
    }
}