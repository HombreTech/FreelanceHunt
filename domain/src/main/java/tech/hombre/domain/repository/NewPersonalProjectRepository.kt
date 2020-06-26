package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface NewPersonalProjectRepository {
    suspend fun newProject(name: String,
                           freelancerId: Int,
                           is_personal: Boolean,
                           budget: MyBidsList.Data.Attributes.Budget,
                           safeType: String,
                           descriptionHtml: String,
                           skills: ArrayList<Int>,
                           expired_at: String): Result<ProjectDetail>
}