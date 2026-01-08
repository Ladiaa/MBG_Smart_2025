package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel

@Composable
fun AdminMasterScreen(
    adminVM: AdminMasterViewModel = viewModel()
) {
    var schoolName by remember { mutableStateOf("") }
    var cateringName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Admin Master Data", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = schoolName,
            onValueChange = { schoolName = it },
            label = { Text("Nama Sekolah") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                adminVM.addSchool(
                    name = schoolName,
                    onSuccess = {
                        message = "Sekolah ditambahkan"
                        schoolName = ""
                    },
                    onFailure = {
                        message = "Gagal menambah sekolah"
                    }
                )
            },
            enabled = schoolName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Sekolah")
        }

        OutlinedTextField(
            value = cateringName,
            onValueChange = { cateringName = it },
            label = { Text("Nama Catering") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                adminVM.addCatering(
                    name = cateringName,
                    onSuccess = {
                        message = "Catering ditambahkan"
                        cateringName = ""
                    },
                    onFailure = {
                        message = "Gagal menambah catering"
                    }
                )
            },
            enabled = cateringName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Catering")
        }

        if (message.isNotBlank()) {
            Text(message, color = MaterialTheme.colorScheme.primary)
        }
    }
}
