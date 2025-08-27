package com.example.quotesmvvm.presentation.quotes

import com.example.quotesmvvm.domain.model.Quote

/**
 * contract ile devam edilecek
 */
sealed class UiState {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class Success(val data: List<Quote>) : UiState()
}