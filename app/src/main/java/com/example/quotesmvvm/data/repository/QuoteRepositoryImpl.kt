package com.example.quotesmvvm.data.repository

import com.example.quotesmvvm.data.mapper.toDomain
import com.example.quotesmvvm.data.remote.RetrofitClient
import com.example.quotesmvvm.domain.model.Quote
import com.example.quotesmvvm.domain.repository.QuoteRepository

class QuoteRepositoryImpl : QuoteRepository {

    private val api = RetrofitClient.api

    override suspend fun fetchQuotes(): List<Quote> {
        val response = api.getQuotes()
        return response.quotes.map { it.toDomain() }
    }
}