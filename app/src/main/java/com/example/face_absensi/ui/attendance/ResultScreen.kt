package com.example.face_absensi.ui.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.PrimaryOrange
import com.example.face_absensi.ui.theme.SoftYellow

@Composable
fun ResultScreen(
    name: String,
    time: String,
    status: String,
    onBackToHome: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SoftYellow.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ikon Centang Hijau
                Box(
                    modifier = Modifier.size(80.dp).background(Color(0xFF4CAF50), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(50.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Berhasil", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                Spacer(modifier = Modifier.height(32.dp))

                // Detail Data
                ResultRow("Nama", name)
                Spacer(modifier = Modifier.height(16.dp))
                ResultRow("Jam", time)
                Spacer(modifier = Modifier.height(16.dp))
                ResultRow("Status", status)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Text("Kembali", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp, color = PrimaryBrown, fontWeight = FontWeight.Medium)
        Text(value, fontSize = 16.sp, color = PrimaryBrown, fontWeight = FontWeight.Bold)
    }
}