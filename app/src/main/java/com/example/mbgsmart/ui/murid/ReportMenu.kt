package com.example.mbgsmart.ui.murid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.theme.BrandDarkBlue
import com.example.mbgsmart.ui.viewmodel.ReportViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun ReportMenuScreen(
    menu: Menu,
    onSubmitSuccess: () -> Unit,
    reportViewModel: ReportViewModel = viewModel()
) {
    var reason by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser == null) {
        Text("User tidak valid, silakan login ulang")
        return
    }

    val muridId = currentUser.uid
    val muridName = currentUser.email ?: "Murid"

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        BaseScreenMurid(
            title = "Laporkan Menu",
            subtitle = "Laporkan menu MBG yang tidak sesuai",
            modifier = Modifier.padding(padding) // ✅ FIX PADDING
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Text("Sekolah: ${menu.schoolName}")
                    Text("Tanggal: ${menu.date}")
                    HorizontalDivider()
                }

                item {
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text("Alasan Laporan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Deskripsi Detail") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }

                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isSubmitting &&
                                reason.isNotBlank() &&
                                description.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandDarkBlue
                        ),
                        onClick = {
                            isSubmitting = true

                            val report = Report(
                                id = "",
                                menuId = menu.id,
                                menuName = "Menu ${menu.date}",
                                menuSummary = "",
                                menuScore = 0,
                                muridId = muridId,
                                muridName = muridName,
                                schoolId = menu.schoolId,
                                schoolName = menu.schoolName,
                                reason = reason,
                                description = description,
                                status = "PENDING"
                            )

                            reportViewModel.submitReport(
                                report = report,
                                onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Laporan berhasil dikirim"
                                        )
                                        delay(700)
                                        onSubmitSuccess()
                                    }
                                    isSubmitting = false
                                }
                            )
                        }
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Kirim Laporan")
                        }
                    }
                }
            }
        }
    }
}






