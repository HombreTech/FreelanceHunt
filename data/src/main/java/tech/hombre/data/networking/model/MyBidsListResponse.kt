package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.MyBidsList

data class MyBidsListResponse(
    val data: List<MyBidsList.Data>,
    val links: MyBidsList.Links
) : DomainMapper<MyBidsList> {

    override fun mapToDomainModel() = MyBidsList(data, links)
}

