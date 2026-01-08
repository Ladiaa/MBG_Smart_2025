package com.example.mbgsmart.data.repository

import com.example.mbgsmart.data.model.Sekolah
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SekolahRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getSekolahByUserId(userId: String): Sekolah? {
        val snapshot = db.collection("sekolah")
            .whereEqualTo("uid", userId)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(Sekolah::class.java)
    }

    suspend fun saveSekolah(sekolah: Sekolah) {
        db.collection("sekolah")
            .document(sekolah.id)
            .set(sekolah)
            .await()
    }
}
