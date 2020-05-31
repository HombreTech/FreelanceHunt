package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.FEED_LIST_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.FeedList

@Entity(tableName = FEED_LIST_TABLE_NAME)
data class FeedListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val url: String,
    val feedList: FeedList?
) : DomainMapper<FeedList> {

    override fun mapToDomainModel() = feedList ?: FeedList()
}
