package tech.hombre.domain.repository

import tech.hombre.domain.model.MyProfile
import tech.hombre.domain.model.Result

interface MyProfileRepository {
    suspend fun getMyProfile(): Result<MyProfile>
}