package com.example.quotesmvvm.data.mapper

import com.example.quotesmvvm.data.remote.dto.DummyQuoteDto
import com.example.quotesmvvm.domain.model.Quote

fun DummyQuoteDto.toDomain(): Quote =
    Quote(
        id = id.toString(),
        author = author,
        content = quote
    )