package com.example.learningspace.data

import androidx.lifecycle.LiveData

class FlashCardRepository(private val dao: FlashCardDao) {
    val allCards: LiveData<List<FlashCard>> = dao.getAll()

    suspend fun insert(card: FlashCard) = dao.insert(card)
    suspend fun update(card: FlashCard) = dao.update(card)
    suspend fun delete(card: FlashCard) = dao.delete(card)
    suspend fun getById(id: Int): FlashCard? = dao.getById(id)
}
