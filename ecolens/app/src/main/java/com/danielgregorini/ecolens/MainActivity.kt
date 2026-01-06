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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.danielgregorini.ecolens.data.FirstLaunchStore
import com.danielgregorini.ecolens.ui.components.CameraPreview
import com.danielgregorini.ecolens.screens.Home

import androidx.compose.runtime.rememberCoroutineScope
import com.danielgregorini.ecolens.screens.Camera
import kotlinx.coroutines.launch


import com.danielgregorini.ecolens.ui.components.Buttons

import com.danielgregorini.ecolens.screens.FAQ
import com.danielgregorini.ecolens.screens.OnBoarding

class MainActivity : ComponentActivity() {
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

    // controla qual "tela" está selecionada
    var selected by remember { mutableStateOf("home") }

    var showFaqFullScreen by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        showFaqFullScreen = FirstLaunchStore.isFirstLaunch(context)
    }

    val scope = rememberCoroutineScope()

    // enquanto não carregou ainda
    if (showFaqFullScreen == null) {
        return
    }

    if (showFaqFullScreen == true) {
        OnBoarding(
            onClose = {
                //  aqui NÃO chama composable, só muda estado
                showFaqFullScreen = false
                selected = "home"


                scope.launch {
                    FirstLaunchStore.setLaunched(context)
                }

                // se precisar chamar função suspend, usa coroutine
                // scope.launch { FirstLaunchStore.setLaunched(context) }
            }
        )
        return
    }

    // essa parte meio que ignora por enquanto
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        /*
       if (hasPermission) {
           //CameraPreview(modifier = Modifier.fillMaxWidth())
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .weight(0.8f)
                      .fillMaxHeight(),

                  contentAlignment = Alignment.Center
              ){
                  Home()
              }

           //ai coloca os botes de mudar menu, camera, FAQ
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .weight(0.2f)
                   .fillMaxHeight(),
               contentAlignment = Alignment.Center
           ){
               Text("BOTAOES")

           }


       } else {
           Spacer(Modifier.height(24.dp))
           Text("Preciso da permissão da câmera para mostrar ao vivo.")
           Spacer(Modifier.height(12.dp))
           Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
               Text("Permitir câmera")
           }
       }
       */

        // aqui nessa box fica a home, faq e a camera, parte principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.85f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

            // AQUI É ONDE VOCÊ VAI COLOCAR OS COMPONENTES DE VERDADE
            // por enquanto só texto pra testar a troca de "telas"
            when (selected) {
                "home" -> {
                    Home()
                }

                "camera" -> {
                    Camera()
                }

                "faq" -> {
                   FAQ()
                }
            }
        }

        // nessa box coloca os botoes de mudar menu, camera, FAQ
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.15f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Buttons(
                onHomeClick = { selected = "home" },
                onCameraClick = {
                    selected = "camera"
                    // depois você coloca a lógica da câmera aqui
                    // ex: launcher.launch(Manifest.permission.CAMERA)
                },
                onFaqClick = { selected = "faq" }
            )
        }
    }
}

