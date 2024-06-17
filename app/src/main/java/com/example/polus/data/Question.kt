package com.example.polus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.polus.data.Comment
import com.example.polus.data.convertor.CommentTypeConverter


@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String? = "",
    var description: String? = "",
    var subject: String? = "",
    var creator: String? = "",
    var creationDate: Long = System.currentTimeMillis(),
)
