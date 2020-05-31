package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.CONTESTS_LIST_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ContestsList

@Entity(tableName = CONTESTS_LIST_TABLE_NAME)
data class ContestsListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val url: String,
    val contestsList: ContestsList?
) : DomainMapper<ContestsList> {

    override fun mapToDomainModel() = contestsList ?: ContestsList()
}
