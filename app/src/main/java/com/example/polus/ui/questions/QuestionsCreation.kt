package com.example.polus.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.polus.R
import com.example.polus.data.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class QuestionsCreation : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questions_creation, container, false)

        titleEditText = view.findViewById(R.id.titleEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        subjectEditText = view.findViewById(R.id.subjectEditText)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val subject = subjectEditText.text.toString().trim()
            val creator = FirebaseAuth.getInstance().currentUser?.uid ?: "Anonymous"

            if (title.isNotEmpty() && description.isNotEmpty() && subject.isNotEmpty()) {
                saveQuestionToDatabase(Question(title, description, subject, creator))
            } else {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun saveQuestionToDatabase(question: Question) {
        val database = FirebaseDatabase.getInstance().reference
        val questionId = database.child("questions").push().key
        if (questionId != null) {
            database.child("questions").child(questionId).setValue(question)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Question created successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_createQuestionFragment_to_questionsListFragment)
                    } else {
                        Toast.makeText(requireContext(), "Failed to create question", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
