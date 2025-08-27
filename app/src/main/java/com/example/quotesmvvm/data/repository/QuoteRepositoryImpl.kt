package com.example.quotesmvvm.data.repository

import com.example.quotesmvvm.data.mapper.toDomain
import com.example.quotesmvvm.data.remote.RetrofitClient
import com.example.quotesmvvm.domain.model.Quote
import com.example.quotesmvvm.domain.repository.QuoteRepository

class QuoteRepositoryImpl : QuoteRepository {

    private val api = RetrofitClient.api
    private val cache = mutableListOf<Quote>()

    override suspend fun fetchQuotes(): List<Quote> {
        if (cache.isNotEmpty()) return cache.toList()
        val remote = api.getQuotes().quotes.map { it.toDomain() }
        cache.clear(); cache.addAll(remote)
        return remote
    }

    override suspend fun toggleFavorite(id: String): Quote? {
        val idx = cache.indexOfFirst { it.id == id }
        if (idx == -1) return null
        val updated = cache[idx].copy(isFavorite = !cache[idx].isFavorite)
        cache[idx] = updated
        return updated
    }

    override suspend fun getById(id: String): Quote? {
        // önce cache'te var mı bak
        val cached = cache.firstOrNull { it.id == id.toString() }
        if (cached != null) return cached

        // yoksa API'den getir
        return api.getQuoteById(id).toDomain().also {
            cache.add(it) // cache'e ekle
        }
    }
}