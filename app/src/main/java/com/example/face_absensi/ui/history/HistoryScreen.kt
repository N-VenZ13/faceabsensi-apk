package com.example.face_absensi.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.face_absensi.data.model.HistoryData
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.SoftYellow

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel() // Ambil ViewModel via Hilt
) {
    val state by viewModel.historyState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(top = 32.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "History Absensi",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryBrown,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reaksi terhadap State (Loading, Sukses, Error)
        when (state) {
            is HistoryState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBrown)
                }
            }
            is HistoryState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = (state as HistoryState.Error).message, color = Color.Red)
                }
            }
            is HistoryState.Success -> {
                val list = (state as HistoryState.Success).list

                if (list.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada data absensi", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp) // Jarak dari BottomBar
                    ) {
                        items(list) { item ->
                            HistoryCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryData) {
    Box(
        modifier = Modifier.fillMaxWidth().background(SoftYellow.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .border(1.dp, PrimaryBrown.copy(alpha = 0.5f), RoundedCornerShape(20.dp)).padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.date, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBrown)
                Spacer(modifier = Modifier.height(4.dp))
                // Jika check_out belum ada, statusnya "Belum Selesai", dll. (Contoh logika dasar)
                val statusText = if(item.check_out == "-") "Absen Masuk (${item.check_in})" else "Selesai (${item.check_in} - ${item.check_out})"
                Text(text = statusText, fontSize = 13.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
            }
            IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Outlined.Info, contentDescription = "Detail", tint = PrimaryBrown)
            }
        }
    }
}