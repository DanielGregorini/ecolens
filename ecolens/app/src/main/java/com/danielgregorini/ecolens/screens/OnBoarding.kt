package com.danielgregorini.ecolens.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnBoarding(
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ON ASDIDJ - Bem-vindo ao EcoLens")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onClose) {
            Text("Fechar")
        }
    }
}
