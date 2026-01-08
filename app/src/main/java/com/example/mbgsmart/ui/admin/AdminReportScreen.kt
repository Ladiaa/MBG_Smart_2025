package com.example.mbgsmart.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.ui.components.AdminBaseScreen
import com.example.mbgsmart.ui.components.AdminBottomNavBar
import com.example.mbgsmart.ui.murid.InfoRow
import com.example.mbgsmart.ui.theme.*
import androidx.compose.foundation.lazy.items
import com.example.mbgsmart.ui.viewmodel.ReportViewModel

@Composable
fun AdminReportScreen(
    currentScreen: String = "admin_report",
    onNavigate: (String) -> Unit,
    viewModel: ReportViewModel = viewModel()
) {
    val reports = viewModel.reportList.value
    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedReport by remember { mutableStateOf<Report?>(null) }

    LaunchedEffect(Unit) {
        viewModel.startListeningAllReports()
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.stopListening() }
    }

    val filteredReports = when (selectedFilter) {
        "PENDING" -> reports.filter { it.status == "PENDING" }
        "APPROVED" -> reports.filter { it.status == "APPROVED" }
        "REJECTED" -> reports.filter { it.status == "REJECTED" }
        else -> reports
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
            title = "Laporan Menu Murid",
            subtitle = "Monitoring & validasi laporan",
            modifier = Modifier.padding(padding)
        ) {

            ReportFilterSection(
                selected = selectedFilter,
                onSelect = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredReports.isEmpty()) {
                Text("Tidak ada laporan", color = Color.Gray)
                return@AdminBaseScreen
            }

            /* 🔥 FIX UTAMA: LazyColumn */
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredReports,
                    key = { it.id }
                ) { report ->
                    AdminReportCard(
                        report = report,
                        onClick = { selectedReport = report },
                        onApprove = { viewModel.approveReport(report.id) },
                        onReject = { viewModel.rejectReport(report.id) }
                    )
                }

            }
        }
    }

    selectedReport?.let { report ->
        AdminReportDetailDialog(
            report = report,
            onDismiss = { selectedReport = null },
            onDelete = {
                viewModel.deleteReport(report.id)
                selectedReport = null
            }
        )
    }
}

/* ================================================= */
/* ================= FILTER SECTION ================= */
/* ================================================= */

@Composable
fun ReportFilterSection(
    selected: String,
    onSelect: (String) -> Unit
) {
    val filters = listOf("ALL", "PENDING", "APPROVED", "REJECTED")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Filter Status Laporan",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = SecondaryBlue
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters.size) { index ->
                    val filter = filters[index]
                    FilterChip(
                        selected = selected == filter,
                        onClick = { onSelect(filter) },
                        label = {
                            Text(
                                when (filter) {
                                    "ALL" -> "Semua"
                                    "PENDING" -> "Pending"
                                    "APPROVED" -> "Disetujui"
                                    "REJECTED" -> "Ditolak"
                                    else -> filter
                                }
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandDarkBlue.copy(alpha = 0.15f),
                            selectedLabelColor = BrandDarkBlue
                        )
                    )
                }
            }
        }
    }
}

/* ================================================= */
/* ================= REPORT CARD =================== */
/* ================================================= */

@Composable
fun AdminReportCard(
    report: Report,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {

    val statusColor = when (report.status) {
        "APPROVED" -> BrandGreen
        "REJECTED" -> BrandRed
        else -> Color(0xFFFFA000)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = report.menuName,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = report.schoolName,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text("Menu: ${report.menuSummary}")

            Box(
                modifier = Modifier
                    .background(
                        statusColor.copy(alpha = 0.15f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = report.status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
                ) {
                    Text("Terima")
                }

                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandRed)
                ) {
                    Text("Tolak")
                }
            }
        }
    }
}

/* ================================================= */
/* ================= DETAIL DIALOG ================= */
/* ================================================= */

@Composable
fun AdminReportDetailDialog(
    report: Report,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detail Laporan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow("Sekolah", report.schoolName)
                InfoRow("Menu", report.menuSummary)
                InfoRow("Alasan", report.reason)
                InfoRow("Deskripsi", report.description.ifBlank { "-" })
                InfoRow("Status", report.status)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        },
        dismissButton = {
            TextButton(onClick = onDelete) {
                Text("Hapus", color = Color.Red)
            }
        }
    )
}
