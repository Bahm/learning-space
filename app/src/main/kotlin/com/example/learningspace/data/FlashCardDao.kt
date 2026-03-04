package com.example.learningspace.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM flash_cards ORDER BY id ASC")
    fun getAll(): LiveData<List<FlashCard>>

    @Query("SELECT * FROM flash_cards WHERE deckId = :deckId ORDER BY id ASC")
    fun getByDeck(deckId: Int): LiveData<List<FlashCard>>

    @Query("SELECT * FROM flash_cards WHERE id = :id")
    suspend fun getById(id: Int): FlashCard?

    @Insert
    suspend fun insert(card: FlashCard)

    @Update
    suspend fun update(card: FlashCard)

    @Delete
    suspend fun delete(card: FlashCard)

    @Query("SELECT * FROM flash_cards WHERE dueDate <= :now ORDER BY dueDate ASC")
    suspend fun getDueCards(now: Long): List<FlashCard>

    @Query("SELECT * FROM flash_cards WHERE deckId = :deckId AND dueDate <= :now ORDER BY dueDate ASC")
    suspend fun getDueCardsByDeck(deckId: Int, now: Long): List<FlashCard>

    @Query("DELETE FROM flash_cards WHERE deckId = :deckId")
    suspend fun deleteByDeck(deckId: Int)
}
