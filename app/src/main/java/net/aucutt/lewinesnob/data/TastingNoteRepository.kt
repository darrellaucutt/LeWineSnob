package net.aucutt.lewinesnob.data

class TastingNoteRepository(
    private val tastingNoteDao: TastingNoteDao,
) {
    fun observeNotesForWine(wineId: String) = tastingNoteDao.observeNotesForWine(wineId)

    suspend fun addNote(note: TastingNote) {
        tastingNoteDao.upsert(note)
    }

    suspend fun deleteNote(id: String) {
        tastingNoteDao.deleteById(id)
    }

    suspend fun deleteNotesForWine(wineId: String) {
        tastingNoteDao.deleteByWineId(wineId)
    }
}
