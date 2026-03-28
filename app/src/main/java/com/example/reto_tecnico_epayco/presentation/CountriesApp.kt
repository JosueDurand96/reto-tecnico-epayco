package com.example.reto_tecnico_epayco.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reto_tecnico_epayco.presentation.components.CountriesBottomBar
import com.example.reto_tecnico_epayco.presentation.navigation.CountriesNavHost
import com.example.reto_tecnico_epayco.presentation.navigation.Routes

@Composable
fun CountriesApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute == Routes.COUNTRIES || currentRoute == Routes.FAVORITES

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                CountriesBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Routes.MAIN_GRAPH) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        CountriesNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
