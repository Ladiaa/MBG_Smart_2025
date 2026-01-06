package com.example.mbgsmart.data.repository


import com.example.mbgsmart.data.model.Murid
import com.google.firebase.firestore.FirebaseFirestore

class MuridRepository {

    private val db = FirebaseFirestore.getInstance()

    fun createMurid(
        murid: Murid,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("murid")
            .document(murid.uid)
            .set(murid)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateMurid(
        uid: String,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("murid")
            .document(uid)
            .update(
                mapOf(
                    "name" to name,
                    "email" to email
                )
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun getMurid(
        uid: String,
        onResult: (Murid) -> Unit,
        onNotFound: () -> Unit
    ) {
        db.collection("murid")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    doc.toObject(Murid::class.java)?.let(onResult)
                } else {
                    onNotFound()
                }
            }
            .addOnFailureListener {
                onNotFound()
            }
    }
}

