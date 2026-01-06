package com.danielgregorini.ecolens.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Buttons(
    onHomeClick: () -> Unit,
    onCameraClick: () -> Unit,
    onFaqClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onHomeClick) { Text("Home") }
        Button(onClick = onCameraClick) { Text("Camera") }
        Button(onClick = onFaqClick) { Text("FAQ") }
    }
}
