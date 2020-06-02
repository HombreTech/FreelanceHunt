package tech.hombre.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ContestDetail(
    val `data`: Data = Data(),
    val links: Links = Links()
) : Parcelable {

    @Parcelize
    data class Data(
        val id: Int = 0,
        val type: String = "",
        val attributes: Attributes = Attributes(),
        val links: Links = Links()
    ) : Parcelable {

        @Parcelize
        data class Attributes(
            val name: String = "",
            val description: String = "",
            val description_html: String? = "",
            val skill: Skill = Skill(),
            val status: Status = Status(),
            val budget: Budget = Budget(),
            val application_count: Int = 0,
            val published_at: String = "",
            val duration_days: Int = 0,
            val final_started_at: String = "",
            val employer: Employer = Employer(),
            val freelancer: Freelancer? = Freelancer(),
            val tags: List<Tag> = listOf(),
            val updates: List<Update> = listOf()
        ) : Parcelable {

            @Parcelize
            data class Skill(
                val id: Int = 0,
                val name: String = ""
            ) : Parcelable


            @Parcelize
            data class Status(
                val id: Int = 0,
                val name: String = ""
            ) : Parcelable


            @Parcelize
            data class Budget(
                val amount: Int = 0,
                val currency: String = ""
            ) : Parcelable


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
            data class Tag(
                val id: String = "",
                val name: String = ""
            ) : Parcelable


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
            val applications: String = ""
        ) : Parcelable {

            @Parcelize
            data class Self(
                val api: String = "",
                val web: String = ""
            ) : Parcelable
        }
    }


    @Parcelize
    data class Links(
        val self: String = ""
    ) : Parcelable
}