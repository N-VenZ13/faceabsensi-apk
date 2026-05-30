package com.example.face_absensi.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_absensi.data.local.SessionManager
import com.example.face_absensi.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

// 1. Buat class khusus untuk UI State
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Gunakan AuthState, bukan String
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading // Set ke Loading saat diklik

            try {
                val response = apiService.login(mapOf("email" to email, "password" to password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Simpan token DAN data user
                        sessionManager.saveSession(
                            token = body.access_token,
                            name = body.user.name,
                            email = body.user.email,
                            role = body.user.role
                        )
                        _loginState.value = AuthState.Success("Login berhasil!")
                    }
                } else {
                    // Coba ambil pesan error asli dari JSON Laravel
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "Gagal login. Periksa kembali data Anda."
                    }
                    _loginState.value = AuthState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _loginState.value = AuthState.Error("Tidak ada koneksi ke server.")
            }
        }
    }

    // Fungsi untuk mereset state (agar dialog bisa muncul lagi saat diklik berulang kali)
    fun resetState() {
        _loginState.value = AuthState.Idle
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLogoutSuccess()
        }
    }
}