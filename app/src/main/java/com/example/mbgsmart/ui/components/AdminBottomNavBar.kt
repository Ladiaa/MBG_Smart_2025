package com.example.mbgsmart.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbgsmart.ui.theme.BrandDarkBlue

@Composable
fun AdminBottomNavBar(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = BrandDarkBlue, // 🔥 FIX 1: background navbar
        tonalElevation = 6.dp
    ) {
        BottomNavItem("Dashboard", "admin_dashboard", Icons.Filled.Dashboard, currentScreen, onNavigate)
        BottomNavItem("Menu", "admin_menu", Icons.Filled.RestaurantMenu, currentScreen, onNavigate)
        BottomNavItem("Report", "admin_report", Icons.Filled.Report, currentScreen, onNavigate)
        BottomNavItem("Sekolah", "admin_school", Icons.Filled.School, currentScreen, onNavigate)
        BottomNavItem("Catering", "admin_catering", Icons.Filled.LocalDining, currentScreen, onNavigate)
    }
}

@Composable
private fun RowScope.BottomNavItem(
    label: String,
    route: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    val selected = currentScreen == route

    NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(route) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            selectedTextColor = Color.White,
            unselectedIconColor = Color.White.copy(alpha = 0.6f),
            unselectedTextColor = Color.White.copy(alpha = 0.6f),

            // 🔥 FIX 2: indikator kelihatan
            indicatorColor = Color.White.copy(alpha = 0.18f)
        )
    )
}
