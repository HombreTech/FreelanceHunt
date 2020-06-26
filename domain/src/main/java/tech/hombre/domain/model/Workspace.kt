package tech.hombre.domain.model


data class Workspace(
    val `data`: WorkspaceDetail.Data = WorkspaceDetail.Data(),
    val links: Links = Links()
) {
    data class Links(
        val self: String = "",
        val first: String = "",
        val prev: String = "",
        val next: String = ""
    )
}