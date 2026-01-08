package com.example.mbgsmart.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.mbgsmart.ui.admin.*

fun NavGraphBuilder.adminNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.AdminRoutes.ROOT,
        startDestination = Routes.AdminRoutes.DASHBOARD
    ) {

        composable(Routes.AdminRoutes.DASHBOARD) {
            AdminDashboardScreen(
                currentScreen = Routes.AdminRoutes.DASHBOARD,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(Routes.AdminRoutes.SCHOOL) {
            AdminSchoolScreen()
        }

        composable(Routes.AdminRoutes.CATERING) {
            AdminCateringScreen()
        }

        composable(Routes.AdminRoutes.MENU) {
            AdminMenuScreen(
                currentScreen = Routes.AdminRoutes.MENU,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(Routes.AdminRoutes.REPORT) {
            AdminReportScreen(
                currentScreen = Routes.AdminRoutes.REPORT,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
    }
}
