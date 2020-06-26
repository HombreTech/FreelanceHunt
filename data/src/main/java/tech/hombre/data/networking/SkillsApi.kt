package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import tech.hombre.data.networking.model.SkillsResponse

interface SkillsApi {

    @GET("skills")
    suspend fun getSkillsList(): Response<SkillsResponse>

}