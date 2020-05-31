package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class FreelancerReviews(
    val `data`: List<Data> = listOf(),
    val links: Links = Links()
) {
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes()
    ) : ViewModel {
        data class Attributes(
            val published_at: String = "",
            val is_pending: Boolean = false,
            val pending_ends_at: String? = "",
            val comment: String? = "",
            val grades: Grades = Grades(),
            val from: From = From(),
            val project: Project = Project()
        ) {
            data class Grades(
                val quality: Int? = 0,
                val professionalism: Int? = 0,
                val cost: Int? = 0,
                val connectivity: Int? = 0,
                val schedule: Int? = 0,
                val total: Float? = 0F
            )

            data class From(
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
                val id: Long = 0L,
                val type: String = "",
                val name: String = "",
                val status: Status = Status(),
                val safe_type: String? = "",
                val budget: Budget? = Budget(),
                val self: String = ""
            ) {
                data class Status(
                    val id: Int = 0,
                    val name: String = ""
                )

                data class Budget(
                    val amount: Int = 0,
                    val currency: String = ""
                )
            }
        }
    }

    data class Links(
        val self: String = "",
        val next: String = "",
        val last: String = ""
    )
}