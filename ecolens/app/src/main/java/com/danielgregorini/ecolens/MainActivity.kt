package com.danielgregorini.ecolens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.danielgregorini.ecolens.data.FirstLaunchStore
import com.danielgregorini.ecolens.screens.Camera
import com.danielgregorini.ecolens.screens.FAQ
import com.danielgregorini.ecolens.screens.Home
import com.danielgregorini.ecolens.screens.OnBoarding
import com.danielgregorini.ecolens.ui.components.Buttons
import kotlinx.coroutines.launch

//ONBOARDING sempre aparecer
private const val ALWAYS_SHOW_ONBOARDING = false

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // controle de telas
    var selected by remember { mutableStateOf("home") }

    // controle do onboarding
    var showOnBoarding by remember { mutableStateOf<Boolean?>(null) }

    // decide se mostra onboarding
    LaunchedEffect(Unit) {
        showOnBoarding = if (ALWAYS_SHOW_ONBOARDING) {
            true
        } else {
            FirstLaunchStore.isFirstLaunch(context)
        }
    }

    // enquanto carrega
    if (showOnBoarding == null) return

    // -------- ONBOARDING --------
    if (showOnBoarding == true) {
        OnBoarding(
            onFinish = {
                showOnBoarding = false
                selected = "home"

                if (!ALWAYS_SHOW_ONBOARDING) {
                    scope.launch {
                        FirstLaunchStore.setLaunched(context)
                    }
                }
            }
        )
        return
    }

    // -------- APP NORMAL --------

    // permissão de câmera (guardada para depois)
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // CONTEÚDO PRINCIPAL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.82f),
            contentAlignment = Alignment.Center
        ) {
            when (selected) {
                "home" -> Home()
                "camera" -> Camera()
                "faq" -> FAQ()
            }
        }

        // BOTTOM BUTTONS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.18f),
            contentAlignment = Alignment.Center
        ) {
            Buttons(
                onHomeClick = { selected = "home" },
                onCameraClick = {
                    selected = "camera"
                    // futuramente:
                    // launcher.launch(Manifest.permission.CAMERA)
                },
                onFaqClick = { selected = "faq" }
            )
        }
    }
}
