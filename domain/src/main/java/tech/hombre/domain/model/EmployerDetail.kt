package tech.hombre.domain.model

import android.os.Parcelable
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.android.parcel.Parcelize

data class EmployerDetail(
    val `data`: Data = Data(),
    val links: Links = Links()
) {
    @Parcelize
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes(),
        val links: Links = Links()
    ) : ViewModel, Parcelable {
        @Parcelize
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
            val contacts: Contacts? = Contacts()
        ) : Parcelable {
            @Parcelize
            data class Avatar(
                val small: Small = Small(),
                val large: Large = Large()
            ) : Parcelable {
                @Parcelize
                data class Small(
                    val url: String = "",
                    val width: Int = 0,
                    val height: Int = 0
                ) : Parcelable

                @Parcelize
                data class Large(
                    val url: String = "",
                    val width: Int = 0,
                    val height: Int = 0
                ) : Parcelable
            }

            @Parcelize
            data class Contacts(
                val skype: String? = "",
                val telegram: String? = "",
                val phone: String? = "",
                val wmid: String? = "",
                val email: String? = "",
                val website: String? = ""
            ) : Parcelable

            @Parcelize
            data class Location(
                val country: Country? = Country(),
                val city: City? = City()
            ) : Parcelable {
                @Parcelize
                data class Country(
                    val id: Int = 0,
                    val name: String = ""
                ) : Parcelable

                @Parcelize
                data class City(
                    val id: Int = 0,
                    val name: String = ""
                ) : Parcelable
            }

            @Parcelize
            data class Verification(
                val identity: Boolean = false,
                val birth_date: Boolean = false,
                val phone: Boolean = false,
                val website: Boolean = false,
                val wmid: Boolean = false,
                val email: Boolean = false
            ) : Parcelable
        }

        @Parcelize
        data class Links(
            val self: Self = Self(),
            val reviews: String = ""
        ) : Parcelable {
            @Parcelize
            data class Self(
                val api: String = "",
                val web: String = ""
            ) : Parcelable
        }

        override fun hashCode(): Int {
            return this.id
        }

        override fun equals(other: Any?): Boolean {
            if (other is Data) {
                val pm: Data = other
                return pm.id == this.id
            }
            return false
        }
    }

    @Parcelize
    data class Links(
        val self: String = ""
    ) : Parcelable
}