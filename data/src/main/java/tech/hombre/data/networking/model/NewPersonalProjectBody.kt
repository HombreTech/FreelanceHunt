package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName
import tech.hombre.domain.model.MyBidsList


data class NewPersonalProjectBody(
    val name: String,
    @SerializedName("freelancer_id")
    val freelancerId: Int,
    val is_personal: Boolean,
    val budget: MyBidsList.Data.Attributes.Budget,
    @SerializedName("safe_type")
    val safeType: String,
    @SerializedName("description_html")
    val descriptionHtml: String,
    val skills: ArrayList<Int>,
    @SerializedName("expired_at")
    val expiredAt: String
)