package com.danielgregorini.ecolens.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danielgregorini.ecolens.R
import kotlinx.coroutines.launch

@Composable
fun OnBoarding(
    onFinish: () -> Unit
) {
    val ecoGreen = Color(0xFF6FCF9B)
    val scope = rememberCoroutineScope()

    val pages = listOf(
        Triple(
            R.drawable.go_green2,
            "Foco em impacto",
            "Contribua para um futuro mais sustentável com pequenas ações diárias."
        ),
        Triple(
            R.drawable.lata_lixo,
            "Foco em praticidade",
            "Aponte, identifique e recicle certo. O EcoLens guia você em segundos."
        ),
        Triple(
            R.drawable.go_green,
            "Foco em comunidade",
            "Junte-se a uma comunidade que faz a diferença para o planeta."
        )
    )

    val pagerState = rememberPagerState { pages.size }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(16.dp))

        // Header
        Text(
            text = "EcoLens",
            color = ecoGreen,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Comece fazendo a diferença",
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(pages[page].first),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = pages[page].second,
                    color = ecoGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = pages[page].third,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Indicators
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                        .background(
                            if (pagerState.currentPage == index) ecoGreen else Color.LightGray,
                            CircleShape
                        )
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = onFinish) {
                Text("Pular", color = ecoGreen)
            }

            Button(
                onClick = {
                    if (pagerState.currentPage == pages.lastIndex) {
                        onFinish()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ecoGreen)
            ) {
                Text(
                    if (pagerState.currentPage == pages.lastIndex) "Começar" else "Próximo",
                    color = Color.White
                )
            }
        }
    }
}
