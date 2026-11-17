package com.example.tictactoedatabase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Configuration screen to set player names for PlayerX and PlayerO
@Composable
fun ConfigScreen(prefsManager: PrefsManager, onStartGame: () -> Unit, onLogout: () -> Unit) {
    // Load saved player name
    val config = prefsManager.getPlayerConfig()
    var playerX by remember { mutableStateOf(config.first) }
    var playerO by remember { mutableStateOf(config.second) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Game Configuration", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))

        // Input for PlayerX name
        OutlinedTextField(
            value = playerX,
            onValueChange = { playerX = it },
            label = { Text("Player X's name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Input for PlayerO name
        OutlinedTextField(
            value = playerO,
            onValueChange = { playerO = it },
            label = { Text("Player O's name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // this button is save config and go to Game screen
        Button(
            onClick = {
                prefsManager.savePlayerConfig(playerX, playerO)
                onStartGame()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // this button is clear login info and go back to Login screen
        TextButton(onClick = onLogout) {
            Text("Exit")
        }
    }
}