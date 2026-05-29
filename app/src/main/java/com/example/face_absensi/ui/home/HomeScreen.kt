package com.example.face_absensi.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.SoftYellow

@Composable
fun HomeScreen(userName: String, onAbsenClick: () -> Unit,  onLogoutClick: () -> Unit ) { // Default "User"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header Profile
        Text("Selamat Datang,", fontSize = 16.sp, color = Color.Gray)
        Text(userName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)

        Spacer(modifier = Modifier.height(32.dp))

        // Card Absensi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SoftYellow),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("ANDA BELUM ABSEN", fontWeight = FontWeight.Bold, color = PrimaryBrown)
                Spacer(modifier = Modifier.height(8.dp))
                // Jam real-time bisa ditambahkan nanti
                Text("08 : 00 : 00", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onAbsenClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ABSEN")
                }
            }
        }

        // Tambahkan tombol Logout di bagian atas atau bawah
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Logout")
        }
    }
}