package com.example.quotesmvvm.data.remote.api

import com.example.quotesmvvm.data.remote.dto.DummyQuotesResponse
import retrofit2.http.GET

interface QuoteApi {
    @GET("quotes")
    suspend fun getQuotes(): DummyQuotesResponse
}