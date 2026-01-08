package com.example.mbgsmart.ui.navigation

object Routes {

    const val SELECT_ROLE = "select_role"

    /* ================= SEKOLAH ================= */
    const val LOGIN_SEKOLAH = "login_sekolah"
    const val REGISTER_SEKOLAH = "register_sekolah"
    const val SEKOLAH_ROOT = "sekolah_root"
    const val SEKOLAH_IDENTITY = "sekolah_identity"
    const val SEKOLAH_UPLOAD = "sekolah_upload"
    const val SEKOLAH_ANALYSIS = "sekolah_analysis"
    const val SEKOLAH_HISTORY = "sekolah_history"
    const val SEKOLAH_LEADERBOARD = "sekolah_leaderboard"
    const val SEKOLAH_PROFILE = "sekolah_profile"

    /* ================= MURID ================= */
    const val LOGIN_MURID = "login_murid"
    const val REGISTER_MURID = "register_murid"

    object MuridRoutes {
        const val ROOT = "murid_root"
        const val HOME = "murid_home"
        const val REPORT_MENU = "murid_report_menu"
        const val HISTORY = "murid_history"
        const val REPORT_DETAIL = "murid_report_detail"
        const val PROFILE = "murid_profile"
        const val LEADERBOARD = "murid_leaderboard"
    }

    /* ================= ADMIN ================= */
    object AdminRoutes {
        const val ROOT = "admin_root"
        const val LOGIN = "login_admin"
        const val DASHBOARD = "admin_dashboard"
        const val SCHOOL = "admin_school"
        const val CATERING = "admin_catering"
        const val MENU = "admin_menu"
        const val REPORT = "admin_report"
    }
}
