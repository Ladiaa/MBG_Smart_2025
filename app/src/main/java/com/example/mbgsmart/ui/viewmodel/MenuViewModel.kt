package com.example.mbgsmart.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.repository.MenuRepository
import com.google.firebase.firestore.ListenerRegistration

class MenuViewModel : ViewModel() {

    private val repository = MenuRepository()

    /* ================= STATE ================= */
    private val _menuList = mutableStateOf<List<Menu>>(emptyList())
    val menuList: State<List<Menu>> = _menuList

    private val _leaderboard = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val leaderboard: State<List<Pair<String, Int>>> = _leaderboard

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _schoolName = mutableStateOf("")
    val schoolName: State<String> = _schoolName

    private var menuListener: ListenerRegistration? = null

    /* ================= MENU ANALYSIS ================= */
    private val _menuForAnalysis = mutableStateOf<Menu?>(null)
    val menuForAnalysis: State<Menu?> = _menuForAnalysis

    fun setMenuForAnalysis(menu: Menu) {
        _menuForAnalysis.value = menu
    }

    fun clearMenuForAnalysis() {
        _menuForAnalysis.value = null
    }

    /* ================= CREATE ================= */
    fun createMenu(menu: Menu, onResult: (Boolean) -> Unit) {
        _isLoading.value = true
        repository.createMenu(menu) { success ->
            _isLoading.value = false
            if (!success) _errorMessage.value = "Gagal menyimpan menu"
            onResult(success)
        }
    }

    /* ================= READ ================= */
    fun startListeningMenusBySchoolName(name: String) {
        if (name.isBlank()) return
        stopListening()

        _isLoading.value = true
        menuListener = repository.listenMenusBySchoolName(name) {
            _menuList.value = it
            _isLoading.value = false
        }
    }

    fun startListeningMenusBySchool(schoolId: String) {
        if (schoolId.isBlank()) return
        stopListening()

        _isLoading.value = true
        menuListener = repository.listenMenusBySchool(schoolId) {
            _menuList.value = it
            _isLoading.value = false
            _errorMessage.value = null
        }
    }




    fun startListeningAllMenus() {
        stopListening()
        menuListener = repository.listenAllMenus {
            _menuList.value = it
        }
    }

    fun stopListening() {
        menuListener?.remove()
        menuListener = null
    }

    /* ================= LEADERBOARD ================= */
    fun loadLeaderboard() {
        _isLoading.value = true
        repository.getLeaderboard(
            onResult = {
                _leaderboard.value = it
                _isLoading.value = false
            },
            onFailure = {
                _errorMessage.value = it.message
                _isLoading.value = false
            }
        )
    }

    /* ================= MURID ================= */
    fun loadMuridSchool(schoolName: String) {
        if (schoolName.isBlank()) return
        _schoolName.value = schoolName
    }

    /* ================= GENERAL LOAD (REALTIME) ================= */
    fun loadMenus() {
        // Kalau sudah ada listener & data, jangan buat ulang
        if (menuListener != null && _menuList.value.isNotEmpty()) return

        stopListening()
        _isLoading.value = true

        menuListener = repository.listenAllMenus { menus ->
            _menuList.value = menus
            _isLoading.value = false
        }
    }


    /* ================= DELETE ================= */
    fun deleteMenu(
        menuId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.deleteMenu(
            menuId = menuId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override fun onCleared() {
        stopListening()
        super.onCleared()
    }
}
