package com.example.chitaysostav.presentation.edit

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

private const val SCAN_INTERVAL_MS = 500L

@Composable
fun OcrCameraScreen(
    onTextRecognized: (String) -> Unit,
    onCameraReady: () -> Unit = {},
    onClose: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isProcessing = remember { AtomicBoolean(false) }
    val lastScanTime = remember { AtomicLong(0L) }

    var recognizedText by remember { mutableStateOf("") }
    val recognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        BackHandler {
            onClose()
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                previewView.previewStreamState.observe(lifecycleOwner) { state ->
                    if (state == PreviewView.StreamState.STREAMING) {
                        onCameraReady()
                    }
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    analysis.setAnalyzer(executor) { imageProxy ->
                        processImage(
                            imageProxy,
                            recognizer,
                            isProcessing,
                            lastScanTime
                        ) { text ->
                            recognizedText = text
                        }
                    }
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )
                }, executor)
                previewView
            }
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 20.dp, end = 20.dp)
                .size(36.dp)
                .background(Color(0x66808080), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClose, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Закрыть камеру",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(Color(0xAA000000))
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = recognizedText,
                color = Color.White
            )
            if (recognizedText.isNotEmpty()) {
                Button(onClick = { onTextRecognized(recognizedText) }) {
                    Text(text = "Сохранить")
                }
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun processImage(
    imageProxy: ImageProxy,
    recognizer: TextRecognizer,
    isProcessing: AtomicBoolean,
    lastScanTime: AtomicLong,
    onDetected: (String) -> Unit
) {
    val now = System.currentTimeMillis()
    if (now - lastScanTime.get() < SCAN_INTERVAL_MS) {
        imageProxy.close()
        return
    }
    if (!isProcessing.compareAndSet(false, true)) {
        imageProxy.close()
        return
    }
    val mediaImage = imageProxy.image ?: run {
        isProcessing.set(false)
        imageProxy.close()
        return
    }
    lastScanTime.set(now)
    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
    recognizer.process(inputImage)
        .addOnSuccessListener { result ->
            onDetected(result.text)
        }
        .addOnCompleteListener {
            isProcessing.set(false)
            imageProxy.close()
        }
}