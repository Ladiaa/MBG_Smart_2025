package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Sekolah
import com.example.mbgsmart.ui.components.*
import com.example.mbgsmart.ui.theme.AppTextField
import com.example.mbgsmart.ui.theme.AppDropdownField
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun IdentityScreen(
    sekolahVM: SekolahViewModel = viewModel(),
    adminVM: AdminMasterViewModel = viewModel(),
    onSaveSuccess: () -> Unit,
    onNavigate: (String) -> Unit
) {

    var selectedSchoolName by remember { mutableStateOf("") }
    var selectedSchoolId by remember { mutableStateOf("") }

    var namaPJ by remember { mutableStateOf("") }
    var jabatanPJ by remember { mutableStateOf("") }
    var hpPJ by remember { mutableStateOf("") }
    var selectedCatering by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val schoolList by adminVM.schools
    val cateringList by adminVM.caterings

    val schoolNames = schoolList.map { it.namaSekolah }
    val cateringNames = cateringList.map { it.name }

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = "sekolah_identity",
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { AppHeader() }

            item {
                PageTitleCard(
                    title = "Identitas Sekolah",
                    subtitle = "Lengkapi data sekolah penerima MBG"
                )
            }

            item {
                AppDropdownField(
                    value = selectedSchoolName,
                    placeholder = "Pilih Sekolah",
                    items = schoolNames,
                    onItemSelected = { name ->
                        selectedSchoolName = name
                        selectedSchoolId =
                            schoolList.first { it.namaSekolah == name }.id
                    }
                )
            }

            item {
                AppTextField(namaPJ, { namaPJ = it }, "Nama Penanggung Jawab")
            }

            item {
                AppTextField(jabatanPJ, { jabatanPJ = it }, "Jabatan")
            }

            item {
                AppTextField(hpPJ, { hpPJ = it }, "No HP")
            }

            item {
                AppDropdownField(
                    value = selectedCatering,
                    placeholder = "Pilih Catering",
                    items = cateringNames,
                    onItemSelected = { selectedCatering = it }
                )
            }

            if (errorMessage.isNotBlank()) {
                item {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }

            item {
                AppButton(
                    text = if (isLoading) "Menyimpan..." else "Simpan Identitas",
                    enabled =
                        !isLoading &&
                                uid.isNotBlank() &&
                                selectedSchoolId.isNotBlank() &&
                                selectedCatering.isNotBlank() &&
                                namaPJ.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    isLoading = true

                    val sekolah = Sekolah(
                        id = selectedSchoolId,
                        uid = uid,
                        namaSekolah = selectedSchoolName,
                        namaPJ = namaPJ,
                        jabatanPJ = jabatanPJ,
                        hpPJ = hpPJ,
                        catering = selectedCatering
                    )

                    sekolahVM.saveSekolah(
                        sekolah,
                        onSuccess = {
                            isLoading = false
                            onSaveSuccess()
                        },
                        onFailure = {
                            isLoading = false
                            errorMessage = it.message ?: "Gagal menyimpan data"
                        }
                    )
                }
            }
        }
    }
}


