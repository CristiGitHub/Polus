package com.example.polus.models

data class UppVotes(
    val postId: Question,
    val userId: String?="",
    val vote: Int?= 1
)
