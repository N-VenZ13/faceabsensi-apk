package com.example.face_absensi.data.model

data class HistoryResponse(
    val status: String,
    val data: List<HistoryData>
)

data class HistoryData(
    val id: Int,
    val date: String,
    val check_in: String,
    val check_out: String,
    val status: String
)