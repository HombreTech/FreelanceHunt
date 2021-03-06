package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface NewProjectRepository {
    suspend fun newProject(name: String,
                           budget: MyBidsList.Data.Attributes.Budget,
                           safeType: String,
                           descriptionHtml: String,
                           skills: ArrayList<Int>,
                           expired_at: String,
                           isForPlus: Boolean): Result<ProjectDetail>
}