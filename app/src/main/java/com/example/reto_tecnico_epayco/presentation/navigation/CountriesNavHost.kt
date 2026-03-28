package com.example.reto_tecnico_epayco.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.reto_tecnico_epayco.presentation.screens.detail.CountryDetailScreen
import com.example.reto_tecnico_epayco.presentation.screens.favorites.FavoritesScreen
import com.example.reto_tecnico_epayco.presentation.screens.list.CountriesScreen
import com.example.reto_tecnico_epayco.presentation.screens.login.LoginScreen

@Composable
fun CountriesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN_GRAPH) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        navigation(
            route = Routes.MAIN_GRAPH,
            startDestination = Routes.COUNTRIES
        ) {
            composable(Routes.COUNTRIES) {
                CountriesScreen(
                    onCountryClick = { cca3 ->
                        navController.navigate(Routes.detail(cca3))
                    }
                )
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onCountryClick = { cca3 ->
                        navController.navigate(Routes.detail(cca3))
                    }
                )
            }
            composable(
                route = Routes.DETAIL,
                arguments = listOf(
                    navArgument(NavArgs.CCA3) { type = NavType.StringType }
                )
            ) {
                CountryDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
