package tech.hombre.data.repository.myprofile

import tech.hombre.data.networking.MyProfileApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData

import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyProfile
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.MyProfileRepository

class MyProfileRepositoryImpl(
    private val myProfileApi: MyProfileApi
) : BaseRepository<MyProfile, DomainMapper<MyProfile>>(),
    MyProfileRepository {

    override suspend fun getMyProfile(): Result<MyProfile> {
        return fetchData(
            dataProvider = { myProfileApi.getMyProfile().getData() }
        )
    }
}