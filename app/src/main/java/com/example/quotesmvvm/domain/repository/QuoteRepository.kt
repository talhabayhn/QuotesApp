package com.example.quotesmvvm.domain.repository

import com.example.quotesmvvm.domain.model.Quote

interface QuoteRepository {
    suspend fun fetchQuotes(): List<Quote>
    suspend fun loadMore(): List<Quote>
    suspend fun toggleFavorite(id: String): Quote?
    suspend fun getById(id: String): Quote?

}