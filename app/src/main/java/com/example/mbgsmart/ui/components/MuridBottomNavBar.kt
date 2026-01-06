package com.example.mbgsmart.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mbgsmart.ui.theme.BrandDarkBlue

@Composable
fun MuridBottomNavBar(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(containerColor = BrandDarkBlue) {
        BottomNavItem("Home", "murid_home", Icons.Filled.Home, currentScreen, onNavigate)
        BottomNavItem("History", "murid_history", Icons.Filled.History, currentScreen, onNavigate)
        BottomNavItem("Leaderboard", "murid_leaderboard", Icons.Filled.Leaderboard, currentScreen, onNavigate)
        BottomNavItem("Profile", "murid_profile", Icons.Filled.Person, currentScreen, onNavigate)
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
                icon,
                contentDescription = label,
                tint = if (selected) Color.White else Color.White.copy(alpha = 0.7f)
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) Color.White else Color.White.copy(alpha = 0.7f)
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = BrandDarkBlue
        )
    )
}
