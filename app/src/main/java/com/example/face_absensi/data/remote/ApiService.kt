package com.example.face_absensi.data.remote

import com.example.face_absensi.data.model.AttendanceResponse
import com.example.face_absensi.data.model.HistoryResponse
import com.example.face_absensi.data.model.LoginResponse
import com.example.face_absensi.data.model.TodayResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Headers("Accept: application/json")
    @POST("login")
    suspend fun login(@Body request: Map<String, String>): Response<LoginResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("attendance/upload")
    suspend fun uploadAttendance(
        @Part image: MultipartBody.Part
    ): Response<AttendanceResponse>

    @Headers("Accept: application/json")
    @GET("attendance/history")
    suspend fun getHistory(): Response<HistoryResponse>

    @Headers("Accept: application/json")
    @GET("attendance/today")
    suspend fun getTodayStatus(): Response<TodayResponse>
}