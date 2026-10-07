package com.example.chitaysostav.presentation.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitaysostav.domain.usecase.history.HistoryUseCase
import com.example.chitaysostav.presentation.history.HistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistory: HistoryUseCase
) : ViewModel() {

    val history: StateFlow<HistoryUiState> = getHistory.getAll()
        .map { list ->
            HistoryUiState.Success(list)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            HistoryUiState.Loading
        )

    fun clearHistory() {
        viewModelScope.launch {
            getHistory.clearAll()
        }
    }
}