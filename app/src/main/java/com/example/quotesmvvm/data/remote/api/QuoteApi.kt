package com.example.quotesmvvm.data.remote.api

import com.example.quotesmvvm.data.remote.dto.DummyQuoteDto
import com.example.quotesmvvm.data.remote.dto.DummyQuotesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuoteApi {
    @GET("quotes")
    suspend fun getQuotes(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): DummyQuotesResponse
    @GET("quotes/{id}")
    suspend fun getQuoteById(@Path("id") id: String): DummyQuoteDto
}