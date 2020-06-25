package tech.hombre.domain.model


data class WorkspacesList(
    val `data`: List<WorkspaceDetail.Data> = listOf(),
    val links: Links = Links()
) {
    data class Links(
        val self: String = "",
        val first: String = "",
        val prev: String = "",
        val next: String = ""
    )
}