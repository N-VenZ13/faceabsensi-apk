package com.example.face_absensi.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.face_absensi.R
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.SoftYellow

@Composable
fun ProfileScreen(
    name: String,   // Tambahan parameter
    email: String,  // Tambahan parameter
    role: String,   // Tambahan parameter
    onLogoutClick: () -> Unit // Kita terima aksi logout dari luar
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Judul Halaman
        Text(
            text = "Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryBrown
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Card ID Profil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Membuat card mengisi sisa ruang yang ada
                .background(SoftYellow.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                .border(1.dp, PrimaryBrown.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Foto Profil
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Ganti dengan foto asli nanti
                    contentDescription = "Foto Profil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Ganti string manual dengan variabel dari parameter
                ProfileRow(label = "Nama", value = name)
                Spacer(modifier = Modifier.height(16.dp))
                ProfileRow(label = "Username", value = name.split(" ").first()) // Ambil kata pertama
                Spacer(modifier = Modifier.height(16.dp))
                ProfileRow(label = "Email", value = email)
                Spacer(modifier = Modifier.height(16.dp))
                // Ubah role menjadi Huruf Kapital Awal
                ProfileRow(label = "Jabatan", value = role.replaceFirstChar { it.uppercase() })
                Spacer(modifier = Modifier.height(16.dp))
                ProfileRow(label = "No. HP", value = "-") // Bisa ditambahkan nanti di DB Laravel

                Spacer(modifier = Modifier.weight(1f)) // Mendorong tulisan Logout ke paling bawah

                // Tombol Logout (Berbentuk Text Button seperti di desain)
                Text(
                    text = "Logout",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable { onLogoutClick() }
                        .padding(8.dp) // Memberi area klik yang lebih luas
                )
            }
        }
    }
}

// Komponen Reusable untuk setiap baris informasi
@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrown
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.End
        )
    }
}