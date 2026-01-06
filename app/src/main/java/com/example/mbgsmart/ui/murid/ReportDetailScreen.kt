package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.theme.*


@Composable
fun ReportDetailScreen(
    report: Report,
    onBack: () -> Unit
) {

    /* ================= STATUS COLOR ================= */
    val statusColor = when (report.status) {
        "APPROVED" -> BrandGreen
        "REJECTED" -> BrandRed
        else -> Color(0xFFFFA000) // Menunggu
    }

    BaseScreenMurid(
        title = "Detail Laporan",
        subtitle = "Informasi laporan yang dikirim"
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ===== HEADER ===== */
            Text(
                text = report.menuName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = report.schoolName,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Divider()

            /* ===== INFORMASI ===== */
            InfoRow("Tanggal", formatTimestamp(report.createdAt))
            InfoRow("Status", report.status, statusColor)
            InfoRow("Menu", report.menuSummary.ifBlank { "-" })
            InfoRow("Alasan", report.reason)

            Divider()

            /* ===== DESKRIPSI ===== */
            Text(
                text = "Deskripsi",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = report.description.ifBlank { "-" },
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* ===== ACTION ===== */
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandDarkBlue)
            ) {
                Text("Kembali")
            }
        }
    }
}

/* ================= INFO ROW ================= */
@Composable
fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = Color.DarkGray
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = valueColor
        )
    }
}


