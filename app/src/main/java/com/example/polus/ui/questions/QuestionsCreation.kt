package com.example.polus.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.polus.R
import com.example.polus.config.AppDatabase
import com.example.polus.data.Question
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionsCreation : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questions_creation, container, false)

        titleEditText = view.findViewById(R.id.titleEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        subjectEditText = view.findViewById(R.id.subjectEditText)
        submitButton = view.findViewById(R.id.submitButton)

        db = AppDatabase.getDatabase(requireContext())

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val subject = subjectEditText.text.toString().trim()
            val creator = FirebaseAuth.getInstance().currentUser?.uid ?: "Anonymous"

            if (title.isNotEmpty() && description.isNotEmpty() && subject.isNotEmpty()) {
                saveQuestionToDatabase(Question(title = title, description = description, subject = subject, creator = creator))
            } else {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun saveQuestionToDatabase(question: Question) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.questionDao().insertQuestion(question)
            launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Question created successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createQuestionFragment_to_questionsListFragment)
            }
        }
    }
}
