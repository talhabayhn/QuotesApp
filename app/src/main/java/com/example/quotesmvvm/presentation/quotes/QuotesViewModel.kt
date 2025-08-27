package com.example.quotesmvvm.presentation.quotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quotesmvvm.domain.usecase.GetQuotesUseCase
import com.example.quotesmvvm.core.Result
import com.example.quotesmvvm.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuotesViewModel(
    private val getQuotes: GetQuotesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(QuotesContract.State(isLoading = true))
    val state: StateFlow<QuotesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuotesContract.Effect>()
    val effect: SharedFlow<QuotesContract.Effect> = _effect.asSharedFlow()

    private val searchFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        load()
        viewModelScope.launch {
            searchFlow.debounce(300).distinctUntilChanged().collect { q ->
                _state.update { it.copy(query = q) }
                applyFilter()
            }
        }
    }

    fun onEvent(e: QuotesContract.Event) {
        when (e) {
            QuotesContract.Event.Refresh -> load(force = true)
            is QuotesContract.Event.Search -> searchFlow.tryEmit(e.text)
            is QuotesContract.Event.ClickItem -> navigateDetail(e.id)
            is QuotesContract.Event.ToggleFavorite -> toggleFav(e.id)
        }
    }

    private fun load(force: Boolean = false) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        val res = getQuotes()
        when (res) {
            is Result.Success -> {
                val items = if (force) res.data else res.data
                _state.update { it.copy(isLoading = false, items = items) }
                applyFilter()
            }
            is Result.Error -> _state.update { it.copy(isLoading = false, error = res.message) }
            Result.Loading -> Unit
        }
    }

    private fun applyFilter() {
        val q = _state.value.query.trim()
        if (q.isBlank()) {
            _state.update { it.copy(items = it.items.sortedBy { it.author }) }
            return
        }
        val filtered = _state.value.items.filter {
            it.author.contains(q, true) || it.content.contains(q, true)
        }
        _state.update { it.copy(items = filtered) }
    }

    private fun toggleFav(id: String) = viewModelScope.launch {
        when (val res = toggleFavorite(id)) {
            is Result.Success -> {
                val updated = res.data
                val newList = _state.value.items.map { if (it.id == updated.id) updated else it }
                _state.update { it.copy(items = newList) }
            }
            is Result.Error -> _effect.emit(QuotesContract.Effect.ShowMessage(res.message))
            Result.Loading -> Unit
        }
    }

    private fun navigateDetail(id: String) = viewModelScope.launch {
        _effect.emit(QuotesContract.Effect.NavigateToDetail(id))
    }

    class Factory(
        private val getQuotes: GetQuotesUseCase,
        private val toggleFavorite: ToggleFavoriteUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T =
            QuotesViewModel(getQuotes, toggleFavorite) as T
        }
}