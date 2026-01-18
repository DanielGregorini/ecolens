package com.danielgregorini.ecolens.ml

import android.graphics.*
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val IMAGE_SIZE = 224
private const val BYTES_PER_CHANNEL = 4 // FLOAT32

fun imageProxyToByteBuffer(imageProxy: ImageProxy): ByteBuffer {

    // ImageProxy (YUV) para bitmap
    val bitmap = imageProxyToBitmap(imageProxy)

    // Garante ARGB_8888
    val rgbBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)

    // Resize para 224x224
    val resized = Bitmap.createScaledBitmap(
        rgbBitmap,
        IMAGE_SIZE,
        IMAGE_SIZE,
        true
    )

    // buffer FLOAT32 [1,224,224,3]
    val buffer = ByteBuffer.allocateDirect(
        IMAGE_SIZE * IMAGE_SIZE * 3 * BYTES_PER_CHANNEL
    ).order(ByteOrder.nativeOrder())

    val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
    resized.getPixels(
        pixels,
        0,
        IMAGE_SIZE,
        0,
        0,
        IMAGE_SIZE,
        IMAGE_SIZE
    )

    // 5️⃣ RGB normalizado 0..1 (IGUAL AO TREINO)
    var pixelIndex = 0
    for (y in 0 until IMAGE_SIZE) {
        for (x in 0 until IMAGE_SIZE) {
            val pixel = pixels[pixelIndex++]

            val r = (pixel shr 16 and 0xFF) / 255f
            val g = (pixel shr 8 and 0xFF) / 255f
            val b = (pixel and 0xFF) / 255f

            buffer.putFloat(r)
            buffer.putFloat(g)
            buffer.putFloat(b)
        }
    }

    buffer.rewind()
    return buffer
}

// ===============================
// ImageProxy (YUV_420_888) → Bitmap
// ===============================
private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {

    val yBuffer = imageProxy.planes[0].buffer
    val uBuffer = imageProxy.planes[1].buffer
    val vBuffer = imageProxy.planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(
        nv21,
        ImageFormat.NV21,
        imageProxy.width,
        imageProxy.height,
        null
    )

    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(
        Rect(0, 0, imageProxy.width, imageProxy.height),
        100,
        out
    )

    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
