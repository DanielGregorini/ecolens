package com.danielgregorini.ecolens.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.danielgregorini.ecolens.R

@Composable
fun Buttons(
    onHomeClick: () -> Unit,
    onCameraClick: () -> Unit,
    onFaqClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ecoGreen = Color(0xFF6FCF9B)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // HOME
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home",
            tint = ecoGreen,
            modifier = Modifier
                .size(28.dp)
                .clickable { onHomeClick() }
        )

        Spacer(modifier = Modifier.width(70.dp))

        // CAMERA (círculo grande)
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(ecoGreen)
                .clickable { onCameraClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Camera",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(70.dp))

        // FAQ
        Icon(
            painter = painterResource(id = R.drawable.faq),
            contentDescription = "FAQ",
            tint = ecoGreen,
            modifier = Modifier
                .size(28.dp)
                .clickable { onFaqClick() }
        )
    }
}
