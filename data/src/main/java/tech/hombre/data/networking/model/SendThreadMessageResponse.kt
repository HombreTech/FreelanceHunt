package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ThreadMessageList

data class SendThreadMessageResponse(
    val data: ThreadMessageList.Data
) : DomainMapper<ThreadMessageList.Data> {

    override fun mapToDomainModel() = data
}

