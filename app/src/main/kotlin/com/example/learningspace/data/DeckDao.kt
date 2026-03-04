package com.example.learningspace.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DeckDao {
    @Query(
        """SELECT decks.*, COUNT(flash_cards.id) as cardCount
           FROM decks
           LEFT JOIN flash_cards ON decks.id = flash_cards.deckId
           GROUP BY decks.id
           ORDER BY decks.createdAt DESC"""
    )
    fun getAllWithCardCount(): LiveData<List<DeckWithCardCount>>

    @Query(
        """SELECT decks.*, COUNT(flash_cards.id) as cardCount
           FROM decks
           LEFT JOIN flash_cards ON decks.id = flash_cards.deckId
           GROUP BY decks.id
           ORDER BY decks.createdAt DESC"""
    )
    suspend fun getAllWithCardCountList(): List<DeckWithCardCount>

    @Query("SELECT * FROM decks WHERE id = :id")
    suspend fun getById(id: Int): Deck?

    @Insert
    suspend fun insert(deck: Deck)

    @Update
    suspend fun update(deck: Deck)

    @Delete
    suspend fun delete(deck: Deck)
}
