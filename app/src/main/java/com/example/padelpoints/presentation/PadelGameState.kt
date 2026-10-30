package com.example.padelpoints.presentation

data class PadelGameState(
    val servingTeam: Int? = null, // 1 for Team 1, 2 for Team 2, null for "POR EL SAQUE"
    val team1Points: Int = 0,    // 0 -> 0, 1 -> 15, 2 -> 30, 3 -> 40
    val team2Points: Int = 0,
    val team1Games: Int = 0,
    val team2Games: Int = 0,
    val maxGames: Int = 4,       // Default 4 games to win set
    val soundEnabled: Boolean = true,
    val winnerTeam: Int? = null  // 1 or 2 when match finishes
) {
    val isGoldPoint: Boolean
        get() = team1Points == 3 && team2Points == 3

    fun pointToString(points: Int): String {
        if (isGoldPoint) return "ORO"
        return when (points) {
            0 -> "0"
            1 -> "15"
            2 -> "30"
            3 -> "40"
            else -> "0"
        }
    }

    fun pointToSpokenWord(points: Int): String {
        return when (points) {
            0 -> "Cero"
            1 -> "Quince"
            2 -> "Treinta"
            3 -> "Cuarenta"
            else -> "Cero"
        }
    }
}
