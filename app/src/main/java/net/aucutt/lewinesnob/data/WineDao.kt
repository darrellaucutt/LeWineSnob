package net.aucutt.lewinesnob.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WineDao {
    @Query("SELECT * FROM wines ORDER BY brand COLLATE NOCASE ASC")
    fun observeWines(): Flow<List<Wine>>

    @Upsert
    suspend fun upsert(wine: Wine)

    @Query("DELETE FROM wines WHERE id = :id")
    suspend fun deleteById(id: String)
}
