package com.example.quotesmvvm.data.remote.dto

import com.example.quotesmvvm.domain.model.Quote


data class DummyQuoteDto(
    val id: Int,
    val quote: String,
    val author: String
)

data class DummyQuotesResponse(
    val quotes: List<DummyQuoteDto>
)

fun DummyQuoteDto.toDomain() = Quote(id = id.toString(), author = author, content = quote)