package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.R
import com.example.mbgsmart.ui.components.AppButton
import com.example.mbgsmart.ui.components.AppCard
import com.example.mbgsmart.ui.theme.AppTextField
import com.example.mbgsmart.ui.theme.BrandDarkBlue
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: (FirebaseUser) -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isFormValid =
        email.isNotBlank() &&
                password.length >= 6 &&
                password == confirmPassword

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo MBG Smart",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "MBG Smart",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BrandDarkBlue
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text("Email", fontWeight = FontWeight.Medium)
                    AppTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "email@sekolah.sch.id",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Password", fontWeight = FontWeight.Medium)
                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Konfirmasi Password", fontWeight = FontWeight.Medium)
                    AppTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    AppButton(
                        text = if (isLoading) "Mendaftar..." else "Daftar",
                        enabled = isFormValid && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        isLoading = true
                        authViewModel.registerSekolah(
                            email = email,
                            password = password,
                            onSuccess = { user ->
                                isLoading = false
                                onRegisterSuccess(user) // 🔥 KIRIM USER
                            },
                            onFailure = {
                                isLoading = false
                                errorMessage = it.message ?: "Registrasi gagal"
                            }
                        )
                    }


                    TextButton(onClick = onNavigateToLogin) {
                        Text("Sudah punya akun? Masuk")
                    }
                }
            }
        }
    }
}


