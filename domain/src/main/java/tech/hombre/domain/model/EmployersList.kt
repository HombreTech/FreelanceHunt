package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class EmployersList(
    val `data`: List<EmployerDetail.Data> = listOf(),
    val links: Links = Links()
) {
    /*data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes(),
        val links: Links = Links()
    ) : ViewModel {
        data class Attributes(
            val login: String = "",
            val first_name: String = "",
            val last_name: String = "",
            val avatar: Avatar = Avatar(),
            val birth_date: String? = "",
            val created_at: String = "",
            val cv: String? = "",
            val cv_html: String? = "",
            val rating: Int = 0,
            val rating_position: Int = 0,
            val arbitrages: Int = 0,
            val positive_reviews: Int = 0,
            val negative_reviews: Int = 0,
            val plus_ends_at: String? = "",
            val is_plus_active: Boolean = false,
            val is_online: Boolean = false,
            val visited_at: String? = "",
            val location: Location? = Location(),
            val verification: Verification = Verification(),
            val contacts: Any? = null
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
                    val name: String = ""
                )
            }

            data class Verification(
                val identity: Boolean = false,
                val birth_date: Boolean = false,
                val phone: Boolean = false,
                val website: Boolean = false,
                val wmid: Boolean = false,
                val email: Boolean = false
            )
        }

        data class Links(
            val self: Self = Self(),
            val reviews: String = ""
        ) {
            data class Self(
                val api: String = "",
                val web: String = ""
            )
        }
    }*/

    data class Links(
        val self: String = "",
        val next: String = "",
        val last: String = ""
    )
}