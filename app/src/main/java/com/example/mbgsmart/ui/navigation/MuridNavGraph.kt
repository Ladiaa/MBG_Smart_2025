package com.example.mbgsmart.ui.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.ui.murid.*
import com.example.mbgsmart.ui.viewmodel.MenuViewModel
import com.example.mbgsmart.ui.viewmodel.ReportViewModel

fun NavGraphBuilder.muridNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.MuridRoutes.ROOT,
        startDestination = Routes.MuridRoutes.HOME
    ) {

        /* ================= HOME ================= */
        composable(Routes.MuridRoutes.HOME) {
            val menuViewModel: MenuViewModel = viewModel()

            HomeScreenMurid(
                onNavigate = { navController.navigate(it) },
                onReportMenu = { menu ->
                    if (menu.id.isNotBlank()) {
                        navController.navigate(
                            "${Routes.MuridRoutes.REPORT_MENU}/${menu.id}"
                        )
                    }
                }
            )
        }

        /* ================= REPORT MENU ================= */
        composable(
            route = "${Routes.MuridRoutes.REPORT_MENU}/{menuId}",
            arguments = listOf(
                navArgument("menuId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val menuViewModel: MenuViewModel = viewModel()
            val menuId = backStackEntry.arguments?.getString("menuId")


            LaunchedEffect(Unit) {
                if (menuViewModel.menuList.value.isEmpty()) {
                    menuViewModel.loadMenus() // SESUAIKAN DENGAN METHOD KAMU
                }
            }

            if (menuId.isNullOrBlank()) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
                return@composable
            }

            val menu = menuViewModel.menuList.value
                .firstOrNull { it.id == menuId }

            if (menu == null) {
                // tunggu sebentar, jangan langsung pop
                CircularProgressIndicator()
            } else {
                ReportMenuScreen(
                    menu = menu,
                    onSubmitSuccess = {
                        navController.navigate(Routes.MuridRoutes.HISTORY) {
                            popUpTo(Routes.MuridRoutes.REPORT_MENU) { inclusive = true }
                        }
                    }
                )

            }
        }


        /* ================= HISTORY ================= */
        composable(Routes.MuridRoutes.HISTORY) {
            ReportHistoryScreen(
                onNavigate = { navController.navigate(it) },
                onViewDetail = { report ->
                    navController.navigate(
                        "${Routes.MuridRoutes.REPORT_DETAIL}/${report.id}"
                    )
                }
            )
        }

        /* ================= REPORT DETAIL ================= */
        composable(
            route = "${Routes.MuridRoutes.REPORT_DETAIL}/{reportId}",
            arguments = listOf(
                navArgument("reportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val reportViewModel: ReportViewModel = viewModel()
            val reportId = backStackEntry.arguments?.getString("reportId")

            var report by remember { mutableStateOf<Report?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(reportId) {
                if (reportId != null) {
                    reportViewModel.loadReportById(reportId) {
                        report = it
                        isLoading = false
                    }
                }
            }

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                report == null -> {
                    Text("Data laporan tidak ditemukan")
                }
                else -> {
                    ReportDetailScreen(
                        report = report!!,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }


        /* ================= LEADERBOARD ================= */
        composable(Routes.MuridRoutes.LEADERBOARD) {
            LeaderboardScreenMurid(
                onNavigate = { navController.navigate(it) }
            )
        }

        /* ================= PROFILE ================= */
        composable(Routes.MuridRoutes.PROFILE) {
            ProfileScreenMurid(
                onLogout = {
                    navController.navigate(Routes.SELECT_ROLE) {
                        popUpTo(0)
                    }
                },
                onNavigate = { navController.navigate(it) }
            )
        }
    }
}
