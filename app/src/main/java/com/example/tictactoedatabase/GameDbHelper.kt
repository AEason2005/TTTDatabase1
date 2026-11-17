package com.example.tictactoedatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Helper class to manage the SQLite database creation and version management.
class GameDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database name and version
        private const val DATABASE_NAME = "GameRecords.db"
        private const val DATABASE_VERSION = 1

        // Table name
        private const val TABLE_RECORDS = "game_records"

        // Column names
        private const val COL_ID = "id"
        private const val COL_USERNAME = "username"
        private const val COL_ROUND = "round_number"
        private const val COL_FIVE_NUMBERS = "five_numbers" // Stores X's moves as a string like "1,3,5,7,9"
        private const val COL_TIME = "system_time"
    }

    // Called when the database is created for the first time.
    override fun onCreate(db: SQLiteDatabase?) {
        // SQL statement to create the table
        val createTable = "CREATE TABLE $TABLE_RECORDS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_USERNAME TEXT, " +
                "$COL_ROUND INTEGER, " +
                "$COL_FIVE_NUMBERS TEXT, " +
                "$COL_TIME TEXT)"
        db?.execSQL(createTable)
    }

    // Called when the database version changes (for future upgrades).
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RECORDS")
        onCreate(db)
    }

    // Function to insert a new game record into the database.
    fun insertRecord(username: String, round: Int, fiveNumbers: String, time: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_ROUND, round)
            put(COL_FIVE_NUMBERS, fiveNumbers)
            put(COL_TIME, time)
        }
        db.insert(TABLE_RECORDS, null, values)
        db.close()
    }

    // Function to get all records from the database (used for History screen).
    fun getAllRecords(): List<String> {
        val records = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_RECORDS", null)

        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME))
                val round = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROUND))
                val numbers = cursor.getString(cursor.getColumnIndexOrThrow(COL_FIVE_NUMBERS))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME))
                records.add("User: $username | Round: $round | Moves: $numbers | Time: $time")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return records
    }
}