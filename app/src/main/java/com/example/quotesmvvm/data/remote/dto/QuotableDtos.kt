package com.example.quotesmvvm.data.remote.dto


data class DummyQuoteDto(
    val id: Int,
    val quote: String,
    val author: String
)

data class DummyQuotesResponse(
    val quotes: List<DummyQuoteDto>
)