package com.example.face_absensi.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.face_absensi.R
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.PrimaryOrange
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // State untuk memicu animasi
    var logoVisible by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }
    var subtitleVisible by remember { mutableStateOf(false) }

    // Sequence Animasi (Berurutan)
    LaunchedEffect(key1 = true) {
        logoVisible = true
        delay(300) // Jeda 0.3 detik
        titleVisible = true
        delay(200) // Jeda 0.2 detik
        subtitleVisible = true

        // Tunggu sebentar agar user sempat membaca, lalu pindah layar
        delay(1500)
        onSplashFinished()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animasi Logo: Fade In + Membesar perlahan
            AnimatedVisibility(
                visible = logoVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        scaleIn(
                            initialScale = 0.5f,
                            animationSpec = tween(800, easing = FastOutSlowInEasing)
                        )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_apk), // Ganti dengan logo notaris Anda
                    contentDescription = "Logo Aplikasi",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Animasi Judul Utama: Naik dari bawah + Fade In
            AnimatedVisibility(
                visible = titleVisible,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(
                            initialOffsetY = { 50 }, // Mulai 50 pixel dari bawah
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        )
            ) {
                Text(
                    text = "SISTEM ABSENSI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBrown,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animasi Subjudul: Muncul dari sisi kanan + Fade In
            AnimatedVisibility(
                visible = subtitleVisible,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInHorizontally(
                            initialOffsetX = { 50 }, // Mulai 50 pixel dari kanan
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        )
            ) {
                Text(
                    text = "Kantor Notaris & PPAT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryOrange
                )
            }
        }
    }
}