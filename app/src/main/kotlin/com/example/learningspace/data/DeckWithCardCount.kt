package com.example.learningspace.data

import androidx.room.Embedded

data class DeckWithCardCount(
    @Embedded val deck: Deck,
    val cardCount: Int
)
