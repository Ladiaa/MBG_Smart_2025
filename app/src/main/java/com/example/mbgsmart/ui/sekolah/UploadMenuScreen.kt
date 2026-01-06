package com.example.mbgsmart.ui.sekolah

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.ui.components.*
import com.example.mbgsmart.ui.theme.AppCard
import com.example.mbgsmart.ui.theme.AppDropdownField
import com.example.mbgsmart.ui.theme.AppTextField
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.example.mbgsmart.ui.viewmodel.MenuViewModel
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel
import java.text.SimpleDateFormat
import java.util.*

/* ================= ENUM (DIPERTAHANKAN) ================= */
enum class SelectType { NORMAL, NONE, OTHER }

/* ================= SCREEN ================= */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadMenuScreen(
    authViewModel: AuthViewModel = viewModel(),
    sekolahViewModel: SekolahViewModel = viewModel(),
    menuViewModel: MenuViewModel = viewModel(),
    onUploadSuccess: (Menu) -> Unit,
    onNavigate: (String) -> Unit,
    onClearEdit: () -> Unit = {}
) {

    val user by authViewModel.currentUser.collectAsState()
    val sekolah by sekolahViewModel.sekolah.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var karbo by remember { mutableStateOf(SelectType.NONE to "") }
    var protein by remember { mutableStateOf(SelectType.NONE to "") }
    var sayur by remember { mutableStateOf(SelectType.NONE to "") }
    var buah by remember { mutableStateOf(SelectType.NONE to "") }
    var minuman by remember { mutableStateOf(SelectType.NONE to "") }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = "sekolah_upload",
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        /* 🔥 BASESCREEN → LOGO + NAMA APK + PORTAL SEKOLAH */
        BaseScreen(
            title = "Upload Menu Harian",
            subtitle = sekolah?.let { "Portal ${it.namaSekolah}" } ?: "Unggah Menu MBG",
            modifier = Modifier.padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    UploadPhotoButton(imageUri) {
                        picker.launch("image/*")
                    }
                }

                item {
                    DropdownWithOther(
                        "Karbohidrat",
                        karbo,
                        listOf("Nasi", "Mie", "Roti", "Kentang")
                    ) { karbo = it }
                }

                item {
                    DropdownWithOther(
                        "Protein",
                        protein,
                        listOf("Ayam", "Ikan", "Telur", "Tempe")
                    ) { protein = it }
                }

                item {
                    DropdownWithOther(
                        "Sayur",
                        sayur,
                        listOf("Bayam", "Wortel", "Kangkung")
                    ) { sayur = it }
                }

                item {
                    DropdownWithOther(
                        "Buah",
                        buah,
                        listOf("Pisang", "Apel", "Jeruk")
                    ) { buah = it }
                }

                item {
                    DropdownWithOther(
                        "Minuman",
                        minuman,
                        listOf("Air Putih", "Susu")
                    ) { minuman = it }
                }

                /* ===== BUTTON LANJUT ANALISIS ===== */
                item {
                    AppButton(
                        text = "Lanjut Analisis",
                        enabled = imageUri != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        val currentUser = user ?: return@AppButton
                        val currentSchool = sekolah ?: return@AppButton
                        val currentImage = imageUri ?: return@AppButton

                        val menu = Menu(
                            schoolId = currentUser.uid,
                            schoolName = currentSchool.namaSekolah,
                            catering = currentSchool.catering,
                            date = SimpleDateFormat(
                                "dd MMM yyyy",
                                Locale.getDefault()
                            ).format(Date()),
                            imageUri = currentImage.toString(),

                            karbo = karbo.second,
                            protein = protein.second,
                            sayur = sayur.second,
                            buah = buah.second,
                            minuman = minuman.second,

                            statusKelengkapan =
                                if (
                                    karbo.second.isNotBlank() &&
                                    protein.second.isNotBlank() &&
                                    sayur.second.isNotBlank()
                                ) "Lengkap" else "Tidak Lengkap"
                        )

                        onUploadSuccess(menu)
                        onNavigate("sekolah_analysis")
                    }
                }
            }
        }
    }
}




/* ================= DROPDOWN WITH OTHER (DIPERTAHANKAN LOGIKA) ================= */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownWithOther(
    title: String,
    value: Pair<SelectType, String>,
    options: List<String>,
    onChange: (Pair<SelectType, String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Text(
            text = title,
            fontWeight = FontWeight.Medium
        )

        /* ===== DROPDOWN FIELD (DESAIN THEME) ===== */
        Box {
            AppDropdownField(
                value = when (value.first) {
                    SelectType.NONE -> "Tidak ada"
                    SelectType.OTHER -> "Lainnya"
                    SelectType.NORMAL -> value.second
                },
                placeholder = "Pilih $title",
                items = emptyList(), // menu kita handle manual
                onItemSelected = {},
                modifier = Modifier.fillMaxWidth()
            )

            /* Overlay clickable untuk buka menu */
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )
        }

        /* ===== DROPDOWN MENU ===== */
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onChange(SelectType.NORMAL to option)
                        expanded = false
                    }
                )
            }

            DropdownMenuItem(
                text = { Text("Tidak ada") },
                onClick = {
                    onChange(SelectType.NONE to "")
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Lainnya") },
                onClick = {
                    onChange(SelectType.OTHER to "")
                    expanded = false
                }
            )
        }

        /* ===== INPUT JIKA LAINNYA ===== */
        if (value.first == SelectType.OTHER) {
            AppTextField(
                value = value.second,
                onValueChange = { onChange(SelectType.OTHER to it) },
                placeholder = "Tulis $title",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

