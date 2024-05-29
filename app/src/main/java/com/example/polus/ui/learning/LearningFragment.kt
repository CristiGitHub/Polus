package com.example.polus.ui.learning

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.polus.databinding.FragmentLearningBinding
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
class LearningFragment : Fragment() {

    private var _binding: FragmentLearningBinding? = null
    private val binding get() = _binding!!
    private val apiKey = "jA0aqOkI8mzbmFl7ApP+TA==2stEE0CCgwc1wH1b"
    private var correctAnswer: String? = null

    private val viewModel: LearningViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fetchTriviaQuestion()

        binding.buttonSubmit.setOnClickListener {
            val userAnswer = binding.editAnswer.text.toString()
            checkAnswer(userAnswer)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchTriviaQuestion() {
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
                    Toast.makeText(context, "Failed to load question", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonResponse = JSONArray(responseBody.string())
                    val question = jsonResponse.getJSONObject(0).getString("question")
                    correctAnswer = jsonResponse.getJSONObject(0).getString("answer")

                    activity?.runOnUiThread {
                        binding.textQuestion.text = question
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        })
    }
    private fun checkAnswer(userAnswer: String) {
        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            binding.textFeedback.text = "Correct!"
            binding.textFeedback.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        } else {
            binding.textFeedback.text = "Incorrect! The correct answer is $correctAnswer."
            binding.textFeedback.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
        binding.textFeedback.visibility = View.VISIBLE
    }
}