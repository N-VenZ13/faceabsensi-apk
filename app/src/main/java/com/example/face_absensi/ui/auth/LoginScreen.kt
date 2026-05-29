package com.example.face_absensi.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.face_absensi.R
import com.example.face_absensi.ui.theme.PrimaryBrown

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Mengamati state dari ViewModel
    val loginState by viewModel.loginState.collectAsState()

    // Navigasi otomatis saat login sukses
    LaunchedEffect(loginState) {
        if (loginState != null && loginState!!.contains("Login Berhasil")) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selamat Datang",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrown
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Username / Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBrown,
                focusedLabelColor = PrimaryBrown
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBrown,
                focusedLabelColor = PrimaryBrown
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Login
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Log In", color = Color.White, fontSize = 16.sp)
        }

        // Tampilkan pesan error jika ada
        if (loginState != null && loginState!!.contains("Gagal")) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = loginState!!, color = Color.Red, fontSize = 14.sp)
        }
    }
}