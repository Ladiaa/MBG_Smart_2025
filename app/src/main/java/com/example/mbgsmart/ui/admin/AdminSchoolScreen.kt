package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.AdminBaseScreen
import com.example.mbgsmart.ui.components.AdminBottomNavBar
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel

@Composable
fun AdminSchoolScreen(
    currentScreen: String = "admin_school",
    onNavigate: (String) -> Unit,
    viewModel: AdminMasterViewModel = viewModel()
) {

    /* ================= STATE ================= */
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val schools by viewModel.schools

    /* ================= UI ================= */
    Scaffold(
        bottomBar = {
            AdminBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        AdminBaseScreen(
            title = "Data Sekolah",
            subtitle = "Kelola sekolah penerima MBG",
            modifier = Modifier.padding(padding)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                /* ================= FORM TAMBAH ================= */
                Text(
                    text = "Tambah Sekolah Penerima MBG",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Sekolah") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Alamat Sekolah") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (name.isNotBlank() && address.isNotBlank()) {
                            viewModel.addSchool(name, address)
                            name = ""
                            address = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Sekolah")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                /* ================= LIST SEKOLAH ================= */
                Text(
                    text = "Daftar Sekolah",
                    style = MaterialTheme.typography.titleMedium
                )

                if (schools.isEmpty()) {
                    Text(
                        text = "Belum ada data sekolah",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        schools.forEach { school ->
                            Text("• ${school.namaSekolah}")
                        }
                    }
                }
            }
        }
    }
}
