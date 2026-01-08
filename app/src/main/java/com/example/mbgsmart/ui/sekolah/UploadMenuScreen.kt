package com.example.mbgsmart.ui.sekolah

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.ui.components.*
import com.example.mbgsmart.ui.navigation.Routes
import com.example.mbgsmart.ui.theme.AppDropdownField
import com.example.mbgsmart.ui.theme.AppTextField
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.example.mbgsmart.ui.viewmodel.MenuViewModel
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class SelectType { NORMAL, NONE, OTHER }

@Composable
fun UploadMenuScreen(
    authViewModel: AuthViewModel = viewModel(),
    sekolahViewModel: SekolahViewModel = viewModel(),
    paddingValues: PaddingValues,
    onUploadSuccess: (Menu) -> Unit,
    onNavigate: (String) -> Unit
)
 {
    val user by authViewModel.currentUser.collectAsState()
    val sekolah by sekolahViewModel.sekolah.collectAsState()

    LaunchedEffect(user) {
        user?.uid?.let { sekolahViewModel.loadSekolah(it) {} }
    }

    if (user == null || sekolah == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var karbo by remember { mutableStateOf(SelectType.NONE to "") }
    var protein by remember { mutableStateOf(SelectType.NONE to "") }
    var sayur by remember { mutableStateOf(SelectType.NONE to "") }
    var buah by remember { mutableStateOf(SelectType.NONE to "") }
    var minuman by remember { mutableStateOf(SelectType.NONE to "") }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri = it }

     BaseScreen(
         title = "Upload Menu Harian",
         subtitle = "Portal ${sekolah!!.namaSekolah}",
         modifier = Modifier.padding(paddingValues) // ⬅️ PENTING
     ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            UploadPhotoButton(imageUri) {
                picker.launch("image/*")
            }

            DropdownWithOther("Karbohidrat", karbo, listOf("Nasi", "Mie", "Roti")) {
                karbo = it
            }

            DropdownWithOther("Protein", protein, listOf("Ayam", "Ikan", "Telur")) {
                protein = it
            }

            DropdownWithOther("Sayur", sayur, listOf("Bayam", "Wortel")) {
                sayur = it
            }

            DropdownWithOther("Buah", buah, listOf("Pisang", "Apel")) {
                buah = it
            }

            DropdownWithOther("Minuman", minuman, listOf("Air Putih", "Susu")) {
                minuman = it
            }

            AppButton(
                text = "Lanjut Analisis",
                enabled = imageUri != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                val menu = Menu(
                    schoolId = sekolah!!.id,
                    schoolName = sekolah!!.namaSekolah,
                    catering = sekolah!!.catering,
                    date = SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    ).format(Date()),
                    imageUri = imageUri!!.toString(),
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
            }
        }
    }
}


@Composable
fun DropdownWithOther(
    title: String,
    value: Pair<SelectType, String>,
    options: List<String>,
    onChange: (Pair<SelectType, String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Text(text = title, fontWeight = FontWeight.Medium)

        Box {
            AppDropdownField(
                value = when (value.first) {
                    SelectType.NORMAL -> value.second
                    SelectType.OTHER -> if (value.second.isBlank()) "Lainnya" else value.second
                    SelectType.NONE -> "Data belum tersedia"
                },
                placeholder = "Pilih $title",
                items = listOf(""), // ⬅️ PENTING: JANGAN emptyList()
                onItemSelected = {},
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
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
