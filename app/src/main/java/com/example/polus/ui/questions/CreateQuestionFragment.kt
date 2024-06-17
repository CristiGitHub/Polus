package com.example.polus.ui.questions
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.polus.R
import com.example.polus.models.Question
import com.google.firebase.Timestamp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateQuestionFragment : Fragment() {

    private lateinit var subjectEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var tagsAutoCompleteTextView: AutoCompleteTextView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_question, container, false)

        subjectEditText = view.findViewById(R.id.subjectEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        tagsAutoCompleteTextView = view.findViewById(R.id.tagsAutoCompleteTextView)

        databaseReference = FirebaseDatabase.getInstance().getReference("questions")

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            postQuestion()
        }

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    private fun postQuestion() {
        val subject = subjectEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val dueDate = Timestamp.now()
        val tags = tagsAutoCompleteTextView.text.toString().trim()

        if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(description) || TextUtils.isEmpty(tags)) {
            Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val id = databaseReference.push().key
        val question = Question(id, subject, description, dueDate,null,null,tags)

        if (id != null) {
            databaseReference.child(id).setValue(question).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Question created", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(activity, "Failed to create question", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
