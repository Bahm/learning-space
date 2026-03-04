package com.example.learningspace.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.learningspace.data.FlashCard
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.data.FlashCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class FlashCardListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FlashCardRepository(
        FlashCardDatabase.getInstance(application).flashCardDao()
    )

    private val _deckId = MutableStateFlow(0)

    val cards: LiveData<List<FlashCard>> = _deckId
        .filter { it != 0 }
        .flatMapLatest { deckId -> repository.getByDeckFlow(deckId) }
        .asLiveData()

    val dueCardCount: LiveData<Int> = cards.map { list ->
        val now = System.currentTimeMillis()
        list.count { it.dueDate <= now }
    }

    fun setDeckId(deckId: Int) {
        _deckId.value = deckId
    }

    fun deleteCard(card: FlashCard) {
        viewModelScope.launch { repository.delete(card) }
    }

    fun insertCard(card: FlashCard) {
        viewModelScope.launch { repository.insert(card) }
    }
}
