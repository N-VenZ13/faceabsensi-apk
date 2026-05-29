package com.example.face_absensi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.face_absensi.data.local.SessionManager
import com.example.face_absensi.ui.auth.LoginScreen
import com.example.face_absensi.ui.home.HomeScreen
import com.example.face_absensi.ui.theme.FaceabsensiTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.runtime.getValue // PENTING: Tambahkan ini
import com.example.face_absensi.ui.attendance.AttendanceCameraScreen
import com.example.face_absensi.ui.attendance.AttendanceViewModel
// Pastikan Anda sudah import hiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inject SessionManager langsung
    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaceabsensiTheme {
                // Kirim sessionManager ke navigasi
                AppNavigation(sessionManager)
            }
        }
    }
}

@Composable
fun AppNavigation(sessionManager: SessionManager) {
    val navController = rememberNavController()

    // Observasi token sebagai State
    val token by sessionManager.getToken.collectAsState(initial = null)

    // Logika Navigasi berdasarkan Token
    if (token == null) {
        // Tampilkan loading screen sementara token dimuat
        androidx.compose.material3.CircularProgressIndicator()
    } else {
        NavHost(
            navController = navController,
            startDestination = if (token.isNullOrEmpty()) "login" else "home"
        ) {
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("home") {
                HomeScreen(
                    userName = "Budi Santoso",
                    onAbsenClick = { navController.navigate("camera") },
                    onLogoutClick = {
                        // Anda harus memanggil fungsi logout dari ViewModel di sini
                        // Tapi untuk langkah awal, cara ini sudah cukup untuk testing navigasi
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            composable("camera") {
                // Gunakan hiltViewModel() agar Hilt meng-inject ApiService ke ViewModel
                val viewModel: AttendanceViewModel = hiltViewModel()

                AttendanceCameraScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
