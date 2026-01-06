package com.danielgregorini.ecolens.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    paddingDp: Int = 16
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    DisposableEffect(lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        val listener = Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { p ->
                p.setSurfaceProvider(previewView.surfaceProvider)
            }

            val selector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview)
            } catch (_: Exception) {
                //erro
            }
        }

        cameraProviderFuture.addListener(listener, executor)

        onDispose {
            // libera a câmera ao sair
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)   //70% da tela
            .padding(paddingDp.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }

}