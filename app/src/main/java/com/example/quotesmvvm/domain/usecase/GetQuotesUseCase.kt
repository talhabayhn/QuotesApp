package com.example.quotesmvvm.domain.usecase

import com.example.quotesmvvm.domain.model.Quote
import com.example.quotesmvvm.core.Result
import com.example.quotesmvvm.domain.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetQuotesUseCase(
    private val repository: QuoteRepository
) {
    // Tek sorumlu: quotes getir ve domain Result döndür
    suspend operator fun invoke(reset : Boolean): Result<List<Quote>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val data = repository.fetchQuotes()
            Result.Success(data)
        } catch (t: Throwable) {
            Result.Error(message = t.message ?: "Unexpected error", throwable = t)
        }
    }
}