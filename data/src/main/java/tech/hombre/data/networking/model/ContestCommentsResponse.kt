package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ContestComment

data class ContestCommentsResponse(
    val data: List<ContestComment.Data>,
    val links: ContestComment.Links
) : DomainMapper<ContestComment> {

    override fun mapToDomainModel() = ContestComment(data, links)
}

