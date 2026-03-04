package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.Deck
import com.example.learningspace.data.DeckRepository
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.data.FlashCardRepository
import kotlinx.coroutines.launch

class DeckListViewModel(application: Application) : AndroidViewModel(application) {
    private val deckRepository: DeckRepository
    private val flashCardRepository: FlashCardRepository

    init {
        val db = FlashCardDatabase.getInstance(application)
        deckRepository = DeckRepository(db.deckDao())
        flashCardRepository = FlashCardRepository(db.flashCardDao())
    }

    val allDecks = deckRepository.allDecksWithCardCount

    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            flashCardRepository.deleteByDeck(deck.id)
            deckRepository.delete(deck)
        }
    }

    fun insertDeck(deck: Deck) {
        viewModelScope.launch {
            deckRepository.insert(deck)
        }
    }
}
