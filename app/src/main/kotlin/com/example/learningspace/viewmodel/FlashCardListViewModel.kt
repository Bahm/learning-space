package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var currentDeckId: Int = 0
    private val _cards = MutableLiveData<List<FlashCard>>(emptyList())
    val cards: LiveData<List<FlashCard>> = _cards

    val dueCardCount: LiveData<Int> = cards.map { list ->
        val now = System.currentTimeMillis()
        list.count { it.dueDate <= now }
    }

    fun setDeckId(deckId: Int) {
        currentDeckId = deckId
        viewModelScope.launch {
            _cards.value = repository.getByDeckList(deckId)
        }
    }

    fun deleteCard(card: FlashCard) {
        viewModelScope.launch {
            repository.delete(card)
            _cards.value = repository.getByDeckList(currentDeckId)
        }
    }

    fun insertCard(card: FlashCard) {
        viewModelScope.launch {
            repository.insert(card)
            _cards.value = repository.getByDeckList(currentDeckId)
        }
    }
}
