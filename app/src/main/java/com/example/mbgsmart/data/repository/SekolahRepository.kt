package com.example.mbgsmart.data.repository

import com.example.mbgsmart.data.model.Sekolah
import com.google.firebase.firestore.FirebaseFirestore

class SekolahRepository {

    private val db = FirebaseFirestore.getInstance()
    private val col = db.collection("sekolah")

    fun saveSekolah(
        sekolah: Sekolah,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        col.document(sekolah.uid)
            .set(sekolah)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getSekolah(
        uid: String,
        onSuccess: (Sekolah?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        col.document(uid)
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObject(Sekolah::class.java))
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun checkIdentity(uid: String, onResult: (Boolean) -> Unit) {
        col.document(uid).get()
            .addOnSuccessListener { onResult(it.exists()) }
            .addOnFailureListener { onResult(false) }
    }
}
