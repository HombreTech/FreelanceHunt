package tech.hombre.domain.model

data class ApiError(
    val error: Error = Error()
) {
    data class Error(
        val status: Int = 0,
        val title: String = "",
        val detail: String? = ""
    )
}