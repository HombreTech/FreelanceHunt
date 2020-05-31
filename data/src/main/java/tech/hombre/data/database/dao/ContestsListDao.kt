package tech.hombre.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.hombre.data.database.CONTESTS_LIST_TABLE_NAME
import tech.hombre.data.database.model.ContestsListEntity

@Dao
interface ContestsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContestsList(contestsList: ContestsListEntity)

    @Query("SELECT * FROM $CONTESTS_LIST_TABLE_NAME WHERE url = :url ORDER BY id DESC LIMIT 1")
    suspend fun getContestsList(url: String): ContestsListEntity
}