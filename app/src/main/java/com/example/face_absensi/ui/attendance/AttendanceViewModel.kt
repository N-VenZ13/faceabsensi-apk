package com.example.face_absensi.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_absensi.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

// Ubah AuthState Success menjadi membawa object/data
data class AttendanceResult(val name: String, val time: String, val status: String)

// State Khusus untuk Absensi
sealed class AttendanceState {
    object Idle : AttendanceState()
    object Loading : AttendanceState()
    data class Success(val result: AttendanceResult) : AttendanceState() // Bawa Data
    data class Error(val message: String) : AttendanceState()
}

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uploadState = MutableStateFlow<AttendanceState>(AttendanceState.Idle)
    val uploadState = _uploadState.asStateFlow()

    fun uploadImage(filePath: String) {
        viewModelScope.launch {
            _uploadState.value = AttendanceState.Loading // Set Loading!

            val file = File(filePath)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            try {
                val response = apiService.uploadAttendance(body)
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Absensi Berhasil!"
                    _uploadState.value = AttendanceState.Success(
                        AttendanceResult(name = "Budi Santoso", time = "08:00", status = "Hadir")
                    )
                } else {
                    // Tangkap pesan asli dari Laravel
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "Wajah tidak dikenali (Error ${response.code()})"
                    }
                    _uploadState.value = AttendanceState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _uploadState.value = AttendanceState.Error("Koneksi gagal: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uploadState.value = AttendanceState.Idle
    }
}