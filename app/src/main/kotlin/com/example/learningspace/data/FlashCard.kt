package com.example.learningspace.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flash_cards")
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val state: Int = STATE_NEW,
    val stability: Float = 0f,
    val difficulty: Float = 0f,
    val scheduledDays: Int = 0,
    val reps: Int = 0,
    val lapses: Int = 0,
    val dueDate: Long = 0L,
    val lastReview: Long = 0L
) {
    companion object {
        const val STATE_NEW = 0
        const val STATE_LEARNING = 1
        const val STATE_REVIEW = 2
        const val STATE_RELEARNING = 3
    }
}
