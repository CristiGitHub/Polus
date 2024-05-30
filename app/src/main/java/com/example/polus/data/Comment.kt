package com.example.polus.data

data class Comment(
    val text: String? = "",
    val creator: String? = "",
    val creationDate: Long = System.currentTimeMillis()
)