package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Murid
import com.example.mbgsmart.data.repository.MuridRepository
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.components.MuridBottomNavBar
import com.example.mbgsmart.ui.sekolah.ProfileMenuItem
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.AuthViewModel

@Composable
fun ProfileMuridScreen(
    authViewModel: AuthViewModel = viewModel(),
    currentScreen: String = "murid_profile",
    onNavigate: (String) -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val muridRepo = remember { MuridRepository() }
    val firebaseUser by authViewModel.currentUser.collectAsState()

    var murid by remember { mutableStateOf<Murid?>(null) }

    /* ===== LOAD DATA MURID ===== */
    LaunchedEffect(firebaseUser?.uid) {
        firebaseUser?.uid?.let { uid ->
            muridRepo.getMurid(
                uid = uid,
                onResult = { murid = it },
                onNotFound = {}
            )
        }
    }

    Scaffold(
        bottomBar = {
            MuridBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreenMurid(
            title = "Profil Murid",
            subtitle = "Informasi akun pengguna",
            modifier = Modifier.padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()), // ✅ FIX SCROLL
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /* ===== AVATAR ===== */
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = murid?.name ?: "-",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Murid",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(24.dp))

                /* ===== INFO ===== */
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        ProfileInfoRow("Nama", murid?.name ?: "-")
                        ProfileInfoRow("Sekolah", murid?.schoolName ?: "-")
                        ProfileInfoRow("Email", murid?.email ?: "-")
                    }
                }

                Spacer(Modifier.height(20.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    title = "Edit Profil",
                    onClick = { onNavigate("murid_edit_profile") }
                )

                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Ubah Password",
                    onClick = { onNavigate("murid_change_password") }
                )

                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    title = "Keluar",
                    isDanger = true,
                    onClick = {
                        authViewModel.logout()
                        onLogoutSuccess()
                    }
                )

                Spacer(Modifier.height(40.dp))

                @Composable
                fun ProfileInfoRow(
                    label: String,
                    value: String
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = value,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
