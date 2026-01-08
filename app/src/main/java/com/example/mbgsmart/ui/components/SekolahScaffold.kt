package com.example.mbgsmart.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.mbgsmart.ui.navigation.Routes

@Composable
fun SekolahScaffold(
    currentRoute: String,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Routes.SEKOLAH_ROOT) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        content(padding)
    }
}
