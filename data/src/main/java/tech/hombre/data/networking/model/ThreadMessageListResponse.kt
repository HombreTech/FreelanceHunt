package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ThreadMessageList

data class ThreadMessageListResponse(
    val data: List<ThreadMessageList.Data>,
    val links: ThreadMessageList.Links,
    val meta: ThreadMessageList.Meta
) : DomainMapper<ThreadMessageList> {

    override fun mapToDomainModel() = ThreadMessageList(data, links, meta)
}

