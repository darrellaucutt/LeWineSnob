package net.aucutt.lewinesnob.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TastingNoteDao {
    @Query("SELECT * FROM tasting_notes WHERE wineId = :wineId ORDER BY date DESC")
    fun observeNotesForWine(wineId: String): Flow<List<TastingNote>>

    @Upsert
    suspend fun upsert(note: TastingNote)

    @Query("DELETE FROM tasting_notes WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM tasting_notes WHERE wineId = :wineId")
    suspend fun deleteByWineId(wineId: String)
}
