package com.danielgregorini.ecolens.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class FaqItem(
    val question: String,
    val answer: String
)

@Composable
fun FAQ(
    modifier: Modifier = Modifier
) {
    val faqItems = remember {
        listOf(
            FaqItem(
                question = "Como o EcoLens identifica o tipo de lixo?",
                answer = "O EcoLens usa a câmera do telemóvel e um modelo de IA para analisar a imagem e sugerir a categoria do resíduo."
            ),
            FaqItem(
                question = "O que fazer se o app não reconhecer o lixo?",
                answer = "Tente melhorar a iluminação, aproximar o objeto e evitar fundo muito poluído. Se ainda falhar, tente outro ângulo."
            ),
            FaqItem(
                question = "O EcoLens mostra onde descartar cada tipo de lixo?",
                answer = "A ideia é indicar a categoria e orientar o descarte. Você pode adaptar para regras locais (ecopontos, recicláveis, orgânicos, etc.)."
            ),
            FaqItem(
                question = "É seguro enviar fotos para o app?",
                answer = "Se a classificação for no dispositivo (on-device), a imagem não precisa sair do telemóvel. Se usar cloud, dá para aplicar políticas de privacidade."
            ),
            FaqItem(
                question = "O app funciona para qualquer tipo de lixo?",
                answer = "Funciona melhor para categorias comuns. Itens muito específicos podem ter menor precisão dependendo do dataset/modelo."
            )
        )
    }

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 28.dp)
        ) {

            // Topo: EcoLens (cor primária) e título FAQ
            Text(
                text = "EcoLens",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "FAQ",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black
            )

            Spacer(Modifier.height(18.dp))

            // Lista de perguntas
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(faqItems) { index, item ->
                    val expanded = expandedIndex == index

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedIndex = if (expanded) null else index
                            }
                            .padding(vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.question,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Icon(
                                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        AnimatedVisibility(visible = expanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, end = 28.dp)
                            ) {
                                Text(
                                    text = item.answer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Divider()
                }
            }
        }
    }
}
