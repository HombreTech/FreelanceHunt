package tech.hombre.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.hombre.data.database.FEED_LIST_TABLE_NAME
import tech.hombre.data.database.model.FeedListEntity

@Dao
interface FeedListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFeedList(feedlist: FeedListEntity)

    @Query("SELECT * FROM $FEED_LIST_TABLE_NAME WHERE url = :url ORDER BY id DESC LIMIT 1")
    suspend fun getFeedList(url: String): FeedListEntity
}