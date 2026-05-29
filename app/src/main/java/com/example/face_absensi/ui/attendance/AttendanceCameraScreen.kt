package com.example.face_absensi.ui.attendance

import android.content.Context
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File

@Composable
fun AttendanceCameraScreen(
    viewModel: AttendanceViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    val uploadStatus by viewModel.uploadStatus.collectAsState()

    // Tampilkan pesan jika upload selesai
    LaunchedEffect(uploadStatus) {
        uploadStatus?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageCapture
                            )
                        } catch (e: Exception) { e.printStackTrace() }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                takePhoto(context, imageCapture) { path ->
                    viewModel.uploadImage(path)
                    onNavigateBack()
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp)
        ) {
            Text("Ambil Foto & Absen")
        }
    }
}

fun takePhoto(context: Context, imageCapture: ImageCapture, onImageCaptured: (String) -> Unit) {
    val photoFile = File(context.cacheDir, "absensi_${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onImageCaptured(photoFile.absolutePath)
            }
            override fun onError(e: ImageCaptureException) {
                Toast.makeText(context, "Gagal ambil foto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}