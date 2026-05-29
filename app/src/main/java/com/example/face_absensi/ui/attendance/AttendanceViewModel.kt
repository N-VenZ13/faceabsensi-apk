package com.example.face_absensi.ui.attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_absensi.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uploadStatus = MutableStateFlow<String?>(null)
    val uploadStatus = _uploadStatus

    fun uploadImage(filePath: String) {

        val file = File(filePath)
        Log.d("DEBUG_UPLOAD", "Path file: $filePath")
        Log.d("DEBUG_UPLOAD", "Apakah file ada?: ${file.exists()}")
        Log.d("DEBUG_UPLOAD", "Ukuran file: ${file.length()} bytes")
        viewModelScope.launch {
            val file = File(filePath)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            try {
                val response = apiService.uploadAttendance(body)
                if (response.isSuccessful) {
                    _uploadStatus.value = "Absensi Berhasil!"
                } else {
                    _uploadStatus.value = "Gagal Upload: ${response.code()}"
                }
            } catch (e: Exception) {
                _uploadStatus.value = "Error: ${e.message}"
            }
        }
    }
}