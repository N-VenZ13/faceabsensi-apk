package com.example.face_absensi.ui.auth


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_absensi.data.local.SessionManager
import com.example.face_absensi.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(mapOf("email" to email, "password" to password))
                if (response.isSuccessful) {
                    val token = response.body()?.access_token ?: ""
                    sessionManager.saveToken(token) // <--- SIMPAN TOKEN DI SINI
                    _loginState.value = "Login Berhasil"
                } else {
                    _loginState.value = "Login Gagal"
                }
            } catch (e: Exception) {
                _loginState.value = "Error: ${e.message}"
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLogoutSuccess()
        }
    }
}