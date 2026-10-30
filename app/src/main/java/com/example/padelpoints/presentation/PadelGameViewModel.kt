package com.example.padelpoints.presentation

import androidx.lifecycle.ViewModel
import com.example.padelpoints.presentation.audio.SoundManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PadelGameViewModel : ViewModel() {

    private val _state = MutableStateFlow(PadelGameState())
    val state: StateFlow<PadelGameState> = _state.asStateFlow()

    private val history = ArrayDeque<PadelGameState>()

    var soundManager: SoundManager? = null

    fun onSideTapped(team: Int) {
        val currentState = _state.value

        if (currentState.winnerTeam != null) {
            soundManager?.playSoundOrSpeak("partido_finalizado", "Partido finalizado. Reinicia en configuración.")
            return
        }

        // Selección inicial de saque "POR EL SAQUE"
        if (currentState.servingTeam == null) {
            pushHistory(currentState)
            val newState = currentState.copy(servingTeam = team)
            _state.value = newState
            soundManager?.playSoundOrSpeak("saca_equipo_$team", "Saca Equipo $team")
            return
        }

        pushHistory(currentState)

        val isGold = currentState.isGoldPoint

        if (isGold) {
            // El que gana el Punto de Oro gana el juego
            winGame(team, currentState)
            return
        }

        if (team == 1) {
            if (currentState.team1Points == 3 && currentState.team2Points < 3) {
                // Ganó el juego Equipo 1
                winGame(1, currentState)
            } else {
                val newT1Points = currentState.team1Points + 1
                val newState = currentState.copy(team1Points = newT1Points)
                _state.value = newState
                announceScore(newState)
            }
        } else {
            if (currentState.team2Points == 3 && currentState.team1Points < 3) {
                // Ganó el juego Equipo 2
                winGame(2, currentState)
            } else {
                val newT2Points = currentState.team2Points + 1
                val newState = currentState.copy(team2Points = newT2Points)
                _state.value = newState
                announceScore(newState)
            }
        }
    }

    private fun winGame(winningTeam: Int, currentState: PadelGameState) {
        val newT1Games = if (winningTeam == 1) currentState.team1Games + 1 else currentState.team1Games
        val newT2Games = if (winningTeam == 2) currentState.team2Games + 1 else currentState.team2Games

        val nextServingTeam = if (currentState.servingTeam == 1) 2 else 1

        val hasWinner = if (winningTeam == 1) {
            newT1Games >= currentState.maxGames
        } else {
            newT2Games >= currentState.maxGames
        }

        val newState = currentState.copy(
            team1Points = 0,
            team2Points = 0,
            team1Games = newT1Games,
            team2Games = newT2Games,
            servingTeam = if (hasWinner) currentState.servingTeam else nextServingTeam,
            winnerTeam = if (hasWinner) winningTeam else null
        )

        _state.value = newState

        if (hasWinner) {
            soundManager?.playSoundOrSpeak("partido_equipo_$winningTeam", "Partido para Equipo $winningTeam")
        } else {
            soundManager?.playSoundOrSpeak("juego_equipo_$winningTeam", "Juego Equipo $winningTeam. Saca Equipo $nextServingTeam")
        }
    }

    private fun announceScore(state: PadelGameState) {
        if (state.isGoldPoint) {
            soundManager?.playSoundOrSpeak("punto_de_oro", "Punto de Oro")
            return
        }

        val servingTeam = state.servingTeam ?: return
        val serverPoints = if (servingTeam == 1) state.team1Points else state.team2Points
        val receiverPoints = if (servingTeam == 1) state.team2Points else state.team1Points

        val serverWord = state.pointToSpokenWord(serverPoints)
        val receiverWord = state.pointToSpokenWord(receiverPoints)

        val soundKey = if (serverPoints == receiverPoints) {
            "puntos_${pointsToScoreTag(serverPoints)}_${pointsToScoreTag(receiverPoints)}"
        } else {
            "puntos_${pointsToScoreTag(serverPoints)}_${pointsToScoreTag(receiverPoints)}"
        }

        val fallbackText = if (serverPoints == receiverPoints) {
            "$serverWord iguales"
        } else {
            "$serverWord - $receiverWord"
        }

        soundManager?.playSoundOrSpeak(soundKey, fallbackText)
    }

    private fun pointsToScoreTag(points: Int): String {
        return when (points) {
            0 -> "0"
            1 -> "15"
            2 -> "30"
            3 -> "40"
            else -> "0"
        }
    }

    fun onSideLongPressed(team: Int) {
        if (history.isNotEmpty()) {
            val previousState = history.removeLast()
            _state.value = previousState
            soundManager?.playSoundOrSpeak("punto_anulado", "Punto anulado")
        } else {
            soundManager?.playSoundOrSpeak("no_acciones", "No hay acciones para deshacer")
        }
    }

    fun resetMatch() {
        history.clear()
        val currentState = _state.value
        val newState = PadelGameState(
            maxGames = currentState.maxGames,
            soundEnabled = currentState.soundEnabled
        )
        _state.value = newState
        soundManager?.playSoundOrSpeak("partido_reiniciado", "Partido reiniciado")
    }

    fun setMaxGames(games: Int) {
        val newState = _state.value.copy(maxGames = games)
        _state.value = newState
        soundManager?.playSoundOrSpeak("a_$games", "A $games")
    }

    fun toggleSound(enabled: Boolean) {
        val newState = _state.value.copy(soundEnabled = enabled)
        _state.value = newState
        soundManager?.isEnabled = enabled
        if (enabled) {
            soundManager?.playSoundOrSpeak("sonido_activado", "Sonido activado")
        }
    }

    private fun pushHistory(state: PadelGameState) {
        if (history.size >= 20) {
            history.removeFirst()
        }
        history.addLast(state)
    }
}
