package com.example.polus.ui.questions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.config.AppDatabase
import com.example.polus.data.Comment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionDetailFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var subjectTextView: TextView
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentEditText: EditText
    private lateinit var addCommentButton: Button

    private lateinit var adapter: CommentAdapter
    private val commentsList = mutableListOf<Comment>()
    private var questionId: Int = 0
    private lateinit var db: AppDatabase

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

        db = AppDatabase.getDatabase(requireContext())

        questionId = arguments?.getInt("questionId") ?: 0
        if (questionId != 0) {
            fetchQuestionDetails(questionId)
        }

        addCommentButton.setOnClickListener {
            addCommentToQuestion()
        }

        return view
    }

    private fun fetchQuestionDetails(questionId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val questionWithComments = db.questionDao().getQuestionWithComments(questionId)
            withContext(Dispatchers.Main) {
                if (questionWithComments != null) {
                    titleTextView.text = questionWithComments.question.title
                    descriptionTextView.text = questionWithComments.question.description
                    subjectTextView.text = questionWithComments.question.subject

                    // Assuming commentsList is a MutableList<Comment> initialized elsewhere
                    commentsList.clear()
                    commentsList.addAll(questionWithComments.comments)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Failed to load question details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addCommentToQuestion() {
        val commentText = commentEditText.text.toString().trim()
        if (commentText.isNotEmpty()) {
            val creator = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"
            val comment = Comment(text = commentText, creator = creator, questionId = questionId)

            lifecycleScope.launch(Dispatchers.IO) {
                db.questionDao().insertComment(comment)
                withContext(Dispatchers.Main) {
                    commentsList.add(comment)
                    adapter.notifyDataSetChanged()
                    commentEditText.text.clear()

                    // Hide the keyboard
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(commentEditText.windowToken, 0)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }


//    private fun addCommentToQuestion() {
//        val commentText = commentEditText.text.toString().trim()
//        if (commentText.isNotEmpty()) {
//            val creator = "Anonymous" // Use a proper user identification mechanism
//            val comment = Comment(text = commentText, creator = creator)
//
//            lifecycleScope.launch(Dispatchers.IO) {
//                val question = db.questionDao().getQuestionById(questionId)
//                if (question != null) {
//                    val updatedComments = question.comments.toMutableList()
//                    updatedComments.add(comment)
//
//                    // Update the question with the new list of comments
//                    question.comments = updatedComments
//                    db.questionDao().update(question)
//
//                    // Update UI on the main thread
//                    withContext(Dispatchers.Main) {
//                        commentsList.add(comment)
//                        adapter.notifyDataSetChanged()
//                        commentEditText.text.clear()
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(requireContext(), "Failed to add comment", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(requireContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show()
//        }
//    }

}
