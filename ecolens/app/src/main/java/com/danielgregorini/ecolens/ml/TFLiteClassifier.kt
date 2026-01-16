package com.danielgregorini.ecolens.ml

import android.content.Context
import org.json.JSONArray
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer

class TFLiteClassifier(context: Context) {

    private val interpreter: Interpreter
    private val labels: List<String>

    init {
        // Modelo
        val model = FileUtil.loadMappedFile(context, "model.tflite")
        interpreter = Interpreter(model)

        // 🔥 LER JSON CORRETAMENTE
        labels = loadLabelsFromJson(context)
    }

    private fun loadLabelsFromJson(context: Context): List<String> {
        val json = context.assets
            .open("labels.json")
            .bufferedReader()
            .use { it.readText() }

        val array = JSONArray(json)
        return List(array.length()) { i ->
            array.getString(i)
        }
    }

    fun classifyAll(input: ByteBuffer): List<ClassificationResult> {
        val output = Array(1) { FloatArray(labels.size) }

        interpreter.run(input, output)

        return labels.mapIndexed { index, label ->
            ClassificationResult(
                label = label,
                confidence = output[0][index]
            )
        }.sortedByDescending { it.confidence }
    }
}
