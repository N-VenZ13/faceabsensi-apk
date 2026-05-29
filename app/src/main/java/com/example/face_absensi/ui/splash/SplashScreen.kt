package com.example.face_absensi.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    // Animasi Scale untuk Logo
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 800,
                easing = { OvershootInterpolator(2f).getInterpolation(it) }
            )
        )
        // Tahan di layar selama 2 detik sebelum pindah
        delay(2000L)
        onSplashFinished()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Background bersih dan formal
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ganti R.drawable.ic_launcher_foreground dengan logo Notaris/App Anda nanti
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo Aplikasi",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value) // Terapkan efek scale animasi
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SISTEM ABSENSI",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryBrown,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kantor Notaris & PPAT",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryOrange
            )
        }
    }
}