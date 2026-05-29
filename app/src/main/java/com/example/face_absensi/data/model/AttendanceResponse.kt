package com.example.face_absensi.data.model

data class AttendanceResponse(
    val status: String,
    val message: String,
    val user: String? = null,
    val time: String? = null
)