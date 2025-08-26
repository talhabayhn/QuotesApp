package com.example.quotesmvvm.presentation.quotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quotesmvvm.domain.usecase.GetQuotesUseCase
import com.example.quotesmvvm.core.Result
import kotlinx.coroutines.launch

class QuotesViewModel(
    private val getQuotes: GetQuotesUseCase
) : ViewModel() {

    private val _ui = MutableLiveData<UiState>(UiState.Loading)
    val ui: LiveData<UiState> = _ui

    init { load() }

    fun refresh() = load()

    private fun load() = viewModelScope.launch {
        _ui.value = UiState.Loading
        when (val res = getQuotes()) {
            is Result.Success -> _ui.value = UiState.Success(res.data)
            is Result.Error   -> _ui.value = UiState.Error(res.message)
            is Result.Loading -> _ui.value = UiState.Loading
        }
    }

    class Factory(
        private val getQuotes: GetQuotesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuotesViewModel(getQuotes) as T
        }
    }


}