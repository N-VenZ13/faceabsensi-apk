package com.example.face_absensi.ui.attendance

import android.content.Context
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.face_absensi.ui.components.CustomDialog

@Composable
fun AttendanceCameraScreen(
    viewModel: AttendanceViewModel,
    onSuccess: (String, String, String) -> Unit, // Callback saat sukses
    onBack: () -> Unit // Callback saat batal/error
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }


    var isSuccessDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }


    val uploadState by viewModel.uploadState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Tampilkan pesan jika upload selesai
    // Pantau State
    LaunchedEffect(uploadState) {
        when (val state = uploadState) {
            is AttendanceState.Success -> {
                // LANGSUNG PINDAH LAYAR TANPA DIALOG
                onSuccess(state.result.name, state.result.time, state.result.status)
            }
            is AttendanceState.Error -> {
                dialogMessage = state.message
                showDialog = true // Tampilkan dialog error
            }
            else -> {}
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
                takePhoto(context, imageCapture) { filePath ->
                    viewModel.uploadImage(filePath)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp)
        ) {
            Text("Ambil Foto & Absen")
        }
        // ----------------------------------------------------
        // LOADING OVERLAY TAMPIL JIKA SEDANG UPLOAD/PROSES AI
        // ----------------------------------------------------
        if (uploadState is AttendanceState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Memproses Wajah...", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Modal Sukses / Gagal
    CustomDialog(
        showDialog = showDialog,
        isSuccess = false,
        title = "Gagal",
        message = dialogMessage,
        onDismiss = {
            showDialog = false
            viewModel.resetState()
            // Biarkan user tetap di kamera untuk mencoba lagi
        }
    )
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