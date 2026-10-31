package com.example.padelpoints.presentation

data class PadelGameState(
    val servingTeam: Int? = null, // 1 for Team 1, 2 for Team 2, null for "POR EL SAQUE"
    val team1Points: Int = 0,    // Normal: 0, 1, 2, 3. Tiebreak: 0, 1, 2, 3, 4, 5, 6...
    val team2Points: Int = 0,
    val team1Games: Int = 0,
    val team2Games: Int = 0,
    val maxGames: Int = 4,       // Default 4 games to win set
    val useTiebreak: Boolean = false, // Default false
    val tiebreakDieAt7: Boolean = false, // Default false ("Muere en 7")
    val tiebreakInitialServer: Int? = null, // First server of the tiebreak
    val soundEnabled: Boolean = true,
    val winnerTeam: Int? = null  // 1 or 2 when match finishes
) {
    val isTiebreak: Boolean
        get() = useTiebreak && team1Games == (maxGames - 1) && team2Games == (maxGames - 1) && winnerTeam == null

    val isGoldPoint: Boolean
        get() = !isTiebreak && team1Points == 3 && team2Points == 3

    fun getActiveServingTeam(): Int? {
        val currentServing = servingTeam ?: return null
        if (!isTiebreak) return currentServing
        val initial = tiebreakInitialServer ?: currentServing
        val totalPoints = team1Points + team2Points
        if (totalPoints == 0) return initial
        val index = (totalPoints - 1) / 2
        return if (index % 2 == 0) (3 - initial) else initial
    }

    fun pointToString(points: Int): String {
        if (isTiebreak) return points.toString()
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
        if (isTiebreak) return points.toString()
        return when (points) {
            0 -> "Cero"
            1 -> "Quince"
            2 -> "Treinta"
            3 -> "Cuarenta"
            else -> "Cero"
        }
    }
}
