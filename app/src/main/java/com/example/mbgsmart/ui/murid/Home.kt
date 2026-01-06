package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.model.asText
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.components.MuridBottomNavBar
import com.example.mbgsmart.ui.theme.BrandDarkBlue
import com.example.mbgsmart.ui.theme.BrandGreen
import com.example.mbgsmart.ui.theme.BrandRed
import com.example.mbgsmart.ui.viewmodel.MenuViewModel

@Composable
fun HomeScreenMurid(
    schoolName: String,
    menuViewModel: MenuViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    onReportMenu: (Menu) -> Unit
) {

    /* ================= STATE ================= */
    val menus = menuViewModel.menuList.value
    val isLoading = menuViewModel.isLoading.value
    val errorMessage = menuViewModel.errorMessage.value

    /* ================= LISTENER ================= */
    DisposableEffect(schoolName) {
        if (schoolName.isNotBlank()) {
            menuViewModel.startListeningMenusBySchoolName(schoolName)
        }
        onDispose { menuViewModel.stopListening() }
    }

    /* ================= UI ================= */
    Scaffold(
        bottomBar = {
            MuridBottomNavBar(
                currentScreen = "murid_home",
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        BaseScreenMurid(
            title = "Menu Hari Ini",
            subtitle = "Menu dari sekolah kamu",
            modifier = Modifier.padding(padding)
        ) {

            if (isLoading) {
                CircularProgressIndicator()
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            if (!isLoading && menus.isEmpty()) {
                Text("Belum ada menu", color = Color.Gray)
            }

            if (menus.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(menus, key = { it.id }) { menu ->
                        MenuCardForMurid(
                            menu = menu,
                            onReport = { onReportMenu(menu) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCardForMurid(
    menu: Menu,
    onReport: () -> Unit
) {

    val score = remember(menu) { hitungSkor(menu) }
    val scoreColor =
        if (score >= 80) BrandGreen else BrandRed

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            /* ===== FOTO MENU ===== */
            AsyncImage(
                model = menu.imageUri,
                contentDescription = "Menu Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = menu.schoolName,
                fontWeight = FontWeight.Bold,
                color = BrandDarkBlue
            )

            Text(
                text = menu.date,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            /* ===== SKOR ===== */
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skor Menu", fontWeight = FontWeight.Medium)
                Text(
                    "$score",
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = if (score >= 80) "Menu Seimbang"
                else "Menu Belum Lengkap",
                color = scoreColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(10.dp))

            /* ===== DETAIL ===== */
            MenuTypeItem("Karbohidrat", menu.karbo.asText())
            MenuTypeItem("Protein", menu.protein.asText())
            MenuTypeItem("Sayur", menu.sayur.asText())
            MenuTypeItem("Buah", menu.buah.asText())
            MenuTypeItem("Minuman", menu.minuman.asText())

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onReport,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandRed)
            ) {
                Text("Laporkan Menu")
            }
        }
    }
}
fun hitungSkor(menu: Menu): Int {
    var score = 0
    if (menu.karbo.asText().isNotBlank()) score += 20
    if (menu.protein.asText().isNotBlank()) score += 20
    if (menu.sayur.asText().isNotBlank()) score += 20
    if (menu.buah.asText().isNotBlank()) score += 20
    if (menu.minuman.asText().isNotBlank()) score += 20
    return score
}

@Composable
fun MenuTypeItem(
    label: String,
    value: String
) {
    if (value.isNotBlank()) {
        Text(
            text = "$label: $value",
            fontSize = 13.sp,
            color = Color.DarkGray
        )
    }
}
