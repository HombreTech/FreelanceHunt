package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName
import tech.hombre.domain.model.MyBidsList


data class CreateThreadBody(
    val subject: String,
    @SerializedName("message_html")
    val messageHtml: String,
    @SerializedName("to_profile_id")
    val toProfileId: Int
)