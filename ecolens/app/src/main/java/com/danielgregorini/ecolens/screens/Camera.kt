package com.danielgregorini.ecolens.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.danielgregorini.ecolens.ml.ClassificationResult
import com.danielgregorini.ecolens.ml.TFLiteClassifier
import com.danielgregorini.ecolens.ml.imageProxyToByteBuffer
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Camera() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ================= PERMISSÃO =================
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Permissão da câmera necessária")
        }
        return
    }

    // ================= ESTADO DO TEXTO =================
    var statusText by remember {
        mutableStateOf("Analisando...")
    }

    val classifier = remember { TFLiteClassifier(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val mainExecutor = ContextCompat.getMainExecutor(context)

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    DisposableEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        var lastAnalyzedTime = 0L

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(executor) { imageProxy ->
                val now = System.currentTimeMillis()

                //tempo que roda a IA
                if (now - lastAnalyzedTime > 700) {
                    lastAnalyzedTime = now
                    try {
                        val input = imageProxyToByteBuffer(imageProxy)
                        val results = classifier.classifyAll(input)

                        val best = results.maxByOrNull { it.confidence }

                        statusText = if (best != null) {
                            "${best.label} (${(best.confidence * 100).toInt()}%)"
                        } else {
                            "Analisando..."
                        }

                    } catch (e: Exception) {
                        statusText = "Erro ao analisar"
                        e.printStackTrace()
                    }
                }

                imageProxy.close()
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )
        }, mainExecutor)

        onDispose {
            cameraProviderFuture.get().unbindAll()
            executor.shutdown()
        }
    }

    // ================= UI =================
    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // 🔤 TEXTO ÚNICO SEMPRE VISÍVEL
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
