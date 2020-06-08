package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.MyContestsList

data class MyContestsListResponse(
    val data: List<ContestDetail.Data>,
    val links: MyContestsList.Links
) : DomainMapper<MyContestsList> {

    override fun mapToDomainModel() = MyContestsList(data, links)

}

