package com.example.mbgsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Murid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    /* ================= TAMBAHAN (INI KUNCI) ================= */
    private val _currentMurid = MutableStateFlow<Murid?>(null)
    val currentMurid = _currentMurid.asStateFlow()

    /* ================= LOAD DATA MURID ================= */
    fun loadCurrentMurid() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("murid")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                _currentMurid.value = doc.toObject(Murid::class.java)
            }
    }

    /* ================= REGISTER SEKOLAH ================= */
    fun registerSekolah(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) {
                    onFailure(Exception("User tidak valid"))
                    return@addOnSuccessListener
                }

                auth.currentUser?.reload()
                    ?.addOnCompleteListener {
                        _currentUser.value = auth.currentUser
                        onSuccess(auth.currentUser!!)
                    }
            }
            .addOnFailureListener { onFailure(it) }
    }


    /* ================= REGISTER MURID ================= */
    fun registerMurid(
        name: String,
        email: String,
        password: String,
        schoolId: String,
        schoolName: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user ?: return@addOnSuccessListener
                _currentUser.value = user

                val murid = Murid(
                    uid = user.uid,
                    name = name,
                    email = email,
                    schoolId = schoolId,
                    schoolName = schoolName
                )

                db.collection("murid")
                    .document(user.uid)
                    .set(murid)
                    .addOnSuccessListener {
                        _currentMurid.value = murid
                        onSuccess()
                    }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }

    /* ================= LOGIN ================= */
    fun loginUser(
        email: String,
        pass: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                _currentUser.value = result.user
                loadCurrentMurid()   // 🔥 WAJIB
                result.user?.let { onSuccess(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _currentMurid.value = null
    }
}
