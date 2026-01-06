package com.example.mbgsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Sekolah
import com.example.mbgsmart.data.repository.SekolahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SekolahViewModel : ViewModel() {

    private val repository = SekolahRepository()

    private val _sekolah = MutableStateFlow<Sekolah?>(null)
    val sekolah: StateFlow<Sekolah?> = _sekolah

    fun saveSekolah(
        sekolah: Sekolah,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.saveSekolah(
            sekolah = sekolah,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    fun getSekolah(uid: String) {
        repository.getSekolah(
            uid = uid,
            onSuccess = { _sekolah.value = it },
            onFailure = { it.printStackTrace() }
        )
    }

    /* ================= AMBIL DATA SEKOLAH ================= */
    fun loadSekolah(uid: String) {
        repository.getSekolah(
            uid = uid,
            onSuccess = { data ->
                _sekolah.value = data
            },
            onFailure = { error ->
                error.printStackTrace()
            }
        )
    }

    /* ================= CEK APAKAH IDENTITAS SUDAH ADA ================= */
    fun checkIdentity(uid: String, onResult: (Boolean) -> Unit) {
        repository.checkIdentity(uid, onResult)
    }
}
