package com.example.face_absensi.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.face_absensi.R
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.PrimaryOrange
import com.example.face_absensi.ui.theme.SoftYellow
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    userName: String,
    onAbsenClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // 1. STATE REAL-TIME CLOCK
    var currentTime by remember { mutableStateOf("00:00:00") }
    var currentDate by remember { mutableStateOf("Memuat tanggal...") }

    // Efek Jam Real-time (Looping tiap 1 detik)
    LaunchedEffect(Unit) {
        while (true) {
            val date = Date()
            currentTime = SimpleDateFormat("HH : mm : ss", Locale.getDefault()).format(date)
            currentDate = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(date)
            delay(1000)
        }
    }

    // 2. STATE DATA API
    val todayStatus by viewModel.todayState.collectAsState()

    // Panggil API saat halaman dibuka
    LaunchedEffect(Unit) {
        viewModel.fetchTodayStatus()
    }

    // Variabel Aman (Fallback)
    val checkInText = todayStatus?.check_in ?: "- : -"
    val checkOutText = todayStatus?.check_out ?: "- : -"
    val mainMessage = todayStatus?.status_message ?: "MEMUAT DATA..."
    val buttonText = todayStatus?.action_button ?: "TUNGGU"
    val isDone = buttonText == "SELESAI"

    // Pesan instruksi di bawah jam
    val instructionMessage = when {
        isDone -> "Anda telah menyelesaikan shift hari ini"
        buttonText == "ABSEN PULANG" -> "Jangan lupa lakukan absensi pulang"
        else -> "Silakan lakukan absensi masuk"
    }

    // 3. UI LENGKAP
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // --- HEADER (Profil & Notifikasi) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = userName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                    Text(text = "Staff", fontSize = 12.sp, color = Color.Gray)
                }
            }
            IconButton(onClick = { /* TODO: Halaman Notifikasi */ }) {
                Icon(Icons.Default.NotificationsNone, contentDescription = "Notification", tint = PrimaryBrown)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- TANGGAL & TITLE ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Absensi Hari ini", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBrown)
            Text(text = currentDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- KARTU ABSENSI UTAMA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SoftYellow),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mainMessage,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBrown
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentTime,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = instructionMessage, // Pesan dinamis berdasarkan status
                    fontSize = 12.sp,
                    color = PrimaryBrown
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onAbsenClick,
                    enabled = !isDone && buttonText != "TUNGGU",
                    modifier = Modifier.width(160.dp).height(45.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDone) Color.Gray else PrimaryOrange,
                        disabledContainerColor = Color.LightGray, // Warna saat tombol mati
                        disabledContentColor = Color.DarkGray
                    )
                ) {
                    Text(buttonText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- LIST HISTORY SINGKAT HARI INI ---
        Text(text = "Absensi Anda", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
        Spacer(modifier = Modifier.height(12.dp))

        HistoryCardRow(
            title = "Absensi Masuk",
            time = checkInText,
            status = if(checkInText != "- : -") "Tervalidasi" else "Belum Absen"
        )

        Spacer(modifier = Modifier.height(12.dp))

        HistoryCardRow(
            title = "Absensi Keluar",
            time = checkOutText,
            status = if(checkOutText != "- : -") "Tervalidasi" else "Belum Absen"
        )
    }
}

// Fungsi Reusable
@Composable
fun HistoryCardRow(title: String, time: String, status: String) {
    val statusColor = if (status == "Tervalidasi") Color(0xFF4CAF50) else PrimaryOrange

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, PrimaryBrown, RoundedCornerShape(16.dp))
            .background(SoftYellow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Status : $status", fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Medium)
            }
            Text(text = time, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
        }
    }
}