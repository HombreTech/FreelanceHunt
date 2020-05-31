package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ContestComment(
    val `data`: List<Data> = listOf(),
    val links: Links = Links()
) {
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes()
    ) : ViewModel {
        data class Attributes(
            val message: String = "",
            val message_html: String = "",
            val likes: Int = 0,
            val level: Int = 0,
            val parent_comment_id: Any? = null,
            val is_deleted: Boolean = false,
            val author: Author = Author(),
            val posted_at: String = ""
        ) {
            data class Author(
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
        val self: String = ""
    )
}