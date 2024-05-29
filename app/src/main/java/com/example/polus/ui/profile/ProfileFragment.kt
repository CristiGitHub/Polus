package com.example.polus.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.polus.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val apiKey = "jA0aqOkI8mzbmFl7ApP+TA==2stEE0CCgwc1wH1b"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fetchQuoteOfTheDay()
        displayUserEmail()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchQuoteOfTheDay() {

        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.api-ninjas.com/v1/quotes?category=intelligence")
                .addHeader("X-Api-Key", apiKey)
                .build()

            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
            }

            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val moshi = Moshi.Builder()
                                .add(KotlinJsonAdapterFactory())
                                .build()
                            val type = com.squareup.moshi.Types.newParameterizedType(List::class.java, Quote::class.java)
                            val quoteAdapter = moshi.adapter<List<Quote>>(type)
                            val quotes = quoteAdapter.fromJson(responseBody)
                            val quote = quotes?.get(0)?.text

                            withContext(Dispatchers.Main) {
                                binding.textProfile.text = quote ?: "No quote available"
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.textProfile.text = "Failed to fetch quote"
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.textProfile.text = "An error occurred"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun displayUserEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            binding.textEmail.text = "Email: ${user.email}"
        }
    }

    data class Quote(
        @Json(name = "quote") val text: String
    )
}