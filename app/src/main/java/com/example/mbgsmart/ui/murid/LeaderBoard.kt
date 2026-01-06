package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.components.MuridBottomNavBar
import com.example.mbgsmart.ui.sekolah.LeaderboardItemCard
import com.example.mbgsmart.ui.viewmodel.MenuViewModel
import com.example.mbgsmart.ui.theme.MBGSmartTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme

@Composable
fun LeaderboardScreenMurid(
    menuViewModel: MenuViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {

    /* ================= STATE ================= */
    val leaderboardData by menuViewModel.leaderboard
    val isLoading by menuViewModel.isLoading
    val errorMessage by menuViewModel.errorMessage

    /* ================= LOAD DATA ================= */
    LaunchedEffect(Unit) {
        menuViewModel.loadLeaderboard()
    }

    /* ================= UI ================= */
    Scaffold(
        bottomBar = {
            MuridBottomNavBar(
                currentScreen = "murid_leaderboard",
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreenMurid(
            title = "Papan Peringkat Sekolah",
            subtitle = "Sekolah paling aktif berdasarkan jumlah menu yang diunggah",
            modifier = Modifier.padding(padding)
        ) {

            /* ===== LOADING ===== */
            if (isLoading) {
                CircularProgressIndicator()
            }

            /* ===== ERROR ===== */
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            /* ===== EMPTY ===== */
            if (!isLoading && leaderboardData.isEmpty()) {
                Text(
                    text = "Belum ada data leaderboard",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            /* ===== LIST LEADERBOARD ===== */
            if (leaderboardData.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(leaderboardData) { index, item ->
                        LeaderboardItemCard(
                            rank = index + 1,
                            schoolName = item.first,
                            uploadCount = item.second
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenMuridPreview() {
    MBGSmartTheme {
        LeaderboardScreenMurid(onNavigate = {})
    }
}
