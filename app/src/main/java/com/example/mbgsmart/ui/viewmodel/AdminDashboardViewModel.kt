package com.example.mbgsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.mbgsmart.data.model.DashboardStats
import com.example.mbgsmart.data.repository.AdminRepository
import com.google.firebase.firestore.ListenerRegistration

class AdminDashboardViewModel : ViewModel() {

    private val repository = AdminRepository()

    private val _stats = mutableStateOf(DashboardStats())
    val stats: State<DashboardStats> = _stats

    private var listeners: List<ListenerRegistration> = emptyList()

    fun startListening() {
        if (listeners.isNotEmpty()) return

        listeners = repository.observeDashboardStats {
            _stats.value = it
        }
    }

    override fun onCleared() {
        listeners.forEach { it.remove() }
        super.onCleared()
    }
}
