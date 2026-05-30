package com.example.face_absensi.data.model

data class TodayResponse(
    val check_in: String?,
    val check_out: String?,
    val status_message: String,
    val action_button: String
)