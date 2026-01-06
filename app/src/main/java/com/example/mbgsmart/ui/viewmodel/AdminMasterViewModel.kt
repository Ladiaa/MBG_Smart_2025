package com.example.mbgsmart.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Catering
import com.example.mbgsmart.data.model.Sekolah
import com.example.mbgsmart.data.repository.AdminRepository

class AdminMasterViewModel : ViewModel() {

    private val repository = AdminRepository()

    private val _schools = mutableStateOf<List<Sekolah>>(emptyList())
    val schools: State<List<Sekolah>> = _schools

    private val _caterings = mutableStateOf<List<Catering>>(emptyList())
    val caterings: State<List<Catering>> = _caterings

    init {
        repository.listenSchools { list ->
            _schools.value = list
        }

        repository.listenCaterings { list ->
            _caterings.value = list
        }
    }

    /* ================= SCHOOL ================= */

    fun addSchool(
        namaSekolah: String,
        alamat: String
    ) {
        repository.addSchool(
            namaSekolah = namaSekolah,
            alamat = alamat,
            onSuccess = {},
            onFailure = { it.printStackTrace() }
        )
    }

    /* ================= CATERING ================= */

    fun addCatering(
        name: String,
        phone: String
    ) {
        repository.addCatering(
            name = name,
            phone = phone,
            onSuccess = {},
            onFailure = { it.printStackTrace() }
        )
    }
}
