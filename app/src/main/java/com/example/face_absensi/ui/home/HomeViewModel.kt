package com.example.face_absensi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_absensi.data.model.TodayResponse
import com.example.face_absensi.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _todayState = MutableStateFlow<TodayResponse?>(null)
    val todayState = _todayState.asStateFlow()

    fun fetchTodayStatus() {
        viewModelScope.launch {
            try {
                val response = apiService.getTodayStatus()
                if (response.isSuccessful) {
                    _todayState.value = response.body()
                }
            } catch (e: Exception) {
                // Biarkan null jika error (fallback ke UI default)
            }
        }
    }
}