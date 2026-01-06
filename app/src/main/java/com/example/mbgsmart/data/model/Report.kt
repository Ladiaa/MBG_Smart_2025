package com.example.mbgsmart.data.model

import com.google.firebase.Timestamp

data class Report(
    val id: String = "",

    /* ===== MENU ===== */
    val menuId: String = "",
    val menuName: String = "",

    /**
     * Ringkasan jenis menu
     * Contoh: "Nasi • Ayam • Sayur"
     */
    val menuSummary: String = "",
    val menuScore: Int = 0,


    /* ===== MURID ===== */
    val muridId: String = "",
    val muridName: String = "",

    /* ===== SEKOLAH ===== */
    val schoolId: String = "",
    val schoolName: String = "",

    /* ===== LAPORAN ===== */
    val reason: String = "",
    val description: String = "",

    val status: String = "PENDING",
    val createdAt: Timestamp = Timestamp.now()
)
