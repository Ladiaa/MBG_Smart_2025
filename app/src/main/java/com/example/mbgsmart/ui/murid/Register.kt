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
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel

@Composable
fun RegisterMurid(
    authViewModel: AuthViewModel = viewModel(),
    adminVM: AdminMasterViewModel = viewModel(),
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var selectedSchoolName by remember { mutableStateOf("") }
    var selectedSchoolId by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val schoolList by adminVM.schools
    val schoolNames = schoolList.map { it.namaSekolah }

    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            selectedSchoolId.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(12.dp))

        Text("MBG Smart", style = MaterialTheme.typography.headlineSmall)
        Text("Registrasi Murid")

        Spacer(Modifier.height(32.dp))

        AppTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Nama Lengkap",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AppDropdownField(
            value = selectedSchoolName,
            placeholder = "Pilih Asal Sekolah",
            items = schoolNames,
            onItemSelected = { schoolName ->
                selectedSchoolName = schoolName
                selectedSchoolId =
                    schoolList.firstOrNull { it.namaSekolah == schoolName }?.id ?: ""
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

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

        if (errorMessage.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        AppButton(
            text = if (isLoading) "Mendaftar..." else "Daftar",
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                isLoading = true
                authViewModel.registerMurid(
                    name = name,
                    email = email,
                    password = password,
                    schoolId = selectedSchoolId, 
                    schoolName = selectedSchoolName,
                    onSuccess = {
                        isLoading = false
                        onRegisterSuccess(selectedSchoolName) // PERBAIKAN: Mengembalikan NAMA
                    },
                    onFailure = {
                        isLoading = false
                        errorMessage = it.message ?: "Registrasi gagal"
                    }
                )
            }
        )

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Sudah punya akun? Login")
        }
    }
}
