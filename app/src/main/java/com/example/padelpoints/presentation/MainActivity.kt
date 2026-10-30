package com.example.padelpoints.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.wear.compose.material3.AppScaffold
import com.example.padelpoints.presentation.audio.SoundManager
import com.example.padelpoints.presentation.theme.PadelPointsTheme
import com.example.padelpoints.presentation.ui.ConfigScreen
import com.example.padelpoints.presentation.ui.CounterScreen

class MainActivity : ComponentActivity() {

    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mantener la pantalla encendida durante el partido
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        soundManager = SoundManager(applicationContext)

        setContent {
            PadelPointsTheme {
                AppScaffold(timeText = {}) {
                    PadelApp(soundManager = soundManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.shutdown()
    }
}

@Composable
fun PadelApp(
    soundManager: SoundManager,
    gameViewModel: PadelGameViewModel = remember { PadelGameViewModel() }
) {
    DisposableEffect(soundManager) {
        gameViewModel.soundManager = soundManager
        onDispose {
            gameViewModel.soundManager = null
        }
    }

    var showConfig by remember { mutableStateOf(false) }

    if (showConfig) {
        ConfigScreen(
            viewModel = gameViewModel,
            onClose = { showConfig = false }
        )
    } else {
        CounterScreen(
            viewModel = gameViewModel,
            onOpenConfig = { showConfig = true }
        )
    }
}
