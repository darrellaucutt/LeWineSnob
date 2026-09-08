package net.aucutt.lewinesnob.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WineViewModel : ViewModel() {
    private val _wines = MutableStateFlow<List<Wine>>(emptyList())
    val wines: StateFlow<List<Wine>> = _wines.asStateFlow()

    fun addWine(wine: Wine) {
        _wines.update { it + wine }
    }
}
