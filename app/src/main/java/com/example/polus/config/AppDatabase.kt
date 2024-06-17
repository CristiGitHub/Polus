package com.example.polus.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.polus.dao.QuestionDao

import androidx.room.TypeConverters
import com.example.polus.data.Comment
import com.example.polus.data.Question
import com.example.polus.data.convertor.CommentTypeConverter


@Database(entities = [Question::class, Comment::class], version = 1, exportSchema = true)
@TypeConverters(CommentTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "question_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
