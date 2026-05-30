package com.example.face_absensi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.face_absensi.data.local.SessionManager
import com.example.face_absensi.ui.attendance.AttendanceCameraScreen
import com.example.face_absensi.ui.attendance.AttendanceViewModel
import com.example.face_absensi.ui.attendance.ResultScreen
import com.example.face_absensi.ui.auth.AuthViewModel
import com.example.face_absensi.ui.auth.LoginScreen
import com.example.face_absensi.ui.history.HistoryScreen
import com.example.face_absensi.ui.home.HomeScreen
import com.example.face_absensi.ui.profile.ProfileScreen
import com.example.face_absensi.ui.splash.SplashScreen
import com.example.face_absensi.ui.theme.FaceabsensiTheme
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.SoftYellow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaceabsensiTheme {
                AppNavigation(sessionManager)
            }
        }
    }
}

@Composable
fun AppNavigation(sessionManager: SessionManager) {
    val navController = rememberNavController()
    // 1. Ambil semua state dari SessionManager
    val token by sessionManager.getToken.collectAsState(initial = null)
    val userName by sessionManager.getName.collectAsState(initial = "User")
    val userEmail by sessionManager.getEmail.collectAsState(initial = "")
    val userRole by sessionManager.getRole.collectAsState(initial = "")

    // Deteksi route saat ini untuk menentukan apakah BottomBar harus tampil
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // List halaman yang menggunakan BottomBar
    val bottomBarRoutes = listOf("home", "history", "profile")

    // Scaffold mengatur kerangka utama layar
    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                // Bottom Bar Kustom kita
                NavigationBar(
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    containerColor = SoftYellow, // Warna latar bottom bar
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "home",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryBrown,
                            indicatorColor = PrimaryBrown, // Background ikon saat aktif
                            unselectedIconColor = PrimaryBrown,
                            unselectedTextColor = PrimaryBrown
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == "history",
                        onClick = {
                            navController.navigate("history") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "History") },
                        label = { Text("History") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryBrown,
                            indicatorColor = PrimaryBrown,
                            unselectedIconColor = PrimaryBrown,
                            unselectedTextColor = PrimaryBrown
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == "profile",
                        onClick = {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryBrown,
                            indicatorColor = PrimaryBrown,
                            unselectedIconColor = PrimaryBrown,
                            unselectedTextColor = PrimaryBrown
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // innerPadding adalah jarak agar konten tidak tertutup oleh BottomBar
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding).background(Color.White)
        ) {

            composable("splash") {
                SplashScreen(onSplashFinished = {
                    val destination = if (token.isNullOrEmpty()) "login" else "home"
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }

            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }

            composable("home") {
                HomeScreen(
                    userName = userName,
                    onAbsenClick = { navController.navigate("camera") }
                )
            }

            composable("camera") {
                val viewModel: AttendanceViewModel = hiltViewModel()
                AttendanceCameraScreen(
                    viewModel = viewModel,
                    onSuccess = { name, time, status ->
                        // Lempar data ke layar result
                        navController.navigate("result/$name/$time/$status") {
                            // Hapus kamera dari stack agar user gak bisa "back" ke kamera
                            popUpTo("camera") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("result/{name}/{time}/{status}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val time = backStackEntry.arguments?.getString("time") ?: ""
                val status = backStackEntry.arguments?.getString("status") ?: ""

                ResultScreen(
                    name = name,
                    time = time,
                    status = status,
                    onBackToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            // --- HALAMAN BARU ---
            composable("history") {
                HistoryScreen()
            }

            composable("profile") {
                // Inject AuthViewModel untuk memanggil fungsi logout
                val authViewModel: AuthViewModel = hiltViewModel()

                ProfileScreen(
                    name = userName,
                    email = userEmail,
                    role = userRole,
                    onLogoutClick = {
                        authViewModel.logout {
                            navController.navigate("login") {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}
