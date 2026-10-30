package com.example.padelpoints.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.example.padelpoints.presentation.PadelGameViewModel

@Composable
fun ConfigScreen(
    viewModel: PadelGameViewModel,
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberTransformingLazyColumnState()

    val yellowColor = Color(0xFFFFE500)
    val blackColor = Color(0xFF000000)

    ScreenScaffold(scrollState = listState) { contentPadding ->
        TransformingLazyColumn(
            contentPadding = contentPadding,
            state = listState,
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
        ) {
            item {
                ListHeader {
                    Text(
                        text = "Configuración",
                        color = yellowColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
            }

            // 1. REINICIAR PARTIDO
            item {
                Button(
                    onClick = {
                        viewModel.resetMatch()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Reiniciar Partido",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // 2. JUEGOS MÁXIMOS POR SET (3, 4, 5, 6 con Cápsula Elegante)
            item {
                Text(
                    text = "Juegos por set",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                    textAlign = TextAlign.Center
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color(0xFF2C2C2E), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón Menos (-)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (state.maxGames > 3) Color.DarkGray else Color.Transparent)
                                .clickable(enabled = state.maxGames > 3) {
                                    viewModel.setMaxGames(state.maxGames - 1)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "−",
                                color = if (state.maxGames > 3) Color.White else Color.Gray.copy(alpha = 0.4f),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Valor actual de juegos
                        Text(
                            text = "${state.maxGames} Juegos",
                            color = yellowColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )

                        // Botón Más (+)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (state.maxGames < 6) Color.DarkGray else Color.Transparent)
                                .clickable(enabled = state.maxGames < 6) {
                                    viewModel.setMaxGames(state.maxGames + 1)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                color = if (state.maxGames < 6) Color.White else Color.Gray.copy(alpha = 0.4f),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 3. SONIDO (SÍ / NO)
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = { viewModel.toggleSound(!state.soundEnabled) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.soundEnabled) Color(0xFF2E7D32) else Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (state.soundEnabled) "Sonido: SÍ" else "Sonido: NO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // 4. VOLVER AL PARTIDO
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = yellowColor,
                        contentColor = blackColor
                    )
                ) {
                    Text("Volver", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
