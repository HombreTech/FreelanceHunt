package tech.hombre.domain.model

import android.os.Parcelable
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ProjectDetail(
    val `data`: Data = Data(),
    val links: Links = Links()
) : Parcelable {

    @Parcelize
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes(),
        val links: Links = Links()
    ): ViewModel, Parcelable {

        @Parcelize
        data class Attributes(
            val name: String = "",
            val description: String = "",
            val description_html: String? = "",
            val skills: List<Skill> = listOf(),
            val status: Status = Status(),
            val budget: Budget? = Budget(),
            val bid_count: Int = 0,
            val is_remote_job: Boolean = false,
            val is_premium: Boolean = false,
            val is_only_for_plus: Boolean = false,
            val location: Location? = Location(),
            val safe_type: String? = "",
            val is_personal: Boolean = false,
            val employer: Employer? = Employer(),
            val freelancer: Freelancer? = Freelancer(),
            val tags: List<Tag> = listOf(),
            val updates: List<Update> = listOf(),
            val published_at: String = "",
            val expired_at: String = ""
        ) : Parcelable {

            @Parcelize
            data class Skill(
                val id: Int = 0,
                val name: String = ""
            ) : Parcelable, ViewModel

            @Parcelize
            data class Tag(
                val id: Int = 0,
                val name: String = ""
            ) : Parcelable

            @Parcelize
            data class Status(
                val id: Int = 0,
                val name: String = ""
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
            data class Budget(
                val amount: Int = 0,
                val currency: String = ""
            ) : Parcelable

            @Parcelize
            data class Freelancer(
                val id: Int = 0,
                val type: String = "",
                val login: String = "",
                val first_name: String = "",
                val last_name: String = "",
                val avatar: Avatar = Avatar(),
                val self: String = ""
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
            }


            @Parcelize
            data class Employer(
                val id: Int = 0,
                val type: String = "",
                val login: String = "",
                val first_name: String = "",
                val last_name: String = "",
                val avatar: Avatar = Avatar(),
                val self: String = ""
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
            }


            @Parcelize
            data class Update(
                val description: String = "",
                val description_html: String = "",
                val published_at: String = ""
            ) : Parcelable
        }


        @Parcelize
        data class Links(
            val self: Self = Self(),
            val comments: String = "",
            val bids: String = ""
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