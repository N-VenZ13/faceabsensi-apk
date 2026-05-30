package com.example.face_absensi.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_absensi.data.model.HistoryData
import com.example.face_absensi.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val list: List<HistoryData>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _historyState = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val historyState = _historyState.asStateFlow()

    init {
        fetchHistory() // Otomatis ambil data saat ViewModel dibuat
    }

    fun fetchHistory() {
        viewModelScope.launch {
            _historyState.value = HistoryState.Loading
            try {
                val response = apiService.getHistory()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    _historyState.value = HistoryState.Success(data)
                } else {
                    _historyState.value = HistoryState.Error("Gagal mengambil data")
                }
            } catch (e: Exception) {
                _historyState.value = HistoryState.Error("Koneksi Error: ${e.message}")
            }
        }
    }
}