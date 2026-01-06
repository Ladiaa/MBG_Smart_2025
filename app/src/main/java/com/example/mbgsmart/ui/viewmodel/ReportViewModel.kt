package com.example.mbgsmart.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.data.repository.ReportRepository
import com.google.firebase.firestore.ListenerRegistration

class ReportViewModel : ViewModel() {

    private val repository = ReportRepository()

    private val _reportList = mutableStateOf<List<Report>>(emptyList())
    val reportList: State<List<Report>> = _reportList

    private var reportListener: ListenerRegistration? = null

    private val _reportStatsBySchool =
        mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val reportStatsBySchool: State<List<Pair<String, Int>>> = _reportStatsBySchool

    /* ================= MURID ================= */
    fun startListeningMyReports(muridId: String) {
        stopListening()
        reportListener = repository.listenReportsByMurid(muridId) { reports ->
            _reportList.value = reports
        }
    }

    /* ================= ADMIN ================= */
    fun startListeningAllReports() {
        stopListening()
        reportListener = repository.listenAllReports { reports ->
            _reportList.value = reports
        }
    }

    fun loadReportStatsBySchool() {
        val result = reportList.value
            .groupBy { it.schoolName }
            .map { entry ->
                entry.key to entry.value.size
            }
            .sortedByDescending { it.second }

        _reportStatsBySchool.value = result
    }


    /* ================= CREATE ================= */
    fun submitReport(
        report: Report,
        onSuccess: () -> Unit
    ) {
        repository.createReport(
            report = report,
            onSuccess = onSuccess,
            onFailure = { it.printStackTrace() }
        )
    }

    /* ================= UPDATE ================= */
    fun approveReport(reportId: String) {
        repository.updateReportStatus(
            reportId = reportId,
            status = "APPROVED"
        )
    }

    fun rejectReport(reportId: String) {
        repository.updateReportStatus(
            reportId = reportId,
            status = "REJECTED"
        )
    }

    /* ================= DELETE ================= */
    fun deleteReport(reportId: String) {
        repository.deleteReport(
            reportId = reportId,
            onSuccess = {},
            onFailure = { it.printStackTrace() }
        )
    }

    /* ================= CLEAN ================= */
    fun stopListening() {
        reportListener?.remove()
        reportListener = null
    }

    override fun onCleared() {
        stopListening()
        super.onCleared()
    }
}
