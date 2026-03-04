package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.Deck
import com.example.learningspace.data.DeckRepository
import com.example.learningspace.data.DeckWithCardCount
import com.example.learningspace.data.FlashCard
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

    private val _allDecks = MutableLiveData<List<DeckWithCardCount>>(emptyList())
    val allDecks: LiveData<List<DeckWithCardCount>> = _allDecks

    fun refresh() {
        viewModelScope.launch {
            _allDecks.value = deckRepository.getAllDecksWithCardCountList()
        }
    }

    fun deleteDeck(deck: Deck, onReadyToUndo: (List<FlashCard>) -> Unit) {
        viewModelScope.launch {
            val cards = flashCardRepository.getByDeckList(deck.id)
            flashCardRepository.deleteByDeck(deck.id)
            deckRepository.delete(deck)
            _allDecks.value = deckRepository.getAllDecksWithCardCountList()
            onReadyToUndo(cards)
        }
    }

    fun restoreDeck(deck: Deck, cards: List<FlashCard>) {
        viewModelScope.launch {
            deckRepository.insert(deck)
            cards.forEach { flashCardRepository.insert(it) }
            _allDecks.value = deckRepository.getAllDecksWithCardCountList()
        }
    }

    fun insertDeck(deck: Deck) {
        viewModelScope.launch {
            deckRepository.insert(deck)
        }
    }
}
