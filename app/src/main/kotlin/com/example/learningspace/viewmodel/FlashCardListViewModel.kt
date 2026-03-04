package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.FlashCard
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.data.FlashCardRepository
import kotlinx.coroutines.launch

class FlashCardListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FlashCardRepository(
        FlashCardDatabase.getInstance(application).flashCardDao()
    )
    val allCards = repository.allCards
    val dueCardCount: LiveData<Int> = repository.allCards.map { cards ->
        val now = System.currentTimeMillis()
        cards.count { it.dueDate <= now }
    }

    fun deleteCard(card: FlashCard) {
        viewModelScope.launch {
            repository.delete(card)
        }
    }

    fun insertCard(card: FlashCard) {
        viewModelScope.launch {
            repository.insert(card)
        }
    }
}
