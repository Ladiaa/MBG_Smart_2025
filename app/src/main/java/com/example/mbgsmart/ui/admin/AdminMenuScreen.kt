package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.ui.components.AdminBaseScreen
import com.example.mbgsmart.ui.components.AdminBottomNavBar
import androidx.compose.foundation.lazy.items
import com.example.mbgsmart.ui.viewmodel.MenuViewModel


@Composable
fun AdminMenuScreen(
    currentScreen: String = "admin_menu",
    onNavigate: (String) -> Unit,
    viewModel: MenuViewModel = viewModel()
) {
    val menus = viewModel.menuList.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf<Menu?>(null) }

    LaunchedEffect(Unit) {
        viewModel.startListeningAllMenus()
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.stopListening() }
    }

    Scaffold(
        bottomBar = {
            AdminBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        AdminBaseScreen(
            title = "Monitoring Menu Sekolah",
            subtitle = "Pantau menu yang diunggah oleh sekolah",
            modifier = Modifier.padding(padding)
        ) {

            if (isLoading) {
                CircularProgressIndicator()
                return@AdminBaseScreen
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                return@AdminBaseScreen
            }

            if (menus.isEmpty()) {
                Text("Belum ada menu yang diunggah", color = Color.Gray)
                return@AdminBaseScreen
            }

            /* 🔥 FIX UTAMA: LazyColumn */
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = menus,
                    key = { it.id }
                ) { menu ->
                    AdminMenuCard(
                        menu = menu,
                        onDelete = {
                            selectedMenu = it
                            showDeleteDialog = true
                        }
                    )
                }

            }
        }
    }

    if (showDeleteDialog && selectedMenu != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                selectedMenu = null
            },
            title = { Text("Hapus Menu?") },
            text = { Text("Menu yang dihapus tidak dapat dikembalikan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMenu(
                            menuId = selectedMenu!!.id,
                            onSuccess = {
                                showDeleteDialog = false
                                selectedMenu = null
                            },
                            onFailure = {
                                showDeleteDialog = false
                                selectedMenu = null
                            }
                        )
                    }
                ) {
                    Text("Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        selectedMenu = null
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}


/* ================================================= */
/* ================= MENU CARD ===================== */
/* ================================================= */

@Composable
fun AdminMenuCard(
    menu: Menu,
    onDelete: (Menu) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            AsyncImage(
                model = menu.imageUri,
                contentDescription = "Foto Menu",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = menu.schoolName,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = menu.date,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "Catering: ${menu.catering}",
                fontSize = 13.sp
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { onDelete(menu) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hapus Menu", color = Color.White)
            }
        }
    }
}

