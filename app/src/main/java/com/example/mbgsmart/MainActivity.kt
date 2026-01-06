//Anggota Kelompok Qyfi
//Ladia Iffani Mardatiilah - 23523274
//Taqya Maritsa Hakim - 23523108
//
// login untuk admin
// email: admin@mbg.com
// pass: admin123

package com.example.mbgsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.model.Report
import com.example.mbgsmart.ui.admin.*
import com.example.mbgsmart.ui.auth.SelectRoleScreen
import com.example.mbgsmart.ui.murid.*
import com.example.mbgsmart.ui.sekolah.*
import com.example.mbgsmart.ui.theme.MBGSmartTheme
import com.example.mbgsmart.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MBGSmartTheme {

                /* ================= VIEWMODEL ================= */
                val authVM: AuthViewModel = viewModel()
                val sekolahVM: SekolahViewModel = viewModel()
                val menuViewModel: MenuViewModel = viewModel()

                val currentUser by authVM.currentUser.collectAsState()

                /* ================= NAV STATE ================= */
                var currentScreen by remember { mutableStateOf("select_role") }

                /* ================= SHARED STATE ================= */
                var selectedMenuForReport by remember { mutableStateOf<Menu?>(null) }
                var selectedReport by remember { mutableStateOf<Report?>(null) }
                var muridSchoolName by remember { mutableStateOf<String?>(null) }
                var selectedMenu by remember { mutableStateOf<Menu?>(null) }

                fun resetState() {
                    selectedMenuForReport = null
                    selectedReport = null
                    muridSchoolName = null
                    selectedMenu = null
                }

                /* ================= ROUTING ================= */
                when (currentScreen) {

                    /* ========= ROLE ========= */
                    "select_role" -> SelectRoleScreen(
                        onSelectSekolah = { currentScreen = "login_sekolah" },
                        onSelectMurid = { currentScreen = "login_murid" },
                        onSelectAdmin = { currentScreen = "login_admin" }
                    )

                    /* ========= SEKOLAH ========= */
                    "login_sekolah" -> LoginScreen(
                        onLoginSuccess = { user ->
                            sekolahVM.checkIdentity(user.uid) { hasIdentity ->
                                currentScreen =
                                    if (hasIdentity) "sekolah_upload"
                                    else "sekolah_identity"
                            }
                        },
                        onNavigateToRegister = { currentScreen = "register_sekolah" },
                        onNavigateToForgot = {}
                    )

                    "register_sekolah" -> RegisterScreen(
                        onRegisterSuccess = { currentScreen = "sekolah_identity" },
                        onNavigateToLogin = { currentScreen = "login_sekolah" }
                    )

                    "sekolah_identity" -> IdentityScreen(
                        onSaveSuccess = { currentScreen = "sekolah_upload" },
                        onNavigate = { currentScreen = it }
                    )

                    "sekolah_upload" -> UploadMenuScreen(
                        onUploadSuccess = { menu ->
                            selectedMenu = menu
                        },
                        onNavigate = { currentScreen = it }
                    )

                    "sekolah_analysis" -> AnalisisResultScreen(
                        menu = selectedMenu!!,
                        onFinish = {
                            currentScreen = "sekolah_history"
                        }
                    )

                    "sekolah_history" -> HistoryScreen(
                        onNavigate = { currentScreen = it },
                        onEditMenu = { menu ->
                            selectedMenu = menu
                            currentScreen = "sekolah_upload"
                        }
                    )


                    "sekolah_leaderboard" -> LeaderboardScreen(
                        currentScreen = "sekolah_leaderboard",
                        onNavigate = { currentScreen = it }
                    )

                    "sekolah_profile" -> ProfileScreen(
                        onNavigate = { currentScreen = it },
                        onLogout = {
                            authVM.logout()
                            resetState()
                            currentScreen = "select_role"
                        }
                    )

                    /* ========= MURID ========= */
                    "login_murid" -> LoginMurid(
                        onLoginSuccess = { schoolName ->
                            muridSchoolName = schoolName
                            currentScreen = "murid_home"
                        },
                        onNavigateToRegister = {
                            currentScreen = "register_murid"
                        }
                    )


                    "register_murid" -> RegisterMurid(
                        onRegisterSuccess = { schoolName ->
                            muridSchoolName = schoolName
                            currentScreen = "murid_home"
                        },
                        onNavigateToLogin = { currentScreen = "login_murid" }
                    )

                    "murid_home" -> {
                        muridSchoolName?.let { schoolName ->
                            HomeScreenMurid(
                                schoolName = schoolName,
                                onNavigate = { currentScreen = it },
                                onReportMenu = { menu ->
                                    selectedMenuForReport = menu
                                    currentScreen = "murid_report"
                                }
                            )
                        } ?: run {
                            currentScreen = "login_murid"
                        }
                    }


                    "murid_report" -> {
                        selectedMenuForReport?.let { menu ->
                            ReportMenuScreen(
                                menu = menu,
                                onSubmitSuccess = {
                                    selectedMenuForReport = null
                                    currentScreen = "murid_history"
                                }
                            )
                        } ?: run {
                            currentScreen = "murid_home"
                        }
                    }

                    "murid_history" -> ReportHistoryScreen(
                        onNavigate = { currentScreen = it },
                        onViewDetail = {
                            selectedReport = it
                            currentScreen = "murid_report_detail"
                        }
                    )

                    "murid_report_detail" -> {
                        selectedReport?.let {
                            ReportDetailScreen(
                                report = it,
                                onBack = {
                                    selectedReport = null
                                    currentScreen = "murid_history"
                                }
                            )
                        }
                    }

                    "murid_leaderboard" -> LeaderboardScreenMurid(
                        onNavigate = { currentScreen = it }
                    )

                    "murid_profile" -> ProfileMuridScreen(
                        onNavigate = { currentScreen = it },
                        onLogoutSuccess = {
                            authVM.logout()
                            resetState()
                            currentScreen = "select_role"
                        }
                    )

                    "murid_edit_profile" -> EditProfileScreen(
                        onNavigate = { currentScreen = it }
                    )

                    "murid_change_password" -> ChangePasswordScreen(
                        onNavigate = { currentScreen = it },
                        onSubmit = { _, _ ->
                            currentScreen = "murid_profile"
                        }
                    )

                    /* ========= ADMIN ========= */
                    "login_admin" -> LoginScreenAdmin(
                        onLoginSuccess = { currentScreen = "admin_dashboard" }
                    )

                    "admin_dashboard" -> AdminDashboardScreen(
                        currentScreen = "admin_dashboard",
                        onNavigate = { currentScreen = it }
                    )

                    "admin_menu" -> AdminMenuScreen(
                        currentScreen = "admin_menu",
                        onNavigate = { currentScreen = it }
                    )

                    "admin_report" -> AdminReportScreen(
                        currentScreen = "admin_report",
                        onNavigate = { currentScreen = it }
                    )

                    "admin_school" -> AdminSchoolScreen(
                        currentScreen = "admin_school",
                        onNavigate = { currentScreen = it }
                    )

                    "admin_catering" -> AdminCateringScreen(
                        currentScreen = "admin_catering",
                        onNavigate = { currentScreen = it }
                    )

                    else -> {
                        resetState()
                        currentScreen = "select_role"
                    }
                }
            }
        }
    }
}
