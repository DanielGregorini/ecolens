package com.danielgregorini.ecolens.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.danielgregorini.ecolens.ml.ClassificationResult
import com.danielgregorini.ecolens.ml.wasteMap
import com.danielgregorini.ecolens.ui.components.CameraPreview

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Camera() {

    var results by remember { mutableStateOf<List<ClassificationResult>>(emptyList()) }
    var debugMode by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onResults = { results = it }
        )

        // 🐞 BOTÃO DEBUG
        Text(
            text = if (debugMode) "DEBUG ON" else "DEBUG OFF",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { debugMode = !debugMode },
            color = Color.White
        )

        // 🧠 RESULTADOS
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
                .padding(16.dp)
        ) {

            if (results.isEmpty()) {
                Text("Analisando...", style = MaterialTheme.typography.titleMedium)
            } else {

                if (debugMode) {
                    // 🐞 MODO DEBUG → TODAS AS CLASSES
                    Column {
                        results.forEach {
                            Text(
                                text = "${it.label}: ${(it.confidence * 100).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    // ✅ MODO NORMAL → MELHOR RESULTADO
                    val best = results.first()
                    val info = wasteMap[best.label]

                    Column {
                        Text(
                            text = "Detectado:",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = info?.pt ?: best.label,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "(${best.label})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Descartar em:",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = info?.bin ?: "Lixeira desconhecida",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
