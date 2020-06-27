package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.google.gson.annotations.SerializedName

data class MyBidsList(
    val `data`: List<Data> = listOf(),
    val links: Links = Links()
) {
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes()
    ) : ViewModel {
        data class Attributes(
            val days: Int = 0,
            val safe_type: String? = "",
            val budget: Budget = Budget(),
            val comment: String = "",
            var status: String = "",
            val is_hidden: Boolean = false,
            val is_winner: Boolean = false,
            val freelancer: Freelancer = Freelancer(),
            val project: Project = Project(),
            val attachment: Any? = null,
            val published_at: String = ""
        ) {
            data class Budget(
                @SerializedName("amount")
                val amount: Int = 0,
                @SerializedName("currency")
                val currency: String = ""
            )

            data class Freelancer(
                val id: Int = 0,
                val type: String = "",
                val login: String = "",
                val first_name: String = "",
                val last_name: String = "",
                val avatar: Avatar = Avatar(),
                val self: String = ""
            ) {
                data class Avatar(
                    val small: Small = Small(),
                    val large: Large = Large()
                ) {
                    data class Small(
                        val url: String = "",
                        val width: Int = 0,
                        val height: Int = 0
                    )

                    data class Large(
                        val url: String = "",
                        val width: Int = 0,
                        val height: Int = 0
                    )
                }
            }

            data class Project(
                val id: Int = 0,
                val type: String = "",
                val name: String = "",
                val status: Status = Status(),
                val safe_type: String? = null,
                val budget: Budget? = Budget(),
                val self: String = ""
            ) {
                data class Status(
                    val id: Int = 0,
                    val name: String = ""
                )
            }
        }
    }

    data class Links(
        val self: String = "",
        val first: String = "",
        val prev: String = "",
        val next: String = ""
    )
}