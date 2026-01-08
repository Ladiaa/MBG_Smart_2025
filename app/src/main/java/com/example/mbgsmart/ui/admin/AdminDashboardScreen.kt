package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.ui.components.AdminBaseScreen
import com.example.mbgsmart.ui.components.AdminBottomNavBar
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.AdminDashboardViewModel
import com.example.mbgsmart.ui.viewmodel.ReportViewModel

@Composable
fun AdminDashboardScreen(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    viewModel: AdminDashboardViewModel = viewModel(),
    reportViewModel: ReportViewModel = viewModel()
) {

    /* ================= STATE ================= */
    val stats by viewModel.stats

    /* ================= LOAD DATA ================= */
    LaunchedEffect(Unit) {
        viewModel.startListening()
        reportViewModel.loadReportStatsBySchool()
    }

    /* ================= UI ================= */
    Scaffold(
        bottomBar = {
            AdminBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        AdminBaseScreen(
            title = "Dashboard Admin",
            subtitle = "Monitoring Program MBG Nasional",
            modifier = Modifier.padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /* ===== ROW 1 ===== */
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Sekolah",
                            value = stats.totalSchools.toString(),
                            icon = Icons.Default.School,
                            color = BrandDarkBlue
                        )

                        DashboardStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Menu",
                            value = stats.totalMenus.toString(),
                            icon = Icons.Default.Restaurant,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                /* ===== ROW 2 ===== */
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Laporan",
                            value = stats.totalReports.toString(),
                            icon = Icons.Default.Warning,
                            color = Color(0xFFFFA000)
                        )

                        DashboardStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Catering",
                            value = stats.totalCaterings.toString(),
                            icon = Icons.Default.Store,
                            color = Color(0xFF1E88E5)
                        )
                    }
                }

                /* ===== REPORT STAT BY SCHOOL ===== */
                item {
                    ReportStatsBySchoolSection(
                        reportViewModel = reportViewModel
                    )
                }
            }
        }
    }
}


/* ================= CARD ================= */

@Composable
fun DashboardStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )

            Column {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/* ================= REPORT STATS ================= */

@Composable
fun ReportStatsBySchoolSection(
    reportViewModel: ReportViewModel
) {
    val stats by reportViewModel.reportStatsBySchool

    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = "Statistik Laporan per Sekolah",
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            if (stats.isEmpty()) {
                Text("Belum ada laporan", color = Color.Gray)
            } else {
                stats.forEach { (school, count) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(school)
                        Text(
                            text = "$count laporan",
                            fontWeight = FontWeight.Bold,
                            color = BrandRed
                        )
                    }
                    Divider()
                }
            }
        }
    }
}


/* ================================================= */
/* ================= PREVIEW ======================= */
/* ================================================= */

@Preview(showBackground = true)
@Composable
fun PreviewAdminDashboardScreen() {
    MBGSmartTheme {
        AdminDashboardScreen(
            currentScreen = "admin_dashboard",
            onNavigate = {}
        )
    }
}
