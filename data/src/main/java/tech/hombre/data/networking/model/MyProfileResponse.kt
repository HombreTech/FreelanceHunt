package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.MyProfile

data class MyProfileResponse(
    val data: MyProfile.Data,
    val links: MyProfile.Links
) : DomainMapper<MyProfile> {

    override fun mapToDomainModel() = MyProfile(data, links)
}

