package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ThreadsList

data class ThreadsListResponse(
    val data: List<ThreadsList.Data>,
    val links: ThreadsList.Links
) : DomainMapper<ThreadsList> {

    override fun mapToDomainModel() = ThreadsList(data, links)
}

