package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbgsmart.R
import com.example.mbgsmart.ui.theme.*

@Composable
fun LoginScreenAdmin(
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val isFormValid = email.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        /* ===== LOGO ===== */
        Image(
            painter = painterResource(id = R.drawable.logo), // pastikan ada
            contentDescription = "Logo MBG Smart",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "MBG Smart",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = BrandDarkBlue
        )

        Text(
            text = "Portal Admin",
            fontSize = 14.sp,
            color = BrandDarkBlue.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ===== CARD LOGIN ===== */
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {

                Text("Email Admin", color = BrandDarkBlue)
                Spacer(Modifier.height(8.dp))
                AppTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    placeholder = "admin@mbg.com",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text("Password", color = BrandDarkBlue)
                Spacer(Modifier.height(8.dp))
                AppTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    placeholder = "••••••••",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                AppButton(
                    text = "Masuk",
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // ===== SIMULASI LOGIN ADMIN =====
                        if (email == "admin@mbg.com" && password == "admin123") {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Email atau password salah"
                        }
                    }
                )
            }
        }
    }
}
