package com.example.mbgsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mbgsmart.data.model.Sekolah
import com.example.mbgsmart.data.repository.SekolahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SekolahViewModel : ViewModel() {

    private val repository = SekolahRepository()

    private val _sekolah = MutableStateFlow<Sekolah?>(null)
    val sekolah: StateFlow<Sekolah?> = _sekolah

    fun loadSekolah(
        userId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val data = repository.getSekolahByUserId(userId)
            _sekolah.value = data
            onResult(data != null)
        }
    }

    fun saveSekolah(
        sekolah: Sekolah,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.saveSekolah(sekolah)
                _sekolah.value = sekolah
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Gagal menyimpan data sekolah")
            }
        }
    }
}
