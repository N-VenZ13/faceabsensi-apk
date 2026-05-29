package com.example.face_absensi.data.model

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)