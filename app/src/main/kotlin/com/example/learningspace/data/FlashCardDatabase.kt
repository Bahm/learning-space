package com.example.learningspace.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FlashCard::class, Deck::class], version = 3, exportSchema = false)
abstract class FlashCardDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao
    abstract fun deckDao(): DeckDao

    companion object {
        @Volatile
        private var INSTANCE: FlashCardDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN state INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN stability REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN difficulty REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN scheduledDays INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN reps INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN lapses INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN dueDate INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE flash_cards ADD COLUMN lastReview INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `decks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)"
                )
                database.execSQL(
                    "INSERT INTO `decks` (`name`, `createdAt`) VALUES ('Default', 0)"
                )
                database.execSQL(
                    "ALTER TABLE `flash_cards` ADD COLUMN `deckId` INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        fun getInstance(context: Context): FlashCardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashCardDatabase::class.java,
                    "flash_cards_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
