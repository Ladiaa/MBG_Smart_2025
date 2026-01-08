package com.example.mbgsmart.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Catering
import com.example.mbgsmart.data.model.SekolahMaster
import com.example.mbgsmart.data.repository.AdminMasterRepository
import com.google.firebase.firestore.ListenerRegistration

class AdminMasterViewModel : ViewModel() {

    private val repository = AdminMasterRepository()

    private val _schools = mutableStateOf<List<SekolahMaster>>(emptyList())
    val schools: State<List<SekolahMaster>> = _schools

    private val _caterings = mutableStateOf<List<Catering>>(emptyList())
    val caterings: State<List<Catering>> = _caterings

    private var schoolListener: ListenerRegistration? = null
    private var cateringListener: ListenerRegistration? = null

    init {
        startListeningSchools()
        startListeningCaterings()
    }

    /* ================= READ ================= */

    private fun startListeningSchools() {
        if (schoolListener != null) return
        schoolListener = repository.listenAllSchools {
            _schools.value = it
        }
    }

    private fun startListeningCaterings() {
        if (cateringListener != null) return
        cateringListener = repository.listenAllCaterings {
            _caterings.value = it
        }
    }

    /* ================= CREATE ================= */

    fun addSchool(
        name: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (name.isBlank()) return

        repository.addSchool(
            SekolahMaster(namaSekolah = name),
            onSuccess,
            onFailure
        )
    }

    fun addCatering(
        name: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (name.isBlank()) return

        repository.addCatering(
            Catering(name = name),
            onSuccess,
            onFailure
        )
    }

    override fun onCleared() {
        schoolListener?.remove()
        cateringListener?.remove()
        super.onCleared()
    }
}
