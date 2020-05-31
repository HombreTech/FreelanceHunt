package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ThreadsList(
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
            val subject: String = "",
            val first_post_at: String = "",
            val last_post_at: String = "",
            val messages_count: Int = 0,
            val is_unread: Boolean = false,
            val has_attachments: Boolean = false,
            val participants: Participants = Participants()
        ) {
            data class Participants(
                val from: From = From(),
                val to: To = To()
            ) {
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

                data class To(
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
        }

        data class Links(
            val self: Self = Self()
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