package com.example.mbgsmart.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.repository.MenuRepository
import com.google.firebase.firestore.ListenerRegistration

class MenuViewModel : ViewModel() {

    private val repository = MenuRepository()

    /* =====================================================
     * STATE MENU
     * ===================================================== */
    private val _menuList = mutableStateOf<List<Menu>>(emptyList())
    val menuList: State<List<Menu>> = _menuList

    private val _leaderboard = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val leaderboard: State<List<Pair<String, Int>>> = _leaderboard

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private var menuListener: ListenerRegistration? = null

    /* =====================================================
     * CREATE (UPLOAD MENU SEKOLAH)
     * ===================================================== */
    fun createMenu(
        menu: Menu,
        onResult: (Boolean) -> Unit
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        repository.createMenu(menu) { success ->
            _isLoading.value = false
            if (!success) {
                _errorMessage.value = "Gagal menyimpan menu"
            }
            onResult(success)
        }
    }

    /* =====================================================
     * READ – REALTIME (HISTORY SEKOLAH)
     * ===================================================== */
    fun startListeningMenusBySchool(
        schoolId: String
    ) {
        if (menuListener != null) return

        menuListener = repository.listenMenusBySchool(schoolId) { menus ->
            _menuList.value = menus
        }
    }

    /* =====================================================
     * READ – REALTIME (MENU UNTUK MURID – FILTER SEKOLAH)
     * ===================================================== */
    fun startListeningMenusBySchoolName(
        schoolName: String
    ) {
        if (menuListener != null) return

        menuListener = repository.listenMenusBySchoolName(schoolName) { menus ->
            _menuList.value = menus
        }
    }

    /* =====================================================
     * READ – REALTIME (SEMUA MENU – ADMIN / MURID)
     * ===================================================== */
    fun startListeningAllMenus() {
        if (menuListener != null) return

        menuListener = repository.listenAllMenus { menus ->
            _menuList.value = menus
        }
    }

    /* =====================================================
     * STOP LISTENER (WAJIB DIPANGGIL)
     * ===================================================== */
    fun stopListening() {
        menuListener?.remove()
        menuListener = null
    }

    /* =====================================================
     * UPDATE MENU (EDIT)
     * ===================================================== */
    fun updateMenu(
        menu: Menu,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.updateMenu(
            menu = menu,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    /* =====================================================
     * DELETE MENU
     * ===================================================== */
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

    /* =====================================================
     * LEADERBOARD (JUMLAH MENU PER SEKOLAH)
     * ===================================================== */
    fun loadLeaderboard() {
        _isLoading.value = true
        _errorMessage.value = null

        repository.getLeaderboard(
            onResult = { result ->
                _leaderboard.value = result
                _isLoading.value = false
            },
            onFailure = { e ->
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        )
    }

    /* =====================================================
     * CLEAR STATE (OPTIONAL)
     * ===================================================== */
    fun clearState() {
        _menuList.value = emptyList()
        _leaderboard.value = emptyList()
        _errorMessage.value = null
        _isLoading.value = false
    }

    override fun onCleared() {
        stopListening()
        super.onCleared()
    }
}
