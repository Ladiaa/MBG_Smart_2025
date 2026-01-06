package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.AdminBaseScreen
import com.example.mbgsmart.ui.components.AdminBottomNavBar
import com.example.mbgsmart.ui.components.BaseScreen
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel

@Composable
fun AdminCateringScreen(
    currentScreen: String = "admin_catering",
    onNavigate: (String) -> Unit,
    viewModel: AdminMasterViewModel = viewModel()
) {

    /* ================= STATE ================= */
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // 🔥 STATE DARI VIEWMODEL
    val caterings by viewModel.caterings

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
            title = "Data Catering",
            subtitle = "Kelola daftar catering MBG",
            modifier = Modifier.padding(padding)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                /* ================= FORM TAMBAH ================= */
                Text(
                    text = "Tambah Catering",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Catering") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("No. Telepon") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            viewModel.addCatering(name, phone)
                            name = ""
                            phone = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Catering")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                /* ================= LIST CATERING ================= */
                Text(
                    text = "Daftar Catering",
                    style = MaterialTheme.typography.titleMedium
                )

                if (caterings.isEmpty()) {
                    Text(
                        text = "Belum ada data catering",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        caterings.forEach { catering ->
                            Text("• ${catering.name}")
                        }
                    }
                }
            }
        }
    }
}
