package com.example.quotesmvvm.presentation.quotes

import com.example.quotesmvvm.domain.model.Quote

object QuotesContract {
    data class State(
        val isLoading: Boolean = false,
        val items: List<Quote> = emptyList(),
        val error: String? = null,
        val query: String = ""
    )

    sealed interface Event {
        data object Refresh : Event
        data class Search(val text: String) : Event
        data class ClickItem(val id: String) : Event
        data class ToggleFavorite(val id: String) : Event
    }

    sealed interface Effect {
        data class NavigateToDetail(val id: String) : Effect
        data class ShowMessage(val text: String) : Effect
    }
}