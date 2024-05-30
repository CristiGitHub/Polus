package com.example.polus.ui.learning

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.polus.R
import com.example.polus.databinding.FragmentLearningBinding
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class LearningFragment : Fragment() {

    private var _binding: FragmentLearningBinding? = null
    private val binding get() = _binding!!
    private val apiKey = "jA0aqOkI8mzbmFl7ApP+TA==2stEE0CCgwc1wH1b"
    private val viewModel: LearningViewModel by viewModels()
    private lateinit var triviaAdapter: TriviaAdapter
    private val triviaList = mutableListOf<Trivia>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        fetchTriviaQuestions()

        return root
    }

    private fun setupRecyclerView() {
        triviaAdapter = TriviaAdapter(triviaList) { userAnswer, correctAnswer ->
            checkAnswer(userAnswer, correctAnswer)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = triviaAdapter
    }

    private fun fetchTriviaQuestions() {
        binding.progressBar.visibility = View.VISIBLE
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.api-ninjas.com/v1/trivia?category=")
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                activity?.runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Failed to load questions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonResponse = JSONArray(responseBody.string())
                    for (i in 0 until jsonResponse.length()) {
                        val question = jsonResponse.getJSONObject(i).getString("question")
                        val answer = jsonResponse.getJSONObject(i).getString("answer")
                        triviaList.add(Trivia(question, answer))
                    }
                    activity?.runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        triviaAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun checkAnswer(userAnswer: String, correctAnswer: String) {
        binding.textFeedback.visibility = View.VISIBLE
        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            binding.textFeedback.text = "Correct!"
            binding.textFeedback.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        } else {
            binding.textFeedback.text = "Incorrect! The correct answer is $correctAnswer."
            binding.textFeedback.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
