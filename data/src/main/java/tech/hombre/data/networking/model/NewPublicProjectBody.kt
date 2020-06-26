package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName
import tech.hombre.domain.model.MyBidsList

data class NewPublicProjectBody(
    val name: String,
    val budget: MyBidsList.Data.Attributes.Budget,
    @SerializedName("safe_type")
    val safeType: String,
    @SerializedName("description_html")
    val descriptionHtml: String,
    val skills: ArrayList<Int>,
    @SerializedName("expired_at")
    val expiredAt: String,
    val is_only_for_plus: Boolean = false
)