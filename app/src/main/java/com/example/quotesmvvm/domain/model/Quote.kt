package com.example.quotesmvvm.domain.model

data class Quote(
    val id: String,
    val author: String,
    val content: String,
    val isFavorite: Boolean = false
)