package com.example.polus.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.data.Comment
import com.example.polus.data.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class QuestionDetailFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var subjectTextView: TextView
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentEditText: EditText
    private lateinit var addCommentButton: Button

    private lateinit var adapter: CommentAdapter
    private val commentsList = mutableListOf<Comment>()
    private lateinit var questionId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question_detail, container, false)

        titleTextView = view.findViewById(R.id.questionTitleTextView)
        descriptionTextView = view.findViewById(R.id.questionDescriptionTextView)
        subjectTextView = view.findViewById(R.id.questionSubjectTextView)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
        commentEditText = view.findViewById(R.id.commentEditText)
        addCommentButton = view.findViewById(R.id.addCommentButton)

        commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CommentAdapter(commentsList)
        commentsRecyclerView.adapter = adapter

        questionId = arguments?.getString("questionId") ?: ""
        if (questionId.isNotEmpty()) {
            fetchQuestionDetails(questionId)
        }

        addCommentButton.setOnClickListener {
            addCommentToQuestion()
        }

        return view
    }

    private fun fetchQuestionDetails(questionId: String) {
        val database = FirebaseDatabase.getInstance().reference.child("questions").child(questionId)
        database.get().addOnSuccessListener { snapshot ->
            val question = snapshot.getValue(Question::class.java)
            if (question != null) {
                titleTextView.text = question.title
                descriptionTextView.text = question.description
                subjectTextView.text = question.subject
                if (question.comments != null) {
                    commentsList.addAll(question.comments!!)
                }
                adapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load question details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCommentToQuestion() {
        val commentText = commentEditText.text.toString().trim()
        if (commentText.isNotEmpty()) {
            val creator = FirebaseAuth.getInstance().currentUser?.uid ?: "Anonymous"
            val comment = Comment(text = commentText, creator = creator)
            val database = FirebaseDatabase.getInstance().reference.child("questions").child(questionId).child("comments")
            database.push().setValue(comment).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    commentsList.add(comment)
                    adapter.notifyDataSetChanged()
                    commentEditText.text.clear()
                } else {
                    Toast.makeText(requireContext(), "Failed to add comment", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}
