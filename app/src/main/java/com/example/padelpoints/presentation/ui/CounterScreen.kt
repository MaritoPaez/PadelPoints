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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Text
import com.example.padelpoints.presentation.PadelGameState
import com.example.padelpoints.presentation.PadelGameViewModel
import kotlinx.coroutines.delay
import java.time.LocalTime

@Composable
fun CounterScreen(
    viewModel: PadelGameViewModel,
    onOpenConfig: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    CounterScreenContent(
        state = state,
        onSideTapped = { side -> viewModel.onSideTapped(side) },
        onSideLongPressed = { side -> viewModel.onSideLongPressed(side) },
        onResetMatch = { viewModel.resetMatch() },
        onOpenConfig = onOpenConfig
    )
}

@Composable
fun CounterScreenContent(
    state: PadelGameState,
    onSideTapped: (Int) -> Unit,
    onSideLongPressed: (Int) -> Unit,
    onResetMatch: () -> Unit,
    onOpenConfig: () -> Unit
) {
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
                            onTap = { onSideTapped(1) },
                            onLongPress = { onSideLongPressed(1) }
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
                            onTap = { onSideTapped(2) },
                            onLongPress = { onSideLongPressed(2) }
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

        // RELOJ ANALÓGICO MÁS GRANDE EN LA PARTE SUPERIOR (SUPERPOSICIÓN SUTIL EN LA PARTE SUPERIOR)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            AnalogClock(size = 44.dp)
        }

        // CARTEL "POR EL SAQUE" DEBAJO DEL RELOJ ANALÓGICO
        if (state.servingTeam == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Red, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "POR EL SAQUE",
                        color = Color.White,
                        fontSize = 10.sp,
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
                        onClick = onResetMatch,
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
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
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
            .size(size)
            .background(Color.Black, shape = CircleShape)
            .border(1.5.dp, Color.White.copy(alpha = 0.85f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            val c = center
            val r = this.size.minDimension / 2f

            // Aguja de la hora (corta, gruesa)
            val hourLength = r * 0.55f
            rotate(degrees = hourAngle, pivot = c) {
                drawLine(
                    color = Color.White,
                    start = c,
                    end = Offset(c.x, c.y - hourLength),
                    strokeWidth = 3.dp.toPx(),
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
                    strokeWidth = 2.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Punto central
            drawCircle(
                color = Color.White,
                radius = 2.5.dp.toPx(),
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
            .padding(top = 14.dp, bottom = 16.dp, start = 2.dp, end = 2.dp)
    ) {
        // ENCABEZADO DEL EQUIPO (SI SACA, MUESTRA SOLO 'SAQUE'; SI NO, MUESTRA SOLO 'EQ 1' / 'EQ 2')
        Box(
            modifier = Modifier.height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isServing) {
                Box(
                    modifier = Modifier
                        .background(badgeBgColor, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SAQUE",
                        color = badgeTextColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                Text(
                    text = teamName,
                    color = textColor.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // PUNTOS DEL JUEGO
        Text(
            text = pointsText,
            color = textColor,
            fontSize = if (pointsText == "ORO") 32.sp else 58.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        // JUEGOS GANADOS (CÍRCULO 48dp CON NÚMERO 30sp Y SUPERPOSICIÓN SUTIL EN LOS TANTOS)
        Box(
            modifier = Modifier
                .offset(y = (-8).dp)
                .size(48.dp)
                .background(textColor.copy(alpha = 0.28f), shape = CircleShape)
                .border(1.dp, textColor.copy(alpha = 0.45f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gamesCount.toString(),
                color = textColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
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

@Preview(name = "In Game", widthDp = 200, heightDp = 200, showBackground = true)
@Composable
private fun CounterScreenInGamePreview() {
    CounterScreenContent(
        state = PadelGameState(
            servingTeam = 1,
            team1Points = 2, // 30
            team2Points = 1, // 15
            team1Games = 3,
            team2Games = 2
        ),
        onSideTapped = {},
        onSideLongPressed = {},
        onResetMatch = {},
        onOpenConfig = {}
    )
}

@Preview(name = "Por el Saque", widthDp = 200, heightDp = 200, showBackground = true)
@Composable
private fun CounterScreenPorElSaquePreview() {
    CounterScreenContent(
        state = PadelGameState(
            servingTeam = null,
            team1Points = 0,
            team2Points = 0,
            team1Games = 0,
            team2Games = 0
        ),
        onSideTapped = {},
        onSideLongPressed = {},
        onResetMatch = {},
        onOpenConfig = {}
    )
}

