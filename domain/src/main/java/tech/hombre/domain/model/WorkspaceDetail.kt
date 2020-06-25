package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel


data class WorkspaceDetail(
    val `data`: Data = Data(),
    val links: Links = Links()
) {
    data class Data(
        val attributes: Attributes = Attributes(),
        val id: Int = 0,
        val links: Links = Links(),
        val type: String = ""
    ) : ViewModel {
        data class Attributes(
            val arbitrage_started_at: String? = "",
            val conditions: Conditions = Conditions(),
            val development_ends_at: String = "",
            val employer: Employer = Employer(),
            val freelancer: Freelancer = Freelancer(),
            val project: Project = Project()
        ) {
            data class Conditions(
                val budget: Budget = Budget(),
                val confirmed_at: String = "",
                val confirmed_by: ConfirmedBy = ConfirmedBy(),
                val days: Int = 0,
                val safe_type: String = ""
            ) {
                data class Budget(
                    val amount: Int = 0,
                    val currency: String = ""
                )

                data class ConfirmedBy(
                    val employer: Boolean = false,
                    val freelancer: Boolean = false
                )
            }

            data class Employer(
                val avatar: Avatar = Avatar(),
                val first_name: String = "",
                val id: Int = 0,
                val last_name: String = "",
                val login: String = "",
                val self: String = "",
                val type: String = ""
            ) {
                data class Avatar(
                    val large: Large = Large(),
                    val small: Small = Small()
                ) {
                    data class Large(
                        val height: Int = 0,
                        val url: String = "",
                        val width: Int = 0
                    )

                    data class Small(
                        val height: Int = 0,
                        val url: String = "",
                        val width: Int = 0
                    )
                }
            }

            data class Freelancer(
                val avatar: Avatar = Avatar(),
                val first_name: String = "",
                val id: Int = 0,
                val last_name: String = "",
                val login: String = "",
                val self: String = "",
                val type: String = ""
            ) {
                data class Avatar(
                    val large: Large = Large(),
                    val small: Small = Small()
                ) {
                    data class Large(
                        val height: Int = 0,
                        val url: String = "",
                        val width: Int = 0
                    )

                    data class Small(
                        val height: Int = 0,
                        val url: String = "",
                        val width: Int = 0
                    )
                }
            }

            data class Project(
                val budget: Budget? = Budget(),
                val id: Int = 0,
                val name: String = "",
                val safe_type: String = "",
                val self: String = "",
                val status: Status = Status(),
                val type: String = ""
            ) {
                data class Budget(
                    val amount: Int = 0,
                    val currency: String = ""
                )

                data class Status(
                    val id: Int = 0,
                    val name: String = ""
                )
            }
        }

        data class Links(
            val self: Self = Self(),
            val thread: String = ""
        ) {
            data class Self(
                val api: String = "",
                val web: String = ""
            )
        }
    }

    data class Links(
        val self: String = ""
    )
}