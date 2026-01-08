package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.components.MuridBottomNavBar
import com.example.mbgsmart.ui.navigation.Routes
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.AuthViewModel
import com.example.mbgsmart.ui.viewmodel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*

/* ================= FILTER ENUM ================= */
enum class ReportFilter {
    ALL, PENDING, APPROVED, REJECTED
}

enum class ReportStatus(val label: String, val color: Color) {
    PENDING("Menunggu", Color(0xFFFFA000)),
    APPROVED("Diterima", Color(0xFF4CAF50)),
    REJECTED("Ditolak", Color(0xFFE53935))
}

/* ================= SCREEN ================= */
@Composable
fun ReportHistoryScreen(
    authViewModel: AuthViewModel = viewModel(),
    reportViewModel: ReportViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    onViewDetail: (Report) -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val reports by reportViewModel.reportList

    var selectedFilter by remember { mutableStateOf(ReportFilter.ALL) }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            reportViewModel.startListeningMyReports(it)
        }
    }

    val filteredReports = remember(reports, selectedFilter) {
        when (selectedFilter) {
            ReportFilter.ALL -> reports
            ReportFilter.PENDING -> reports.filter { it.status == "PENDING" }
            ReportFilter.APPROVED -> reports.filter { it.status == "APPROVED" }
            ReportFilter.REJECTED -> reports.filter { it.status == "REJECTED" }
        }
    }

    DisposableEffect(Unit) {
        onDispose { reportViewModel.stopListening() }
    }

    Scaffold(
        bottomBar = {
            MuridBottomNavBar(
                currentScreen = Routes.MuridRoutes.HISTORY,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreenMurid(
            title = "Riwayat Laporan",
            subtitle = "Status laporan menu yang telah dikirim"
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                /* ================= FILTER BAR ================= */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterButton("Semua", selectedFilter == ReportFilter.ALL) {
                        selectedFilter = ReportFilter.ALL
                    }
                    FilterButton("Pending", selectedFilter == ReportFilter.PENDING) {
                        selectedFilter = ReportFilter.PENDING
                    }
                    FilterButton("Diterima", selectedFilter == ReportFilter.APPROVED) {
                        selectedFilter = ReportFilter.APPROVED
                    }
                    FilterButton("Ditolak", selectedFilter == ReportFilter.REJECTED) {
                        selectedFilter = ReportFilter.REJECTED
                    }
                }

                /* ================= LIST ================= */
                if (filteredReports.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada laporan",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredReports, key = { it.id }) { report ->
                            ReportItemCard(
                                report = report,
                                onClick = { onViewDetail(report) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ================= FILTER BUTTON ================= */
@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected)
                BrandDarkBlue
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (selected)
                Color.White
            else
                Color.DarkGray
        )
    ) {
        Text(text, fontSize = 12.sp)
    }
}

/* ================= ITEM CARD ================= */
@Composable
fun ReportItemCard(
    report: Report,
    onClick: () -> Unit
) {
    val status = when (report.status) {
        "APPROVED" -> ReportStatus.APPROVED
        "REJECTED" -> ReportStatus.REJECTED
        else -> ReportStatus.PENDING
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = report.menuName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tanggal: ${formatTimestamp(report.createdAt)}",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )

                if (report.menuSummary.isNotBlank()) {
                    Text(
                        text = "Menu: ${report.menuSummary}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "Alasan: ${report.reason}",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                StatusChip(status)
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }
        }
    }
}

/* ================= STATUS CHIP ================= */
@Composable
fun StatusChip(status: ReportStatus) {
    Box(
        modifier = Modifier
            .background(
                color = status.color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            color = status.color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/* ================= HELPER ================= */
fun formatTimestamp(timestamp: com.google.firebase.Timestamp?): String {
    if (timestamp == null) return "-"
    val date = timestamp.toDate()
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
}

/* ================= PREVIEW ================= */
@Preview(showBackground = true)
@Composable
fun PreviewReportHistoryScreen() {
    MBGSmartTheme {
        ReportHistoryScreen(
            onNavigate = {},
            onViewDetail = {}
        )
    }
}
