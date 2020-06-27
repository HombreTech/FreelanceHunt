package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName


data class ExtendProjectBody(
    @SerializedName("expired_at")
    val expiredAt: String
)