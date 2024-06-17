package com.example.polus.models

import com.google.firebase.Timestamp

data class Answer(
    val answer: String? = "",
    val author: String? = "",
    val date: Timestamp? = Timestamp.now(),
    val id: String? = ""
)
