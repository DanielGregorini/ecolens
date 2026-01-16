package com.danielgregorini.ecolens.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielgregorini.ecolens.R
import com.danielgregorini.ecolens.ui.theme.EcoGreen

@Composable
fun Home() {

    var selectedCategory by remember { mutableStateOf<WasteCategory?>(null) }

    if (selectedCategory == null) {
        HomeContent(onCategoryClick = { selectedCategory = it })
    } else {
        CategoryDetail(
            category = selectedCategory!!,
            onBack = { selectedCategory = null }
        )
    }
}

//////////////////////////////////////////////////////////
// HOME PRINCIPAL
//////////////////////////////////////////////////////////

@Composable
private fun HomeContent(
    onCategoryClick: (WasteCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {

        Text(
            text = "EcoLens",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            color = EcoGreen
        )

        Spacer(Modifier.height(20.dp))

        // CARD PRINCIPAL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    color = EcoGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Aponte a câmera e\n" +
                            "descubra onde\n" +
                            "descartar o lixo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(R.drawable.jogando_lixo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "Categorias de resíduos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem(WasteCategory.PAPER, onCategoryClick)
            CategoryItem(WasteCategory.PLASTIC, onCategoryClick)
            CategoryItem(WasteCategory.GLASS, onCategoryClick)
            CategoryItem(WasteCategory.ORGANIC, onCategoryClick)
        }
    }
}

//////////////////////////////////////////////////////////
// ITEM DE CATEGORIA
//////////////////////////////////////////////////////////

@Composable
private fun CategoryItem(
    category: WasteCategory,
    onClick: (WasteCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick(category) }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    category.background.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(category.icon),
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = category.label,
            fontSize = 13.sp
        )
    }
}

//////////////////////////////////////////////////////////
// TELA DE DETALHE (DENTRO DO COMPONENTE)
//////////////////////////////////////////////////////////

@Composable
private fun CategoryDetail(
    category: WasteCategory,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(category.background.copy(alpha = 0.25f))
            .padding(20.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
        }

        Spacer(Modifier.height(20.dp))

        Image(
            painter = painterResource(category.icon),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = category.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = category.subtitle,
            color = Color.DarkGray
        )

        Spacer(Modifier.height(16.dp))

        category.items.forEach {
            Text("• $it")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Observação: ${category.note}",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

//////////////////////////////////////////////////////////
// MODELO DE DADOS
//////////////////////////////////////////////////////////

private enum class WasteCategory(
    val label: String,
    val title: String,
    val subtitle: String,
    val icon: Int,
    val background: Color,
    val items: List<String>,
    val note: String
) {
    PAPER(
        label = "Papel",
        title = "Papel e cartão",
        subtitle = "Lixo azul",
        icon = R.drawable.blue,
        background = Color(0xFFB3E5FC),
        items = listOf(
            "Jornais",
            "Revistas",
            "Caixas de cartão",
            "Papel de escrita",
            "Embalagens de papel/cartão"
        ),
        note = "Não inclui papel engordurado, guardanapos sujos ou fraldas."
    ),
    PLASTIC(
        label = "Plástico",
        title = "Plástico e metal",
        subtitle = "Lixo amarelo",
        icon = R.drawable.yellow,
        background = Color(0xFFFFF3CD),
        items = listOf(
            "Garrafas de plástico",
            "Latas",
            "Embalagens de iogurte",
            "Pacotes de leite"
        ),
        note = "Esvazie as embalagens antes de descartar."
    ),
    GLASS(
        label = "Vidro e Metal",
        title = "Vidro Metal",
        subtitle = "Lixo verde",
        icon = R.drawable.green,
        background = Color(0xFFC8E6C9),
        items = listOf(
            "Garrafas",
            "Frascos",
            "Latas"
        ),
        note = "Não inclui espelhos, loiça ou cerâmica."
    ),
    ORGANIC(
        label = "Orgânico",
        title = "Orgânico",
        subtitle = "Lixo castanho",
        icon = R.drawable.brown,
        background = Color(0xFFD7CCC8),
        items = listOf(
            "Restos de comida",
            "Cascas de frutas",
            "Borras de café"
        ),
        note = "Disponível apenas onde há recolha orgânica."
    )
}
