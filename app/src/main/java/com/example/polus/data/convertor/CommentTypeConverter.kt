package com.example.polus.data.convertor

import androidx.room.TypeConverter
import com.example.polus.data.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CommentTypeConverter {
    @TypeConverter
    fun fromCommentsList(comments: List<Comment>?): String? {
        if (comments == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Comment>>() {}.type
        return gson.toJson(comments, type)
    }

    @TypeConverter
    fun toCommentsList(commentsString: String?): List<Comment>? {
        if (commentsString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Comment>>() {}.type
        return gson.fromJson(commentsString, type)
    }
}