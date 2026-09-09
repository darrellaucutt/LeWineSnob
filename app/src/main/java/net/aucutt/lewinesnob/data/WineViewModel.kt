package net.aucutt.lewinesnob.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.aucutt.lewinesnob.LeWineSnobApplication

class WineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as LeWineSnobApplication).wineRepository

    val wines: StateFlow<List<Wine>> = repository.observeWines().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    fun addWine(wine: Wine) {
        viewModelScope.launch {
            repository.addWine(wine)
        }
    }
}
