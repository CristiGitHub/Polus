package com.example.polus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polus.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        loadSavedCredentials()

        if (firebaseAuth.currentUser != null) {
            navigateToMainActivity()
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {

            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val rememberMe = binding.rememberMeCheckBox.isChecked

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (rememberMe) {
                            saveCredentials(email, pass)
                        } else {
                            clearSavedCredentials()
                        }
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCredentials(email: String, password: String) {
        val sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("EMAIL", email)
            putString("PASSWORD", password)
            apply()
        }
    }

    private fun loadSavedCredentials() {
        val sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE) ?: return
        val email = sharedPref.getString("EMAIL", "")
        val password = sharedPref.getString("PASSWORD", "")
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            binding.emailEt.setText(email)
            binding.passET.setText(password)
            binding.rememberMeCheckBox.isChecked = true
        }
    }

    private fun clearSavedCredentials() {
        val sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove("EMAIL")
            remove("PASSWORD")
            apply()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}