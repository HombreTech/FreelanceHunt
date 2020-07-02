package tech.hombre.domain.model

data class Cities(
    val `data`: List<Data> = listOf(),
    val links: Links = Links()
) {
    data class Data(
        val id: Int = 0,
        val name: String = ""
    )

    data class Links(
        val self: String = ""
    )
}