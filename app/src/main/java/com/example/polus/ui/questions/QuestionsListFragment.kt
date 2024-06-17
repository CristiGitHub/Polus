package com.example.polus.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.config.AppDatabase
import com.example.polus.data.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionAdapter
    private val questionsList = mutableListOf<Question>()
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_questions_list, container, false)

        recyclerView = view.findViewById(R.id.questionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = QuestionAdapter(questionsList) { question ->
            navigateToQuestionDetails(question.id)
        }
        recyclerView.adapter = adapter

        db = AppDatabase.getDatabase(requireContext())

        fetchQuestionsFromDatabase()

        return view
    }

    private fun fetchQuestionsFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            val questions = db.questionDao().getAllQuestions()
            withContext(Dispatchers.Main) {
                adapter.updateQuestions(questions)
            }
        }
    }

    private fun navigateToQuestionDetails(questionId: Int) {
        val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionDetailsFragment(questionId)
        findNavController().navigate(action)
    }
}
