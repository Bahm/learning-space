package com.example.learningspace.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FlashCard::class], version = 1, exportSchema = false)
abstract class FlashCardDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao

    companion object {
        @Volatile
        private var INSTANCE: FlashCardDatabase? = null

        fun getInstance(context: Context): FlashCardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashCardDatabase::class.java,
                    "flash_cards_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
