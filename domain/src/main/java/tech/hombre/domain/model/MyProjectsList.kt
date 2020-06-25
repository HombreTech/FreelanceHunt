package tech.hombre.domain.model

data class MyProjectsList(
    val `data`: List<ProjectDetail.Data> = listOf(),
    val links: Links = Links()
) {
    data class Links(
        val self: String = "",
        val first: String = "",
        val prev: String = "",
        val next: String = ""
    )
}