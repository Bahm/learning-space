package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.FlashCard
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.data.FlashCardRepository
import kotlinx.coroutines.launch

class FlashCardEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FlashCardRepository

    init {
        val db = FlashCardDatabase.getInstance(application)
        repository = FlashCardRepository(db.flashCardDao())
    }

    suspend fun getById(id: Int): FlashCard? = repository.getById(id)

    fun saveCard(id: Int?, question: String, answer: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            if (id == null) {
                repository.insert(FlashCard(question = question, answer = answer))
            } else {
                repository.update(FlashCard(id = id, question = question, answer = answer))
            }
            onComplete()
        }
    }
}
