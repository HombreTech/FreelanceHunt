package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName

data class RequestArbitrageBody(
    @SerializedName("comment_html")
    val comment: String
)