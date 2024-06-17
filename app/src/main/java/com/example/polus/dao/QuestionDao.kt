package com.example.polus.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update



import androidx.room.Transaction
import com.example.polus.data.Comment
import com.example.polus.data.Question

import com.example.polus.data.QuestionWithComments

@Dao
interface QuestionDao {
    @Insert
     fun insertQuestion(question: Question)

    @Insert
     fun insertComment(comment: Comment)

    @Query("SELECT * FROM questions")
     fun getAllQuestions(): List<Question>

    @Transaction
    @Query("SELECT * FROM questions WHERE id = :questionId")
     fun getQuestionById(questionId: Int): Question?

    @Transaction
    @Query("SELECT * FROM questions WHERE id = :questionId")
     fun getQuestionWithComments(questionId: Int): QuestionWithComments?
}