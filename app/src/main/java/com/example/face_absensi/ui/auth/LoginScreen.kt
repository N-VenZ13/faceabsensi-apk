package com.example.face_absensi.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.face_absensi.R
import com.example.face_absensi.ui.components.CustomDialog
import com.example.face_absensi.ui.theme.PrimaryBrown
import com.example.face_absensi.ui.theme.PrimaryOrange

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var isSuccessDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    // Memantau perubahan state
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is AuthState.Success -> {
                isSuccessDialog = true
                dialogTitle = "Berhasil"
                dialogMessage = state.message
                showDialog = true
            }
            is AuthState.Error -> {
                isSuccessDialog = false
                dialogTitle = "Gagal"
                dialogMessage = state.message
                showDialog = true
            }
            // Abaikan Idle & Loading
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

            Image(
                painter = painterResource(id = R.drawable.logo_apk), // Logo aplikasi
                contentDescription = "Login Illustration",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selamat Datang",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Username / Email", color = PrimaryBrown.copy(alpha = 0.7f)) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "User Icon", tint = PrimaryBrown)
                },
                singleLine = true,
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = PrimaryOrange,
                    unfocusedContainerColor = PrimaryOrange,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PrimaryBrown,
                    focusedTextColor = PrimaryBrown,
                    unfocusedTextColor = PrimaryBrown
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = PrimaryBrown.copy(alpha = 0.7f)) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Lock Icon", tint = PrimaryBrown)
                },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = "Toggle Password", tint = PrimaryBrown)
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = PrimaryOrange,
                    unfocusedContainerColor = PrimaryOrange,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PrimaryBrown,
                    focusedTextColor = PrimaryBrown,
                    unfocusedTextColor = PrimaryBrown
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                color = PrimaryBrown,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { /* TODO */ }
                    .padding(end = 8.dp)
            )


            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Login dengan indikator Loading
            val isLoading = loginState is AuthState.Loading

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLoading) Color.Gray else PrimaryBrown // Ganti warna saat loading
                ),
                enabled = !isLoading // Matikan tombol saat proses berjalan
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Log In", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Custom Dialog
    CustomDialog(
        showDialog = showDialog,
        isSuccess = isSuccessDialog,
        title = dialogTitle,
        message = dialogMessage,
        onDismiss = {
            showDialog = false
            viewModel.resetState() // RESET STATE! Agar tombol bisa diklik dan memicu error lagi
            if (isSuccessDialog) {
                onLoginSuccess()
            }
        }
    )
}