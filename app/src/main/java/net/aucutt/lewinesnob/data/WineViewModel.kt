package net.aucutt.lewinesnob.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.aucutt.lewinesnob.LeWineSnobApplication
import java.util.UUID

class WineViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as LeWineSnobApplication
    private val wineRepository = app.wineRepository
    private val tastingNoteRepository = app.tastingNoteRepository

    val wines: StateFlow<List<Wine>> = wineRepository.observeWines().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    suspend fun findCollision(brand: String, type: String, varietal: String, year: Int?): Wine? {
        return wineRepository.findCollision(brand, type, varietal, year)
    }

    fun addWine(wine: Wine, notes: List<TastingNote> = emptyList()) {
        viewModelScope.launch {
            wineRepository.addWine(wine)
            notes.forEach { tastingNoteRepository.addNote(it) }
        }
    }

    fun deleteWine(id: String) {
        viewModelScope.launch {
            wineRepository.deleteWine(id)
        }
    }

    fun notesForWine(wineId: String) = tastingNoteRepository.observeNotesForWine(wineId)

    fun updateRating(wineId: String, rating: Int) {
        viewModelScope.launch {
            wineRepository.updateRating(wineId, rating)
        }
    }

    fun addNote(wineId: String, text: String) {
        viewModelScope.launch {
            tastingNoteRepository.addNote(
                TastingNote(
                    id = UUID.randomUUID().toString(),
                    wineId = wineId,
                    date = System.currentTimeMillis(),
                    notes = text,
                )
            )
        }
    }
}
