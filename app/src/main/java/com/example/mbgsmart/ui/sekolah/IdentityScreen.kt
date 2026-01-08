package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Sekolah
import com.example.mbgsmart.ui.components.*
import com.example.mbgsmart.ui.theme.AppDropdownField
import com.example.mbgsmart.ui.theme.AppTextField
import com.example.mbgsmart.ui.viewmodel.AdminMasterViewModel
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun IdentityScreen(
    sekolahVM: SekolahViewModel = viewModel(),
    adminVM: AdminMasterViewModel = viewModel(),
    onSaveSuccess: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    /* ================= AUTH GUARD ================= */
    if (currentUser == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val uid = currentUser.uid
    val sekolah by sekolahVM.sekolah.collectAsState()

    /* 🔥 FLAG AGAR TIDAK DOUBLE LOAD / NAVIGATE */
    var hasChecked by remember { mutableStateOf(false) }

    /* ================= LOAD SEKOLAH ================= */
    LaunchedEffect(uid) {
        if (!hasChecked) {
            hasChecked = true
            sekolahVM.loadSekolah(
                userId = uid,
                onResult = { hasData ->
                    if (hasData) {
                        onSaveSuccess() // ✅ AUTO SKIP KE UPLOAD
                    }
                }
            )
        }
    }

    /* ================= FORM STATE ================= */
    var selectedSchoolName by remember { mutableStateOf("") }
    var selectedSchoolId by remember { mutableStateOf("") }
    var namaPJ by remember { mutableStateOf("") }
    var jabatanPJ by remember { mutableStateOf("") }
    var hpPJ by remember { mutableStateOf("") }
    var selectedCatering by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }

    val schoolList by adminVM.schools
    val cateringList by adminVM.caterings

    val schoolNames = schoolList.map { it.namaSekolah }
    val cateringNames = cateringList.map { it.name }

    /* ================= UI ================= */
    Scaffold { padding ->
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

            item { AppTextField(namaPJ, { namaPJ = it }, "Nama Penanggung Jawab") }
            item { AppTextField(jabatanPJ, { jabatanPJ = it }, "Jabatan") }
            item { AppTextField(hpPJ, { hpPJ = it }, "No HP") }

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
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            item {
                AppButton(
                    text = if (saving) "Menyimpan..." else "Simpan & Lanjut",
                    enabled = !saving &&
                            selectedSchoolId.isNotBlank() &&
                            selectedCatering.isNotBlank() &&
                            namaPJ.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    saving = true

                    sekolahVM.saveSekolah(
                        sekolah = Sekolah(
                            id = selectedSchoolId,
                            uid = uid,
                            namaSekolah = selectedSchoolName,
                            namaPJ = namaPJ,
                            jabatanPJ = jabatanPJ,
                            hpPJ = hpPJ,
                            catering = selectedCatering
                        ),
                        onSuccess = {
                            saving = false
                            onSaveSuccess()
                        },
                        onError = {
                            saving = false
                            errorMessage = it
                        }
                    )
                }
            }
        }
    }
}
