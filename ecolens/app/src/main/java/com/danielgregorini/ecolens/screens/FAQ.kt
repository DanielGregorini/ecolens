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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielgregorini.ecolens.ui.theme.AnswerColor
import com.danielgregorini.ecolens.ui.theme.DividerColor
import com.danielgregorini.ecolens.ui.theme.EcoGreen

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
                answer = "A ideia é indicar a categoria e orientar o descarte. Você pode adaptar para regras locais."
            ),
            FaqItem(
                question = "É seguro enviar fotos para o app?",
                answer = "Se a classificação for no dispositivo, a imagem não sai do telemóvel. Em cloud, políticas de privacidade podem ser aplicadas."
            ),
            FaqItem(
                question = "O app funciona para qualquer tipo de lixo?",
                answer = "Funciona melhor para categorias comuns. Itens muito específicos podem ter menor precisão."
            )
        )
    }

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp)
        ) {

            // Logo / Nome
            Text(
                text = "EcoLens",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = EcoGreen
            )

            Spacer(Modifier.height(16.dp))

            // Título
            Text(
                text = "FAQ",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )

            Spacer(Modifier.height(24.dp))

            LazyColumn {
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.question,
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = EcoGreen
                            )

                            Icon(
                                imageVector = if (expanded)
                                    Icons.Filled.KeyboardArrowUp
                                else
                                    Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = EcoGreen
                            )
                        }

                        AnimatedVisibility(visible = expanded) {
                            Text(
                                text = item.answer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, end = 32.dp),
                                fontSize = 14.sp,
                                color = AnswerColor
                            )
                        }
                    }

                    Divider(
                        color = DividerColor,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}
