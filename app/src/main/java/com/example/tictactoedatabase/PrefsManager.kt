package com.example.tictactoedatabase

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    // Save the username and password when user logs in
    fun saveLoginInfo(username: String, password: String) {
        prefs.edit().putString("username", username).putString("password", password).apply()
    }

    // Get the saved username
    fun getSavedUsername(): String? = prefs.getString("username", null)

    // Get the saved password
    fun getSavedPassword(): String? = prefs.getString("password", null)

    // Clear the login info when user click Exit or Logout Button.
    fun clearLoginInfo() {
        prefs.edit().remove("username").remove("password").apply()
    }

    // Save the names of PlayerX and PlayerO from the config screen
    fun savePlayerConfig(playerX: String, playerO: String) {
        prefs.edit().putString("playerX", playerX).putString("playerO", playerO).apply()
    }

    // Get player names here use default PlayerX and PlayerO if not set
    fun getPlayerConfig(): Pair<String, String> {
        val playerX = prefs.getString("playerX", "Player X") ?: "Player X"
        val playerO = prefs.getString("playerO", "Player O") ?: "Player O"
        return Pair(playerX, playerO)
    }
}