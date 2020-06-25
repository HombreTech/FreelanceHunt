package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.*

interface WorkspacesApi {

    @GET
    suspend fun getMyWorkspacesList(@Url url: String): Response<MyWorkspacesListResponse>
}