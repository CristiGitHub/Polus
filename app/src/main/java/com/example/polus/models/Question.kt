package com.example.polus.models

import com.google.firebase.Timestamp

data class Question(
    var id: String? = "",
    var subject: String? = "",
    var description: String? = "",
    var dueDate: Timestamp? = Timestamp.now(),
    var answers: List<Answer>? = listOf(),
    var uppVotes: List<UppVotes>? = listOf(),
    var tags: String? = ""
)

