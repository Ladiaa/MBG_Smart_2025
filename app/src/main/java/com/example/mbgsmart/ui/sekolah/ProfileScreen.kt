package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.BaseScreen
import com.example.mbgsmart.ui.components.BottomNavigationBar
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    sekolahViewModel: SekolahViewModel = viewModel(),
    currentScreen: String = "sekolah_profile",
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val sekolah by sekolahViewModel.sekolah.collectAsState()

    /* ✅ FIX DI SINI */
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            sekolahViewModel.loadSekolah(
                userId = uid,
                onResult = { /* tidak perlu apa-apa */ }
            )
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreen(
            title = "Profil Sekolah",
            subtitle = "Informasi dan pengaturan akun",
            modifier = Modifier.padding(padding)
        ) {

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SecondaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = SecondaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        sekolah?.namaSekolah ?: "Memuat...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "NPSN: ${sekolah?.npsn ?: "-"} • ${sekolah?.statusSekolah ?: "-"}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileMenuItem(
                icon = Icons.Default.Edit,
                title = "Ubah Identitas Sekolah",
                onClick = { onNavigate("sekolah_identity") }
            )

            ProfileMenuItem(
                icon = Icons.Default.Settings,
                title = "Pengaturan Akun",
                onClick = { }
            )

            ProfileMenuItem(
                icon = Icons.Default.HelpOutline,
                title = "Pusat Bantuan",
                onClick = { }
            )

            ProfileMenuItem(
                icon = Icons.Default.Logout,
                title = "Keluar",
                isDanger = true,
                onClick = onLogout
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isDanger) Color.Red else PrimaryBlue
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDanger) Color.Red else Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
