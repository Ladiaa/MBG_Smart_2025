package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.repository.MuridRepository
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.components.MuridBottomNavBar
import com.example.mbgsmart.ui.viewmodel.AuthViewModel

@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    currentScreen: String = "murid_profile",
    onNavigate: (String) -> Unit
) {
    val muridRepo = remember { MuridRepository() }
    val firebaseUser by authViewModel.currentUser.collectAsState()

    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var sekolah by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    /* ===== LOAD DATA MURID ===== */
    LaunchedEffect(firebaseUser?.uid) {
        firebaseUser?.uid?.let { uid ->
            muridRepo.getMurid(
                uid = uid,
                onResult = { murid ->
                    nama = murid.name
                    email = murid.email
                    sekolah = murid.schoolName
                },
                onNotFound = {}
            )
        }
    }

    Scaffold(
        bottomBar = {
            MuridBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreenMurid(
            title = "Edit Profil",
            subtitle = "Perbarui informasi akun murid",
            modifier = Modifier.padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sekolah,
                    onValueChange = {},
                    label = { Text("Sekolah") },
                    enabled = false, // 🔒 tidak bisa diubah murid
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val uid = firebaseUser?.uid ?: return@Button
                        loading = true

                        muridRepo.updateMurid(
                            uid = uid,
                            name = nama,
                            email = email,
                            onSuccess = {
                                loading = false
                                onNavigate("murid_profile")
                            },
                            onFailure = {
                                loading = false
                            }
                        )
                    },
                    enabled = !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(if (loading) "Menyimpan..." else "Simpan Perubahan")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
