package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName
import tech.hombre.domain.model.MyBidsList


data class SendSupportMessageBody(
    val subject: String,
    @SerializedName("message_html")
    val message: String
)