package com.example.learningspace.data

import androidx.lifecycle.LiveData

class FlashCardRepository(private val dao: FlashCardDao) {
    val allCards: LiveData<List<FlashCard>> = dao.getAll()

    fun getByDeck(deckId: Int): LiveData<List<FlashCard>> = dao.getByDeck(deckId)

    suspend fun insert(card: FlashCard) = dao.insert(card)
    suspend fun update(card: FlashCard) = dao.update(card)
    suspend fun delete(card: FlashCard) = dao.delete(card)
    suspend fun getById(id: Int): FlashCard? = dao.getById(id)
    suspend fun getDueCards(now: Long): List<FlashCard> = dao.getDueCards(now)
    suspend fun getDueCardsByDeck(deckId: Int, now: Long): List<FlashCard> = dao.getDueCardsByDeck(deckId, now)
    suspend fun deleteByDeck(deckId: Int) = dao.deleteByDeck(deckId)
    suspend fun getByDeckList(deckId: Int): List<FlashCard> = dao.getByDeckList(deckId)
}
