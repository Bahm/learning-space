package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.FlashCard
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.data.FlashCardRepository
import kotlinx.coroutines.launch

class FlashCardListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FlashCardRepository
    val allCards = run {
        val db = FlashCardDatabase.getInstance(application)
        repository = FlashCardRepository(db.flashCardDao())
        repository.allCards
    }

    fun deleteCard(card: FlashCard) {
        viewModelScope.launch {
            repository.delete(card)
        }
    }
}
