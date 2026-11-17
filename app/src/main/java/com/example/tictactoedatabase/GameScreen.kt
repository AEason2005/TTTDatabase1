package com.example.tictactoedatabase

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

// The main game screen. Handles the board, game logic, and saving records to SQLite.
@Composable
fun GameScreen(prefsManager: PrefsManager, dbHelper: GameDbHelper, onBack: () -> Unit, onShowHistory: () -> Unit) {
    val config = prefsManager.getPlayerConfig()
    val username = prefsManager.getSavedUsername() ?: "Unknown"

    // Game state
    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }

    // New state for assignment requirements
    var roundNumber by remember { mutableStateOf(1) } // Tracks the round number
    var xMoves by remember { mutableStateOf(mutableListOf<Int>()) } // Records X's moves (1-9)

    // Function to save the game record to SQLite when the game ends
    fun saveGameRecord() {
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        // Convert list of moves to a string
        val movesString = xMoves.joinToString(",")
        dbHelper.insertRecord(username, roundNumber, movesString, time)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show winner or current turn
        Text(
            text = if (winner != null) {
                if (winner == "Draw") "Draw!" else "Winner: $winner"
            } else {
                "Round $roundNumber - Turn: ${if (currentPlayer == "X") config.first else config.second}"
            },
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        // The 33 Board
        Column {
            for (i in 0 until 3) {
                Row {
                    for (j in 0 until 3) {
                        val cellNumber = i * 3 + j + 1 // Cell number 1 to 9
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp))
                                .clickable {
                                    if (board[i][j].isEmpty() && winner == null) {
                                        // Update board
                                        board = board.copyOf().apply {
                                            this[i] = this[i].copyOf().apply { this[j] = currentPlayer }
                                        }

                                        // If X plays, record the move number (1-9)
                                        if (currentPlayer == "X") {
                                            xMoves.add(cellNumber)
                                        }

                                        // Check for win or draw
                                        val win = checkWinner(board)
                                        if (win != null) {
                                            winner = win
                                            saveGameRecord() // Save to database immediately
                                        } else {
                                            // Switch player
                                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = board[i][j],
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // clears board, increments round number
        Button(onClick = {
            board = Array(3) { Array(3) { "" } }
            currentPlayer = "X"
            winner = null
            xMoves.clear()
            roundNumber++
        }) {
            Text("Restart Game (Next Round)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to view history (proves database works)
        Button(onClick = onShowHistory) {
            Text("View History")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) {
            Text("Back to Configuration")
        }
    }
}
fun checkWinner(board: Array<Array<String>>): String? {
    for (i in 0 until 3) {
        if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0]
        if (board[0][i] != "" && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i]
    }
    if (board[0][0] != "" && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0]
    if (board[0][2] != "" && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2]
    if (board.flatten().all { it != "" }) return "Draw"
    return null
}