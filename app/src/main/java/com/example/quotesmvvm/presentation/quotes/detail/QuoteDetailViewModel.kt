package com.example.quotesmvvm.presentation.quotes.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quotesmvvm.domain.model.Quote
import com.example.quotesmvvm.domain.usecase.GetQuoteByIdUseCase
import kotlinx.coroutines.launch
import com.example.quotesmvvm.core.Result

class QuoteDetailViewModel(private val getById: GetQuoteByIdUseCase): ViewModel() {
    private val _ui = MutableLiveData<UiState>(UiState.Loading)
    val ui: LiveData<UiState> = _ui

    sealed class UiState {
        data object Loading : UiState()
        data class Error(val msg: String) : UiState()
        data class Success(val quote: Quote) : UiState()
    }

    fun load(id: String) = viewModelScope.launch {
        when (val res = getById(id)) {
            is Result.Success -> _ui.value = UiState.Success(res.data)
            is Result.Error   -> _ui.value = UiState.Error(res.message)
            Result.Loading    -> Unit
        }
    }

    class Factory(private val getById: GetQuoteByIdUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(c: Class<T>): T = QuoteDetailViewModel(getById) as T
    }
}