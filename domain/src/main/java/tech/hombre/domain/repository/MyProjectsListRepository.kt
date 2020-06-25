package tech.hombre.domain.repository

import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.Result

interface MyProjectsListRepository {
    suspend fun getMyProjectsList(url: String): Result<MyProjectsList>
}