package com.example.tictactoedatabase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// The login screen is here
@Composable
fun LoginScreen(prefsManager: PrefsManager, onLoginSuccess: () -> Unit) {
    // Pre-fill the fields if the user has logged in before
    var username by remember { mutableStateOf(prefsManager.getSavedUsername() ?: "") }
    var password by remember { mutableStateOf(prefsManager.getSavedPassword() ?: "") }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tic Tac Toe Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        // Input to username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Input to password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        // Show error message
        if (errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login button logic
        Button(
            onClick = {
                // Check if both fields are not empty
                if (username.isNotBlank() && password.isNotBlank()) {
                    prefsManager.saveLoginInfo(username, password)
                    onLoginSuccess() // Go to the next screen
                } else {
                    errorMsg = "Please enter user name and password"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}