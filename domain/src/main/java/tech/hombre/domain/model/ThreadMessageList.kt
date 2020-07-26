package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ThreadMessageList(
    val `data`: List<Data> = listOf(),
    val links: Links = Links(),
    val meta: Meta = Meta()
) {
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes()
    ) : ViewModel {
        data class Attributes(
            val message: String = "",
            val message_html: String = "",
            val posted_at: String = "",
            val attachments: List<Attachment> = listOf(),
            val participants: Participants = Participants()
        ) {
            data class Attachment(
                val file_type: String = "",
                val id: Int = 0,
                val name: String = "",
                val size: Int = 0,
                val thumbnail_url: String? = "",
                val type: String = "",
                val url: String = ""
            )

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
    }

    data class Links(
        val self: String = ""
    )

    data class Meta(
        val thread: Thread = Thread()
    ) {
        data class Thread(
            val id: Int = 0,
            val type: String = "",
            val attributes: Attributes = Attributes(),
            val links: Links = Links()
        ) {
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
    }
}