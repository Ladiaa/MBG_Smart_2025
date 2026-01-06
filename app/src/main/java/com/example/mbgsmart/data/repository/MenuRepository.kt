package com.example.mbgsmart.data.repository

import com.example.mbgsmart.data.model.Menu
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MenuRepository {

    private val db = FirebaseFirestore.getInstance()
    private val menuRef = db.collection("menus")

    /* =====================================================
     * CREATE (UPLOAD MENU SEKOLAH)
     * ===================================================== */
    fun createMenu(
        menu: Menu,
        onResult: (Boolean) -> Unit
    ) {
        val doc = menuRef.document()

        val newMenu = menu.copy(
            id = doc.id,
            timestamp = Timestamp.now()
        )

        doc.set(newMenu)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener {
                it.printStackTrace()
                onResult(false)
            }
    }

    /* =====================================================
     * READ – REALTIME (MENU SEKOLAH / HISTORY SEKOLAH)
     * ===================================================== */
    fun listenMenusBySchool(
        schoolId: String,
        onUpdate: (List<Menu>) -> Unit
    ): ListenerRegistration {
        return menuRef
            .whereEqualTo("schoolId", schoolId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                val menus = snapshot?.documents
                    ?.mapNotNull { it.toObject(Menu::class.java) }
                    ?: emptyList()

                onUpdate(
                    menus.sortedByDescending { it.timestamp.seconds }
                )
            }
    }

    /* =====================================================
     * READ – REALTIME (MENU UNTUK MURID – SEMUA SEKOLAH)
     * ===================================================== */
    fun listenAllMenus(
        onUpdate: (List<Menu>) -> Unit
    ): ListenerRegistration {
        return menuRef
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                val menus = snapshot?.documents
                    ?.mapNotNull { it.toObject(Menu::class.java) }
                    ?: emptyList()

                onUpdate(
                    menus.sortedByDescending { it.timestamp.seconds }
                )
            }
    }

    /* =====================================================
     * READ – MENU UNTUK MURID (FILTER SEKOLAH TERTENTU)
     * ===================================================== */
    fun listenMenusBySchoolName(
        schoolName: String,
        onUpdate: (List<Menu>) -> Unit
    ): ListenerRegistration {
        return menuRef
            .whereEqualTo("schoolName", schoolName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                val menus = snapshot?.documents
                    ?.mapNotNull { it.toObject(Menu::class.java) }
                    ?: emptyList()

                onUpdate(
                    menus.sortedByDescending { it.timestamp.seconds }
                )
            }
    }

    /* =====================================================
     * UPDATE (EDIT MENU SEKOLAH)
     * ===================================================== */
    fun updateMenu(
        menu: Menu,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val updatedMenu = menu.copy(
            timestamp = Timestamp.now()
        )

        menuRef.document(menu.id)
            .set(updatedMenu)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /* =====================================================
     * DELETE (HAPUS MENU)
     * ===================================================== */
    fun deleteMenu(
        menuId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        menuRef.document(menuId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /* =====================================================
     * LEADERBOARD (JUMLAH MENU PER SEKOLAH)
     * ===================================================== */
    fun getLeaderboard(
        onResult: (List<Pair<String, Int>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        menuRef.get()
            .addOnSuccessListener { snapshot ->
                val menus = snapshot.documents
                    .mapNotNull { it.toObject(Menu::class.java) }

                // Hitung jumlah menu per sekolah
                val leaderboard = menus
                    .groupBy { it.schoolName }
                    .map { entry ->
                        entry.key to entry.value.size
                    }
                    .sortedByDescending { it.second }

                onResult(leaderboard)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
