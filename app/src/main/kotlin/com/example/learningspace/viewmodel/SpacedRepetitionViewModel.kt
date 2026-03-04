package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.FlashCard
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.data.FlashCardRepository
import com.example.learningspace.data.FsrsAlgorithm
import kotlinx.coroutines.launch

class SpacedRepetitionViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val deckId: Int = savedStateHandle["deckId"] ?: 0

    private val repository: FlashCardRepository

    private var dueCards: List<FlashCard> = emptyList()
    private var currentIndex = 0

    private val _currentCard = MutableLiveData<FlashCard?>()
    private val _showAnswer = MutableLiveData(false)
    private val _sessionComplete = MutableLiveData(false)
    private val _currentPosition = MutableLiveData(0)
    private val _totalCards = MutableLiveData(0)

    val currentCard: LiveData<FlashCard?> = _currentCard
    val showAnswer: LiveData<Boolean> = _showAnswer
    val sessionComplete: LiveData<Boolean> = _sessionComplete
    val currentPosition: LiveData<Int> = _currentPosition
    val totalCards: LiveData<Int> = _totalCards

    init {
        val db = FlashCardDatabase.getInstance(application)
        repository = FlashCardRepository(db.flashCardDao())
        loadDueCards()
    }

    private fun loadDueCards() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            dueCards = repository.getDueCardsByDeck(deckId, now)
            _totalCards.value = dueCards.size
            if (dueCards.isEmpty()) {
                _sessionComplete.value = true
            } else {
                currentIndex = 0
                _currentCard.value = dueCards[0]
                _currentPosition.value = 1
            }
        }
    }

    fun showAnswer() {
        _showAnswer.value = true
    }

    fun rateCard(rating: Int) {
        val card = dueCards.getOrNull(currentIndex) ?: return
        viewModelScope.launch {
            val updatedCard = FsrsAlgorithm.applyRating(card, rating)
            repository.update(updatedCard)
            _showAnswer.value = false
            currentIndex++
            if (currentIndex >= dueCards.size) {
                _sessionComplete.value = true
            } else {
                _currentCard.value = dueCards[currentIndex]
                _currentPosition.value = currentIndex + 1
            }
        }
    }
}
