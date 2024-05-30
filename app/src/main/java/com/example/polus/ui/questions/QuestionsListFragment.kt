package com.example.polus.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.data.Question
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuestionsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionAdapter
    private val questionsList = mutableListOf<Question>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_questions_list, container, false)

        recyclerView = view.findViewById(R.id.questionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = QuestionAdapter(questionsList)
        recyclerView.adapter = adapter

        recyclerView.adapter = adapter

        fetchQuestionsFromDatabase()

        return view
    }

    private fun fetchQuestionsFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("questions")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionsList.clear()
                for (questionSnapshot in snapshot.children) {
                    val question = questionSnapshot.getValue(Question::class.java)
                    if (question != null) {
                        questionsList.add(question)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }


    // TODO : implement navigation part
    private fun navigateToQuestionDetail(questionId: String) {
        val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionDetailFragment(questionId)
        requireActivity().findNavController().navigate(action)
    }
}
