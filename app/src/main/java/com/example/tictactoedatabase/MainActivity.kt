package com.example.tictactoedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefsManager = PrefsManager(this)
        // Initialize the database helper
        val dbHelper = GameDbHelper(this)

        setContent {
            var currentScreen by remember { mutableStateOf("Login") }

            when (currentScreen) {
                "Login" -> LoginScreen(
                    prefsManager = prefsManager,
                    onLoginSuccess = { currentScreen = "Config" }
                )
                "Config" -> ConfigScreen(
                    prefsManager = prefsManager,
                    onStartGame = { currentScreen = "Game" },
                    onLogout = {
                        prefsManager.clearLoginInfo()
                        currentScreen = "Login"
                    }
                )
                "Game" -> GameScreen(
                    prefsManager = prefsManager,
                    dbHelper = dbHelper, // Pass the database helper
                    onBack = { currentScreen = "Config" },
                    onShowHistory = { currentScreen = "History" }
                )
                // New History screen
                "History" -> HistoryScreen(
                    dbHelper = dbHelper,
                    onBack = { currentScreen = "Game" }
                )
            }
        }
    }
}