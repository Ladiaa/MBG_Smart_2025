package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel

@Composable
fun AdminCateringScreen(
    adminVM: AdminMasterViewModel = viewModel()
) {
    var cateringName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tambah Catering", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = cateringName,
            onValueChange = { cateringName = it },
            label = { Text("Nama Catering") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotBlank()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        if (successMessage.isNotBlank()) {
            Text(successMessage, color = MaterialTheme.colorScheme.primary)
        }

        Button(
            onClick = {
                errorMessage = ""
                successMessage = ""

                adminVM.addCatering(
                    name = cateringName,
                    onSuccess = {
                        successMessage = "Catering berhasil ditambahkan"
                        cateringName = ""
                    },
                    onFailure = { e ->
                        errorMessage = e.message ?: "Gagal menambah catering"
                    }
                )
            },
            enabled = cateringName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Catering")
        }
    }
}
