package com.example.quotesmvvm.domain.usecase

import com.example.quotesmvvm.domain.model.Quote
import com.example.quotesmvvm.domain.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.quotesmvvm.core.Result

class ToggleFavoriteUseCase(private val repo: QuoteRepository) {
    suspend operator fun invoke(id: String): Result<Quote> = withContext(Dispatchers.IO) {
        repo.toggleFavorite(id)?.let { Result.Success(it) }
            ?: Result.Error("Item not found")
    }
}