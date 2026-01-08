package com.example.mbgsmart.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.mbgsmart.ui.components.SekolahScaffold
import com.example.mbgsmart.ui.sekolah.*
import com.example.mbgsmart.ui.viewmodel.MenuViewModel

fun NavGraphBuilder.sekolahNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.SEKOLAH_ROOT,
        startDestination = Routes.SEKOLAH_IDENTITY
    ) {

        /* ================= IDENTITY ================= */
        composable(Routes.SEKOLAH_IDENTITY) {
            IdentityScreen(
                onSaveSuccess = {
                    navController.navigate(Routes.SEKOLAH_UPLOAD) {
                        popUpTo(Routes.SEKOLAH_IDENTITY) { inclusive = true }
                    }
                },
                onNavigate = { navController.navigate(it) }
            )
        }

        /* ================= UPLOAD ================= */
        composable(Routes.SEKOLAH_UPLOAD) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.SEKOLAH_ROOT)
            }
            val menuVM: MenuViewModel = viewModel(parentEntry)

            SekolahScaffold(
                currentRoute = Routes.SEKOLAH_UPLOAD,
                navController = navController
            ) { padding ->
                UploadMenuScreen(
                    paddingValues = padding,
                    onUploadSuccess = { menu ->
                        menuVM.setMenuForAnalysis(menu)
                        navController.navigate(Routes.SEKOLAH_ANALYSIS)
                    },
                    onNavigate = { navController.navigate(it) }
                )
            }
        }

        /* ================= ANALYSIS ================= */
        composable(Routes.SEKOLAH_ANALYSIS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.SEKOLAH_ROOT)
            }
            val menuVM: MenuViewModel = viewModel(parentEntry)
            val menu = menuVM.menuForAnalysis.value

            if (menu == null) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                AnalisisResultScreen(
                    menu = menu,
                    menuViewModel = menuVM,
                    onFinish = {
                        menuVM.clearMenuForAnalysis()
                        // --- PERBAIKAN NAVIGASI DI SINI ---
                        navController.navigate(Routes.SEKOLAH_HISTORY) {
                            // Membersihkan semua dari SEKOLAH_ROOT ke atas
                            popUpTo(Routes.SEKOLAH_UPLOAD) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        /* ================= HISTORY ================= */
        composable(Routes.SEKOLAH_HISTORY) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.SEKOLAH_ROOT)
            }
            val menuVM: MenuViewModel = viewModel(parentEntry)

            LaunchedEffect(Unit) {
                menuVM.loadMenus()
            }

            SekolahScaffold(
                currentRoute = Routes.SEKOLAH_HISTORY,
                navController = navController
            ) {padding ->
                HistoryScreen(
                    menuViewModel = menuVM,
                    onNavigate = { navController.navigate(it) },
                    onEditMenu = { menu ->
                        menuVM.setMenuForAnalysis(menu)
                        navController.navigate(Routes.SEKOLAH_UPLOAD)
                    }
                )
            }
        }

        /* ================= LEADERBOARD ================= */
        composable(Routes.SEKOLAH_LEADERBOARD) {
            SekolahScaffold(
                currentRoute = Routes.SEKOLAH_LEADERBOARD,
                navController = navController
            ) {
                LeaderboardScreen(
                    onNavigate = { navController.navigate(it) }
                )
            }
        }

        /* ================= PROFILE ================= */
        composable(Routes.SEKOLAH_PROFILE) {
            SekolahScaffold(
                currentRoute = Routes.SEKOLAH_PROFILE,
                navController = navController
            ) {
                ProfileScreen(
                    onNavigate = { navController.navigate(it) },
                    onLogout = {
                        navController.navigate(Routes.SELECT_ROLE) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}
