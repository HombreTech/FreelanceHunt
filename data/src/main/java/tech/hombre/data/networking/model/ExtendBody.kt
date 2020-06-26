package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName

data class ExtendBody(
    @SerializedName("days")
    val days: Int
)