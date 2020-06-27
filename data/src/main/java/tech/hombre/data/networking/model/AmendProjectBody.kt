package tech.hombre.data.networking.model

import com.google.gson.annotations.SerializedName
import tech.hombre.domain.model.MyBidsList

data class AmendProjectBody(
    val budget: MyBidsList.Data.Attributes.Budget,
    @SerializedName("update_html")
    val updateHtml: String,
    val skills: ArrayList<Int>
)