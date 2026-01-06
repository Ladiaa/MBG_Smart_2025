package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.data.model.asText
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.theme.BrandDarkBlue
import com.example.mbgsmart.ui.viewmodel.ReportViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReportMenuScreen(
    menu: Menu,
    onSubmitSuccess: () -> Unit,
    reportViewModel: ReportViewModel = viewModel()
) {

    var reason by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val muridId = currentUser?.uid ?: ""
    val muridName = currentUser?.email ?: "Murid"

    /* ===== HITUNG SCORE MENU (SAMA SEPERTI ANALISIS) ===== */
    val menuScore = remember(menu) {
        var score = 0
        if (menu.karbo.asText().isNotBlank()) score += 20
        if (menu.protein.asText().isNotBlank()) score += 20
        if (menu.sayur.asText().isNotBlank()) score += 20
        if (menu.buah.asText().isNotBlank()) score += 20
        if (menu.minuman.asText().isNotBlank()) score += 20
        score
    }

    /* ===== RINGKASAN MENU ===== */
    val menuSummary = remember(menu) {
        listOf(
            menu.karbo.asText(),
            menu.protein.asText(),
            menu.sayur.asText(),
            menu.buah.asText(),
            menu.minuman.asText()
        ).filter { it.isNotBlank() }
            .joinToString(" • ")
    }

    BaseScreenMurid(
        title = "Laporkan Menu",
        subtitle = "Laporkan menu MBG yang tidak sesuai"
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ===== INFO MENU ===== */
            Text("Sekolah: ${menu.schoolName}")
            Text("Tanggal: ${menu.date}")

            Divider()

            /* ===== DETAIL MENU ===== */
            Text("Detail Menu", style = MaterialTheme.typography.titleMedium)

            Text("Karbohidrat: ${menu.karbo.asText()}")
            Text("Protein: ${menu.protein.asText()}")
            Text("Sayur: ${menu.sayur.asText()}")
            Text("Buah: ${menu.buah.asText()}")
            Text("Minuman: ${menu.minuman.asText()}")

            Divider()

            /* ===== ALASAN ===== */
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Alasan Laporan") },
                modifier = Modifier.fillMaxWidth()
            )

            /* ===== DESKRIPSI ===== */
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi Detail") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            /* ===== SUBMIT ===== */
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isSubmitting &&
                        reason.isNotBlank() &&
                        description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandDarkBlue),
                onClick = {
                    isSubmitting = true

                    val report = Report(
                        id = "",
                        menuId = menu.id,
                        menuName = "Menu ${menu.date}",
                        menuSummary = menuSummary,
                        menuScore = menuScore,
                        muridId = muridId,
                        muridName = muridName,
                        schoolId = menu.schoolId,
                        schoolName = menu.schoolName,
                        reason = reason,
                        description = description,
                        status = "PENDING"
                        // createdAt otomatis
                    )

                    reportViewModel.submitReport(
                        report = report,
                        onSuccess = {
                            isSubmitting = false
                            onSubmitSuccess()
                        }
                    )
                }
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Kirim Laporan")
                }
            }
        }
    }
}
