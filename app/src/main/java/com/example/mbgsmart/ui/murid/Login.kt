package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.R
import com.example.mbgsmart.ui.components.AppButton
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginMurid(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit, // schoolName
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(12.dp))

        Text("MBG Smart", style = MaterialTheme.typography.headlineSmall)
        Text("Login Murid")

        Spacer(Modifier.height(32.dp))

        AppTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        if (errorMessage.isNotBlank()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        AppButton(
            text = if (isLoading) "Masuk..." else "Masuk",
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            isLoading = true

            authViewModel.loginUser(
                email = email,
                pass = password,
                onSuccess = { user ->
                    // AMBIL DATA MURID DARI FIRESTORE
                    FirebaseFirestore.getInstance()
                        .collection("murid")
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            isLoading = false
                            val schoolName =
                                snapshot.getString("schoolName") ?: ""
                            onLoginSuccess(schoolName)
                        }
                        .addOnFailureListener {
                            isLoading = false
                            errorMessage = "Gagal mengambil data murid"
                        }
                },
                onFailure = {
                    isLoading = false
                    errorMessage = it.message ?: "Login gagal"
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Belum punya akun? Daftar")
        }
    }
}

