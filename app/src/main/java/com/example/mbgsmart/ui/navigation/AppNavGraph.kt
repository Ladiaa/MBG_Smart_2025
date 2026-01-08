package com.example.mbgsmart.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.mbgsmart.ui.auth.SelectRoleScreen
import com.example.mbgsmart.ui.admin.LoginAdminScreen
import com.example.mbgsmart.ui.murid.LoginMurid
import com.example.mbgsmart.ui.murid.RegisterMurid
import com.example.mbgsmart.ui.sekolah.*
import com.example.mbgsmart.ui.viewmodel.SekolahViewModel


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SELECT_ROLE
    ) {

        /* ================= SELECT ROLE ================= */
        composable(Routes.SELECT_ROLE) {
            SelectRoleScreen(
                onSelectSekolah = {
                    navController.navigate(Routes.LOGIN_SEKOLAH)
                },
                onSelectMurid = {
                    navController.navigate(Routes.LOGIN_MURID)
                },
                onSelectAdmin = {
                    navController.navigate(Routes.AdminRoutes.LOGIN)
                }
            )
        }

        /* ================= ADMIN LOGIN ================= */
        composable(Routes.AdminRoutes.LOGIN) {
            LoginAdminScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.AdminRoutes.ROOT) {
                        popUpTo(Routes.SELECT_ROLE) { inclusive = true }
                    }
                }
            )
        }

        /* ================= MURID AUTH ================= */
        composable(Routes.LOGIN_MURID) {
            LoginMurid(
                onLoginSuccess = {
                    navController.navigate(Routes.MuridRoutes.ROOT) {
                        popUpTo(Routes.LOGIN_MURID) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER_MURID)
                }
            )
        }

        composable(Routes.REGISTER_MURID) {
            RegisterMurid(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN_MURID)
                },
                onRegisterSuccess = { user ->
                    navController.navigate(Routes.MuridRoutes.HOME) {
                        popUpTo(Routes.REGISTER_MURID) { inclusive = true }
                    }
                }
            )
        }


        /* ================= SEKOLAH AUTH ================= */
        composable(Routes.LOGIN_SEKOLAH) {
            val sekolahVM: SekolahViewModel = viewModel()

            LoginScreen(
                onLoginSuccess = { user ->
                    sekolahVM.loadSekolah(
                        userId = user.uid,
                        onResult = { hasData ->
                            if (hasData) {
                                navController.navigate(Routes.SEKOLAH_UPLOAD) {
                                    popUpTo(Routes.LOGIN_SEKOLAH) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Routes.SEKOLAH_IDENTITY) {
                                    popUpTo(Routes.LOGIN_SEKOLAH) { inclusive = true }
                                }
                            }
                        }
                    )
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER_SEKOLAH)
                }
            )

        }


        composable(Routes.REGISTER_SEKOLAH) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN_SEKOLAH)
                },
                onRegisterSuccess = { user ->
                    navController.navigate(Routes.SEKOLAH_IDENTITY) {
                        popUpTo(Routes.REGISTER_SEKOLAH) { inclusive = true }
                    }
                }
            )
        }
        /* ================= ROOT GRAPHS ================= */
        muridNavGraph(navController)
        sekolahNavGraph(navController)
        adminNavGraph(navController)
    }
}
