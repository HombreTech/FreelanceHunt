package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.MY_PROFILE_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.MyProfile

@Entity(tableName = MY_PROFILE_TABLE_NAME)
data class MyProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val profile: MyProfile?
) : DomainMapper<MyProfile> {

    override fun mapToDomainModel() = profile ?: MyProfile()
}
