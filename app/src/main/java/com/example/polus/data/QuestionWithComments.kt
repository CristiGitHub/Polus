// QuestionWithComments.kt
package com.example.polus.data

import androidx.room.Embedded
import androidx.room.Relation


data class QuestionWithComments(
    @Embedded val question: Question,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val comments: List<Comment>
)
