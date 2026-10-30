package com.example.padelpoints.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Text
import com.example.padelpoints.presentation.PadelGameViewModel
import kotlinx.coroutines.delay
import java.time.LocalTime

@Composable
fun CounterScreen(
    viewModel: PadelGameViewModel,
    onOpenConfig: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val yellowColor = Color(0xFFFFE500)
    val blackColor = Color(0xFF000000)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blackColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // LADO IZQUIERDO: EQUIPO 1 (Fondo Negro / Texto Amarillo)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(blackColor)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.onSideTapped(1) },
                            onLongPress = { viewModel.onSideLongPressed(1) }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                TeamHalfContent(
                    teamName = "EQ 1",
                    pointsText = state.pointToString(state.team1Points),
                    gamesCount = state.team1Games,
                    isServing = state.servingTeam == 1,
                    textColor = yellowColor,
                    badgeBgColor = yellowColor,
                    badgeTextColor = blackColor
                )
            }

            // LADO DERECHO: EQUIPO 2 (Fondo Amarillo / Texto Negro)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(yellowColor)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.onSideTapped(2) },
                            onLongPress = { viewModel.onSideLongPressed(2) }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                TeamHalfContent(
                    teamName = "EQ 2",
                    pointsText = state.pointToString(state.team2Points),
                    gamesCount = state.team2Games,
                    isServing = state.servingTeam == 2,
                    textColor = blackColor,
                    badgeBgColor = blackColor,
                    badgeTextColor = yellowColor
                )
            }
        }

        // RELOJ ANALÓGICO EN LA PARTE SUPERIOR (AGUJAS BLANCAS SOBRE ESFERA NEGRA)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            AnalogClock()
        }

        // CARTEL "POR EL SAQUE" DEBAJO DEL RELOJ ANALÓGICO
        if (state.servingTeam == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 42.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Red, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "POR EL SAQUE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Mensaje de Ganador
        if (state.winnerTeam != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¡GANÓ EQUIPO ${state.winnerTeam}!",
                        color = yellowColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.resetMatch() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = yellowColor,
                            contentColor = blackColor
                        )
                    ) {
                        Text("Nuevo Partido", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Botón de Configuración (centrado en la parte inferior)
        val buttonBgColor = Color.DarkGray.copy(alpha = 0.9f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(buttonBgColor)
                    .clickable { onOpenConfig() },
                contentAlignment = Alignment.Center
            ) {
                CanvasGearIcon(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    bgColor = buttonBgColor
                )
            }
        }
    }
}

@Composable
private fun AnalogClock(
    modifier: Modifier = Modifier
) {
    var time by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            time = LocalTime.now()
            delay(1000L)
        }
    }

    val hour = time.hour % 12 + time.minute / 60f
    val minute = time.minute + time.second / 60f

    val hourAngle = hour * 30f
    val minuteAngle = minute * 6f

    Box(
        modifier = modifier
            .size(30.dp)
            .background(Color.Black, shape = CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(3.dp)) {
            val c = center
            val r = size.minDimension / 2f

            // Aguja de la hora (corta, gruesa)
            val hourLength = r * 0.55f
            rotate(degrees = hourAngle, pivot = c) {
                drawLine(
                    color = Color.White,
                    start = c,
                    end = Offset(c.x, c.y - hourLength),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Aguja del minuto (larga)
            val minuteLength = r * 0.85f
            rotate(degrees = minuteAngle, pivot = c) {
                drawLine(
                    color = Color.White,
                    start = c,
                    end = Offset(c.x, c.y - minuteLength),
                    strokeWidth = 2.0.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Punto central
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = c
            )
        }
    }
}

@Composable
private fun TeamHalfContent(
    teamName: String,
    pointsText: String,
    gamesCount: Int,
    isServing: Boolean,
    textColor: Color,
    badgeBgColor: Color,
    badgeTextColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 20.dp, start = 2.dp, end = 2.dp)
    ) {
        // ENCABEZADO DEL EQUIPO
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(26.dp)
        ) {
            if (isServing) {
                Box(
                    modifier = Modifier
                        .background(badgeBgColor, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "SAQUE",
                        color = badgeTextColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.height(1.dp))
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = teamName,
                color = textColor.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // PUNTOS DEL JUEGO (TAMAÑO EQUILIBRADO PARA QUE TODO ENTRO EN PANTALLA)
        Text(
            text = pointsText,
            color = textColor,
            fontSize = if (pointsText == "ORO") 32.sp else 58.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // JUEGOS GANADOS (CÍRCULO 36dp 100% VISIBLE DIRECTAMENTE DEBAJO DEL PUNTO)
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(textColor.copy(alpha = 0.22f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$gamesCount",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CanvasGearIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    bgColor: Color = Color.DarkGray
) {
    Canvas(modifier = modifier) {
        val r = size.minDimension / 2f
        val c = center

        // Dientes del engranaje 100% simétricos
        val toothW = r * 0.32f
        val toothH = r * 0.32f
        val toothDist = r * 0.80f

        for (i in 0 until 6) {
            rotate(degrees = i * 60f, pivot = c) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(c.x - toothW / 2f, c.y - toothDist),
                    size = Size(toothW, toothH),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
        }
        // Círculo principal exterior
        drawCircle(color = color, radius = r * 0.65f, center = c)
        // Círculo interior calado (del color de fondo del botón)
        drawCircle(color = bgColor, radius = r * 0.30f, center = c)
    }
}
