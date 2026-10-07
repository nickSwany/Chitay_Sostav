package com.example.chitaysostav.presentation.scan

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(
    onBarcodeDetected: (String) -> Unit,
    onClose: () -> Unit
) {
    var cameraReady by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        BackHandler { onClose() }

        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onBarcodeDetected = onBarcodeDetected,
            onCameraReady = { cameraReady = true }
        )

        AnimatedVisibility(visible = cameraReady, enter = fadeIn()) {
            ScannerOverlay(modifier = Modifier.fillMaxSize())
        }

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

        AnimatedVisibility(
            visible = cameraReady,
            enter = fadeIn(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Наведите штрихкод в рамку",
                modifier = Modifier.padding(bottom = 48.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ScannerOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val sidePadding = 20.dp.toPx()
        val cornerLength = 32.dp.toPx()
        val borderWidth = 3.dp.toPx()
        val overlayColor = Color(0x88000000)
        val borderColor = Color.White

        val rectLeft   = sidePadding
        val rectRight  = size.width - sidePadding
        val rectWidth  = rectRight - rectLeft
        val rectHeight = rectWidth * 0.55f
        val rectTop    = (size.height - rectHeight) / 2f
        val rectBottom = rectTop + rectHeight

        drawRect(overlayColor, Offset.Zero, Size(size.width, rectTop))
        drawRect(overlayColor, Offset(0f, rectBottom), Size(size.width, size.height - rectBottom))
        drawRect(overlayColor, Offset(0f, rectTop), Size(rectLeft, rectHeight))
        drawRect(overlayColor, Offset(rectRight, rectTop), Size(size.width - rectRight, rectHeight))

        drawLine(borderColor, Offset(rectLeft, rectTop), Offset(rectLeft + cornerLength, rectTop), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectLeft, rectTop), Offset(rectLeft, rectTop + cornerLength), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectRight - cornerLength, rectTop), Offset(rectRight, rectTop), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectRight, rectTop), Offset(rectRight, rectTop + cornerLength), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectLeft, rectBottom), Offset(rectLeft + cornerLength, rectBottom), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectLeft, rectBottom - cornerLength), Offset(rectLeft, rectBottom), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectRight - cornerLength, rectBottom), Offset(rectRight, rectBottom), borderWidth, StrokeCap.Round)
        drawLine(borderColor, Offset(rectRight, rectBottom - cornerLength), Offset(rectRight, rectBottom), borderWidth, StrokeCap.Round)
    }
}