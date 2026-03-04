package com.example.learningspace.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class DeckRepository(private val dao: DeckDao) {
    fun getAllDecksWithCardCount(): LiveData<List<DeckWithCardCount>> = dao.getAllWithCardCount()
    suspend fun getAllDecksWithCardCountList(): List<DeckWithCardCount> = dao.getAllWithCardCountList()
    fun getAllDecksWithCardCountFlow(): Flow<List<DeckWithCardCount>> = dao.getAllWithCardCountFlow()

    suspend fun insert(deck: Deck) = dao.insert(deck)
    suspend fun update(deck: Deck) = dao.update(deck)
    suspend fun delete(deck: Deck) = dao.delete(deck)
    suspend fun getById(id: Int): Deck? = dao.getById(id)
}
