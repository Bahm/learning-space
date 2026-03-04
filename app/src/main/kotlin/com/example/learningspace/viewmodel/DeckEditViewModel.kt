package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.Deck
import com.example.learningspace.data.DeckRepository
import com.example.learningspace.data.FlashCardDatabase
import kotlinx.coroutines.launch

class DeckEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DeckRepository

    init {
        val db = FlashCardDatabase.getInstance(application)
        repository = DeckRepository(db.deckDao())
    }

    suspend fun getById(id: Int): Deck? = repository.getById(id)

    fun saveDeck(id: Int?, name: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            if (id == null) {
                repository.insert(Deck(name = name))
            } else {
                repository.update(Deck(id = id, name = name))
            }
            onComplete()
        }
    }
}
