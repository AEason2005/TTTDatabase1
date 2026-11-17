package com.example.tictactoedatabase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Screen to display all game records saved in the SQLite database.
@Composable
fun HistoryScreen(dbHelper: GameDbHelper, onBack: () -> Unit) {
    // Read all records from the database when the screen loads
    val records = remember { dbHelper.getAllRecords() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Game History", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (records.isEmpty()) {
            Text("No games played yet.")
        } else {
            // Display each record as a list item
            LazyColumn {
                items(records) { record ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(text = record, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back to Game")
        }
    }
}