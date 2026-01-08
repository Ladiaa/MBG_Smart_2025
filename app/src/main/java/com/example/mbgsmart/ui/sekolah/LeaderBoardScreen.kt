package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.*
import com.example.mbgsmart.ui.theme.SecondaryBlue
import com.example.mbgsmart.ui.viewmodel.MenuViewModel

@Composable
fun LeaderboardScreen(
    currentScreen: String = "sekolah_leaderboard",
    onNavigate: (String) -> Unit,
    menuViewModel: MenuViewModel = viewModel()
) {

    /* ================= STATE ================= */
    val leaderboardData = menuViewModel.leaderboard.value
    val isLoading = menuViewModel.isLoading.value
    val errorMessage = menuViewModel.errorMessage.value

    /* ================= LOAD DATA ================= */
    LaunchedEffect(Unit) {
        menuViewModel.loadLeaderboard()
    }

    /* ================= UI ================= */
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreen(
            title = "Papan Peringkat Sekolah",
            subtitle = "Sekolah paling aktif berdasarkan jumlah menu yang diunggah",
            modifier = Modifier.padding(padding)
        ) {

            /* ===== LOADING ===== */
            if (isLoading) {
                CircularProgressIndicator()
            }

            /* ===== ERROR ===== */
            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error
                )
            }

            /* ===== LIST LEADERBOARD ===== */
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                leaderboardData.forEachIndexed { index, item ->
                    LeaderboardItemCard(
                        rank = index + 1,
                        schoolName = item.first,
                        uploadCount = item.second
                    )
                }

                if (leaderboardData.isEmpty() && !isLoading) {
                    Text("Belum ada data leaderboard.")
                }
            }

        }
    }
}

/* ================= ITEM CARD ================= */

@Composable
fun LeaderboardItemCard(
    rank: Int,
    schoolName: String,
    uploadCount: Int
) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> SecondaryBlue
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            /* ===== RANK ===== */
            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                if (rank <= 3) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = rankColor,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Text(
                        text = "#$rank",
                        fontWeight = FontWeight.Bold,
                        color = SecondaryBlue,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            /* ===== SCHOOL NAME ===== */
            Text(
                text = schoolName,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = SecondaryBlue
            )

            /* ===== UPLOAD COUNT ===== */
            Text(
                text = "$uploadCount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = rankColor
            )
        }
    }
}
