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

    @Query(
        """
        SELECT * FROM wines
        WHERE brand = :brand COLLATE NOCASE
          AND type = :type COLLATE NOCASE
          AND varietal = :varietal COLLATE NOCASE
          AND year IS :year
        LIMIT 1
        """
    )
    suspend fun findCollision(brand: String, type: String, varietal: String, year: Int?): Wine?

    @Query("UPDATE wines SET rating = :rating WHERE id = :id")
    suspend fun updateRating(id: String, rating: Int)

    @Query("DELETE FROM wines WHERE id = :id")
    suspend fun deleteById(id: String)
}
