package com.example.mbgsmart.data.repository

import com.example.mbgsmart.data.model.Catering
import com.example.mbgsmart.data.model.SekolahMaster
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminMasterRepository {

    private val db = FirebaseFirestore.getInstance()

    /* ================= READ ================= */

    fun listenAllSchools(
        onResult: (List<SekolahMaster>) -> Unit
    ): ListenerRegistration {
        return db.collection("master_sekolah")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(SekolahMaster::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onResult(list)
            }
    }

    fun listenAllCaterings(
        onResult: (List<Catering>) -> Unit
    ): ListenerRegistration {
        return db.collection("master_catering")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(Catering::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onResult(list)
            }
    }

    /* ================= CREATE ================= */

    fun addSchool(
        school: SekolahMaster,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("master_sekolah")
            .add(school)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun addCatering(
        catering: Catering,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("master_catering")
            .add(catering)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
