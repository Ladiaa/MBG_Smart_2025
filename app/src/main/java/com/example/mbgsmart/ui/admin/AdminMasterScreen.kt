package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.AdminBaseScreen
import com.example.mbgsmart.ui.components.AdminBottomNavBar
import com.example.mbgsmart.ui.components.BaseScreen
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel

@Composable
fun AdminMasterScreen(
    currentScreen: String = "admin_school",
    onNavigate: (String) -> Unit,
    adminVM: AdminMasterViewModel = viewModel()
) {

    /* ================= STATE ================= */
    var schoolName by remember { mutableStateOf("") }
    var schoolAddress by remember { mutableStateOf("") }

    var cateringName by remember { mutableStateOf("") }
    var cateringPhone by remember { mutableStateOf("") }

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
            title = "Master Data",
            subtitle = "Kelola Sekolah & Catering MBG",
            modifier = Modifier.padding(padding)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /* ================================================= */
                /* ================= TAMBAH SEKOLAH ================ */
                /* ================================================= */

                Text(
                    text = "Tambah Sekolah Penerima MBG",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = schoolName,
                    onValueChange = { schoolName = it },
                    label = { Text("Nama Sekolah") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = schoolAddress,
                    onValueChange = { schoolAddress = it },
                    label = { Text("Alamat Sekolah") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (schoolName.isNotBlank() && schoolAddress.isNotBlank()) {
                            adminVM.addSchool(schoolName, schoolAddress)
                            schoolName = ""
                            schoolAddress = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Sekolah")
                }

                Divider(thickness = 1.dp)

                /* ================================================= */
                /* ================= TAMBAH CATERING =============== */
                /* ================================================= */

                Text(
                    text = "Tambah Catering",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = cateringName,
                    onValueChange = { cateringName = it },
                    label = { Text("Nama Catering") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cateringPhone,
                    onValueChange = { cateringPhone = it },
                    label = { Text("No. Telepon") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (cateringName.isNotBlank() && cateringPhone.isNotBlank()) {
                            adminVM.addCatering(cateringName, cateringPhone)
                            cateringName = ""
                            cateringPhone = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Catering")
                }
            }
        }
    }
}
