package com.danielgregorini.ecolens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.danielgregorini.ecolens.ui.components.CameraPreview
import com.danielgregorini.ecolens.screens.Home


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

@Composable
fun MainScreen() {
    val context = LocalContext.current

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

        //aqui nessa box fica a home, faq e a camera, parte principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.85f)
                .fillMaxHeight(),

            contentAlignment = Alignment.Center
        ){
            Home()
        }

        //nessa box coloca os botes de mudar menu, camera, FAQ
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.15f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ){
            Text("aqui fica os botes")
        }
    }
}
