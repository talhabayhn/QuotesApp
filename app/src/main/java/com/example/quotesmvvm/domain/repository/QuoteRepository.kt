package com.example.quotesmvvm.domain.repository

import com.example.quotesmvvm.domain.model.Quote

interface QuoteRepository {
    suspend fun fetchQuotes(): List<Quote>
}