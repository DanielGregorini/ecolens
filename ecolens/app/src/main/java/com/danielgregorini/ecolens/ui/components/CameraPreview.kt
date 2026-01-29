    package com.danielgregorini.ecolens.ui.components

    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.camera.core.*
    import androidx.camera.lifecycle.ProcessCameraProvider
    import androidx.camera.view.PreviewView
    import androidx.compose.runtime.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.viewinterop.AndroidView
    import androidx.core.content.ContextCompat
    import androidx.lifecycle.compose.LocalLifecycleOwner
    import com.danielgregorini.ecolens.ml.ClassificationResult
    import com.danielgregorini.ecolens.ml.TFLiteClassifier
    import com.danielgregorini.ecolens.ml.imageProxyToByteBuffer
    import java.util.concurrent.Executors

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CameraPreview(
        modifier: Modifier = Modifier,
        onResults: (List<ClassificationResult>) -> Unit
    ) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
,
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

            val listener = Runnable {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analysis.setAnalyzer(executor) { imageProxy ->
                    try {
                        val input = imageProxyToByteBuffer(imageProxy)
                        val results = classifier.classifyAll(input)
                        onResults(results)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        imageProxy.close()
                    }
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            }

            cameraProviderFuture.addListener(listener, mainExecutor)

            onDispose {
                cameraProviderFuture.get().unbindAll()
                executor.shutdown()
            }
        }

        AndroidView(
            factory = { previewView },
            modifier = modifier
        )
    }
