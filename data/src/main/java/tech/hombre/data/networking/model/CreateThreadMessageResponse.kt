package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.ThreadsList

data class CreateThreadMessageResponse(
    val data: ThreadsList.Data
) : DomainMapper<ThreadsList.Data> {

    override fun mapToDomainModel() = data
}

