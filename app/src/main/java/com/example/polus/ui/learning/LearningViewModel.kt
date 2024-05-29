package com.example.polus.ui.learning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LearningViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Learning Fragment"
    }
    val text: LiveData<String> = _text
}