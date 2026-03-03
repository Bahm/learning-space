package com.example.learningspace.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flash_cards")
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String
)
