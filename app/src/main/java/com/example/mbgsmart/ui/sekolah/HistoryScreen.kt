package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.model.asText
import com.example.mbgsmart.ui.components.*
import com.example.mbgsmart.ui.viewmodel.MenuViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel


@Composable
fun HistoryScreen(
    menuViewModel: MenuViewModel,
    sekolahViewModel: SekolahViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    onEditMenu: (Menu) -> Unit
) {
    val sekolah by sekolahViewModel.sekolah.collectAsState()
    val menuList by menuViewModel.menuList
    val isLoading by menuViewModel.isLoading
    val errorMessage by menuViewModel.errorMessage

    LaunchedEffect(sekolah?.id) {
        sekolah?.id?.let {
            menuViewModel.startListeningMenusBySchool(it)
        }
    }

    BaseScreen(
        title = "Riwayat Menu",
        subtitle = "Daftar menu yang telah diunggah"
    ) {

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            errorMessage != null -> {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            menuList.isEmpty() -> {
                Text("Belum ada menu yang diunggah.")
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    menuList.forEach { menu ->
                        MenuHistoryCard(
                            menu = menu,
                            onEdit = { onEditMenu(menu) },
                            onDelete = {
                                menuViewModel.deleteMenu(menu.id, {}, {})
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuHistoryCard(
    menu: Menu,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    /* ================= HITUNG SKOR ================= */
    val score =
        (if (menu.karbo.asText().isNotBlank()) 20 else 0) +
                (if (menu.protein.asText().isNotBlank()) 20 else 0) +
                (if (menu.sayur.asText().isNotBlank()) 20 else 0) +
                (if (menu.buah.asText().isNotBlank()) 20 else 0) +
                (if (menu.minuman.asText().isNotBlank()) 20 else 0)

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            /* ===== FOTO MENU ===== */
            AsyncImage(
                model = menu.imageUri,
                contentDescription = "Menu Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            /* ===== TANGGAL ===== */
            Text(menu.date, fontWeight = FontWeight.Bold)

            /* ===== SKOR ===== */
            Text(
                text = "Skor Nutrisi: $score",
                fontWeight = FontWeight.Bold,
                color =
                    if (score >= 80)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
            )

            Text(
                text = if (score >= 80) "Sangat Baik" else "Perlu Perbaikan",
                fontWeight = FontWeight.Medium
            )

            Divider()

            /* ===== DETAIL MENU ===== */
            Text("Karbohidrat: ${menu.karbo.asText()}")
            Text("Protein: ${menu.protein.asText()}")
            Text("Sayur: ${menu.sayur.asText()}")
            Text("Buah: ${menu.buah.asText()}")
            Text("Minuman: ${menu.minuman.asText()}")

            Text(
                text = "Status: ${menu.statusKelengkapan}",
                color =
                    if (menu.statusKelengkapan == "Lengkap")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
            )

            /* ===== AKSI ===== */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onEdit
                ) {
                    Text("Edit")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = onDelete
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}
