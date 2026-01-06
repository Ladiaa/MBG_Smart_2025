package com.example.mbgsmart.data.repository

import com.example.mbgsmart.data.model.Catering
import com.example.mbgsmart.data.model.DashboardStats
import com.example.mbgsmart.data.model.Sekolah
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminRepository {

    private val db = FirebaseFirestore.getInstance()

    /* ================= LIST SCHOOL (MASTER) ================= */
    fun listenSchools(onUpdate: (List<Sekolah>) -> Unit) {
        db.collection("sekolah_master")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(Sekolah::class.java) }
                    ?: emptyList()
                onUpdate(list)
            }
    }

    /* ================= ADD SCHOOL ================= */
    fun addSchool(
        namaSekolah: String,
        alamat: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val data = mapOf(
            "namaSekolah" to namaSekolah,
            "alamat" to alamat
        )

        db.collection("sekolah_master")
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    /* ================= LIST CATERING ================= */
    fun listenCaterings(onUpdate: (List<Catering>) -> Unit) {
        db.collection("catering")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(Catering::class.java) }
                    ?: emptyList()
                onUpdate(list)
            }
    }

    /* ================= ADD CATERING ================= */
    fun addCatering(
        name: String,
        phone: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val data = mapOf(
            "name" to name,
            "phone" to phone
        )

        db.collection("catering")
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    /* ================= DASHBOARD STATS ================= */
    fun observeDashboardStats(
        onUpdate: (DashboardStats) -> Unit
    ): List<ListenerRegistration> {

        val listeners = mutableListOf<ListenerRegistration>()
        var currentStats = DashboardStats()

        fun emit() = onUpdate(currentStats)

        // TOTAL SEKOLAH
        listeners += db.collection("sekolah_master")
            .addSnapshotListener { snapshot, _ ->
                currentStats = currentStats.copy(
                    totalSchools = snapshot?.size() ?: 0
                )
                emit()
            }

        // TOTAL MENU
        listeners += db.collection("menus")
            .addSnapshotListener { snapshot, _ ->
                currentStats = currentStats.copy(
                    totalMenus = snapshot?.size() ?: 0
                )
                emit()
            }

        // TOTAL REPORT
        listeners += db.collection("reports")
            .addSnapshotListener { snapshot, _ ->
                currentStats = currentStats.copy(
                    totalReports = snapshot?.size() ?: 0
                )
                emit()
            }

        // TOTAL CATERING
        listeners += db.collection("catering")
            .addSnapshotListener { snapshot, _ ->
                currentStats = currentStats.copy(
                    totalCaterings = snapshot?.size() ?: 0
                )
                emit()
            }

        return listeners
    }
}
