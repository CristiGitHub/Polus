package com.example.polus.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Question::class,
        parentColumns = ["id"],
        childColumns = ["questionId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var questionId: Int,
    var text: String,
    var creator: String
)