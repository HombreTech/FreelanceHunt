package tech.hombre.domain.model

import com.google.gson.annotations.SerializedName

data class ApiError(
    val error: Error = Error()
) {

    data class Error(
        val detail: String? = "",
        val meta: Meta? = Meta(),
        val status: Int = 0,
        val title: String = ""
    )

    data class Meta(
        val info: Info = Info()
    )

    data class Info(
        @SerializedName("budget.amount")
        val amount: List<String> = listOf(),
        val comment: List<String> = listOf()
    )
}