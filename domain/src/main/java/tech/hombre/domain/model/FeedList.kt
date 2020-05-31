package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class FeedList(
    val `data`: List<Data> = listOf(),
    val links: Links = Links()
) {
    data class Data(
        val id: Long = 0,
        val type: String = "",
        val attributes: Attributes = Attributes(),
        val links: Links = Links()
    ) : ViewModel {
        data class Attributes(
            val from: From? = From(),
            val message: String = "",
            val is_new: Boolean = false,
            val created_at: String = ""
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
        }

        data class Links(
            val project: String = ""
        )
    }

    data class Links(
        val self: String = ""
    )
}