package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.EmployersList

data class EmployersListResponse(
    val data: List<EmployerDetail.Data>,
    val links: EmployersList.Links
) : DomainMapper<EmployersList> {

    override fun mapToDomainModel() = EmployersList(data, links)
}

