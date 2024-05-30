package com.example.polus.data

data class Question(
    val title: String? = "",
    val description: String? = "",
    val subject: String? = "",
    val creator: String? = "",
    val creationDate: Long = System.currentTimeMillis(),
    val comments: List<Comment>? = listOf()

)
