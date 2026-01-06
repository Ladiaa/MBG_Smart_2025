package com.example.mbgsmart.data.repository

import com.example.mbgsmart.data.model.Report
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class ReportRepository {

    private val db = FirebaseFirestore.getInstance()
    private val reportRef = db.collection("reports")

    /* ================= REALTIME (MURID) ================= */
    fun listenReportsByMurid(
        muridId: String,
        onUpdate: (List<Report>) -> Unit
    ): ListenerRegistration {
        return reportRef
            .whereEqualTo("muridId", muridId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val reports = snapshot?.documents
                    ?.mapNotNull { it.toObject(Report::class.java) }
                    ?: emptyList()

                onUpdate(reports)
            }
    }

    /* ================= REALTIME (ADMIN) ================= */
    fun listenAllReports(
        onUpdate: (List<Report>) -> Unit
    ): ListenerRegistration {
        return reportRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val reports = snapshot?.documents
                    ?.mapNotNull { it.toObject(Report::class.java) }
                    ?: emptyList()

                onUpdate(reports)
            }
    }

    /* ================= CREATE ================= */
    fun createReport(
        report: Report,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val doc = reportRef.document()

        val newReport = report.copy(
            id = doc.id,
            createdAt = Timestamp.now()
        )

        doc.set(newReport)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    /* ================= UPDATE ================= */
    fun updateReportStatus(
        reportId: String,
        status: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        reportRef.document(reportId)
            .update("status", status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    /* ================= DELETE ================= */
    fun deleteReport(
        reportId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        reportRef.document(reportId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
