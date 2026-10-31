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

        // TIEBREAK ACTIVO
        if (currentState.isTiebreak) {
            val initialServer = currentState.tiebreakInitialServer ?: (currentState.servingTeam ?: team)
            val newT1Points = if (team == 1) currentState.team1Points + 1 else currentState.team1Points
            val newT2Points = if (team == 2) currentState.team2Points + 1 else currentState.team2Points

            val winner = if (currentState.tiebreakDieAt7) {
                if (newT1Points >= 7) 1
                else if (newT2Points >= 7) 2
                else null
            } else {
                if (newT1Points >= 7 && (newT1Points - newT2Points) >= 2) 1
                else if (newT2Points >= 7 && (newT2Points - newT1Points) >= 2) 2
                else null
            }

            if (winner != null) {
                val newT1Games = if (winner == 1) currentState.team1Games + 1 else currentState.team1Games
                val newT2Games = if (winner == 2) currentState.team2Games + 1 else currentState.team2Games

                val newState = currentState.copy(
                    team1Points = newT1Points,
                    team2Points = newT2Points,
                    team1Games = newT1Games,
                    team2Games = newT2Games,
                    winnerTeam = winner,
                    tiebreakInitialServer = initialServer
                )
                _state.value = newState
                soundManager?.playSoundOrSpeak("partido_equipo_$winner", "¡Ganó Equipo $winner el tiebreak y el partido!")
            } else {
                val tempState = currentState.copy(
                    team1Points = newT1Points,
                    team2Points = newT2Points,
                    tiebreakInitialServer = initialServer
                )
                val nextServer = tempState.getActiveServingTeam() ?: currentState.servingTeam
                val newState = tempState.copy(servingTeam = nextServer)
                _state.value = newState
                announceScore(newState)
            }
            return
        }

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

        val hasWinner = if (currentState.useTiebreak) {
            if (winningTeam == 1) newT1Games >= currentState.maxGames else newT2Games >= currentState.maxGames
        } else {
            if (winningTeam == 1) {
                newT1Games >= currentState.maxGames && (newT1Games - newT2Games >= 2)
            } else {
                newT2Games >= currentState.maxGames && (newT2Games - newT1Games >= 2)
            }
        }

        val isNextGameTiebreak = currentState.useTiebreak && !hasWinner &&
                newT1Games == (currentState.maxGames - 1) && newT2Games == (currentState.maxGames - 1)

        val newState = currentState.copy(
            team1Points = 0,
            team2Points = 0,
            team1Games = newT1Games,
            team2Games = newT2Games,
            servingTeam = if (hasWinner) currentState.servingTeam else nextServingTeam,
            tiebreakInitialServer = if (isNextGameTiebreak) nextServingTeam else null,
            winnerTeam = if (hasWinner) winningTeam else null
        )

        _state.value = newState

        if (hasWinner) {
            soundManager?.playSoundOrSpeak("partido_equipo_$winningTeam", "Partido para Equipo $winningTeam")
        } else if (isNextGameTiebreak) {
            soundManager?.playSoundOrSpeak("juego_equipo_$winningTeam", "Juego Equipo $winningTeam. Tiebreak, saca Equipo $nextServingTeam")
        } else {
            soundManager?.playSoundOrSpeak("juego_equipo_$winningTeam", "Juego Equipo $winningTeam. Saca Equipo $nextServingTeam")
        }
    }

    private fun announceScore(state: PadelGameState) {
        if (state.isTiebreak) {
            val servingTeam = state.getActiveServingTeam() ?: return
            val serverPoints = if (servingTeam == 1) state.team1Points else state.team2Points
            val receiverPoints = if (servingTeam == 1) state.team2Points else state.team1Points

            val soundKey = "tiebreak_puntos_${serverPoints}_$receiverPoints"
            val fallbackText = if (serverPoints == receiverPoints) {
                "$serverPoints iguales"
            } else {
                "$serverPoints - $receiverPoints"
            }

            soundManager?.playSoundOrSpeak(soundKey, fallbackText)
            return
        }

        if (state.isGoldPoint) {
            soundManager?.playSoundOrSpeak("punto_de_oro", "Punto de Oro")
            return
        }

        val servingTeam = state.servingTeam ?: return
        val serverPoints = if (servingTeam == 1) state.team1Points else state.team2Points
        val receiverPoints = if (servingTeam == 1) state.team2Points else state.team1Points

        val serverWord = state.pointToSpokenWord(serverPoints)
        val receiverWord = state.pointToSpokenWord(receiverPoints)

        val soundKey = "puntos_${pointsToScoreTag(serverPoints)}_${pointsToScoreTag(receiverPoints)}"

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
            useTiebreak = currentState.useTiebreak,
            tiebreakDieAt7 = currentState.tiebreakDieAt7,
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

    fun toggleTiebreak(enabled: Boolean) {
        val newState = _state.value.copy(useTiebreak = enabled)
        _state.value = newState
        if (enabled) {
            soundManager?.playSoundOrSpeak("tiebreak_activado", "Tiebreak activado")
        } else {
            soundManager?.playSoundOrSpeak("tiebreak_desactivado", "Tiebreak desactivado")
        }
    }

    fun toggleTiebreakDieAt7(enabled: Boolean) {
        val newState = _state.value.copy(tiebreakDieAt7 = enabled)
        _state.value = newState
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
