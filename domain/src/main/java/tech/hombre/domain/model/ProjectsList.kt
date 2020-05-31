package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ProjectsList(
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
            val skills: List<Skill> = listOf(),
            val status: Status = Status(),
            val budget: Budget = Budget(),
            val bid_count: Int = 0,
            val is_remote_job: Boolean = false,
            val is_premium: Boolean = false,
            val is_only_for_plus: Boolean = false,
            val location: Location? = Location(),
            val safe_type: String? = "",
            val is_personal: Boolean = false,
            val employer: Employer? = Employer(),
            val freelancer: Any? = null,
            val updates: List<Any> = listOf(),
            val published_at: String = "",
            val expired_at: String = ""
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

            data class Location(
                val country: Country? = Country(),
                val city: City? = City()
            ) {
                data class Country(
                    val id: Int = 0,
                    val name: String = ""
                )

                data class City(
                    val id: Int = 0,
                    val name: String? = ""
                )
            }

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
            val bids: String = ""
        ) {
            data class Self(
                val api: String = "",
                val web: String = ""
            )
        }

        override fun equals(obj: Any?): Boolean {
            if (obj is Data) {
                val pm: Data = obj
                return pm.id == id
            }
            return false
        }
    }

    data class Links(
        val self: String = "",
        val first: String = "",
        val prev: String = "",
        val next: String = ""
    )
}