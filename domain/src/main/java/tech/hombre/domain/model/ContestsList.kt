package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ContestsList(
    val `data`: List<Data> = listOf(),
    val links: Links = Links()
) {
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes(),
        val links: Links = Links()
    ) : ViewModel {
        data class Attributes(
            val name: String = "",
            val description: String = "",
            val description_html: String = "",
            val skill: Skill = Skill(),
            val status: Status = Status(),
            val budget: Budget = Budget(),
            val application_count: Int = 0,
            val published_at: String = "",
            val duration_days: Int = 0,
            val final_started_at: String = "",
            val freelancer: Any? = null,
            val employer: Employer = Employer(),
            val updates: List<Any> = listOf()
        ) {
            data class Skill(
                val id: Int = 0,
                val name: String = ""
            )

            data class Status(
                val id: Int = 0,
                val name: String = ""
            )

            data class Budget(
                val amount: Int = 0,
                val currency: String = ""
            )

            data class Employer(
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
        }

        data class Links(
            val self: Self = Self(),
            val comments: String = "",
            val applications: String = ""
        ) {
            data class Self(
                val api: String = "",
                val web: String = ""
            )
        }
    }

    data class Links(
        val self: String = "",
        val first: String = "",
        val prev: String = "",
        val next: String = ""
    )
}