package com.example.chitaysostav.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chitaysostav.presentation.edit.EditProductScreen
import com.example.chitaysostav.presentation.history.HistoryScreen
import com.example.chitaysostav.presentation.home.HomeScreen
import com.example.chitaysostav.presentation.product.ProductScreen
import com.example.chitaysostav.presentation.profile.PrivacyPolicyScreen
import com.example.chitaysostav.presentation.profile.ProfileScreen
import com.example.chitaysostav.presentation.scan.CameraScreen
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.AccentGreenDim
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.TextDisabled

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    val bottomNavRoutes = listOf(Screen.Home.route, Screen.History.route, Screen.Profile.route)
    val showBottomBar = currentRoute?.destination?.route in bottomNavRoutes

    Scaffold(
        bottomBar = { if (showBottomBar) AppBottomBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) +
                fadeIn(tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) +
                fadeOut(tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) +
                fadeIn(tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) +
                fadeOut(tween(300))
            }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onScan = { navController.navigate(Screen.Camera.route) })
            }
            composable(Screen.History.route) {
                HistoryScreen(onProductHistoryClick = { barcode ->
                    navController.navigate(Screen.Product.route(barcode))
                })
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onPrivacyPolicyClick = { navController.navigate(Screen.PrivacyPolicy.route) })
            }
            composable(Screen.Camera.route) {
                CameraScreen(
                    onBarcodeDetected = { barcode ->
                        navController.navigate(Screen.Product.route(barcode)) {
                            popUpTo(Screen.Camera.route) { inclusive = true }
                        }
                    }, onClose = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Product.route,
                arguments = listOf(
                    navArgument("barcode") { type = NavType.StringType },
                    navArgument("timestamp") { type = NavType.StringType })
            ) { entry ->
                val barcode = entry.arguments?.getString("barcode") ?: return@composable
                ProductScreen(
                    barcode = barcode,
                    onScanAgain = { navController.navigate(Screen.Camera.route) },
                    onEdit = { navController.navigate(Screen.Edit.route(barcode)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Edit.route,
                arguments = listOf(
                    navArgument("barcode") { type = NavType.StringType },
                    navArgument("timestamp") { type = NavType.StringType }
                )
            ) { entry ->
                val barcode = entry.arguments?.getString("barcode") ?: return@composable
                EditProductScreen(
                    barcode = barcode,
                    onSaved = {
                        navController.navigate(Screen.Product.route(barcode)) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.PrivacyPolicy.route) {
                PrivacyPolicyScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}


@Composable
private fun AppBottomBar(navController: NavController) {
    val current by navController.currentBackStackEntryAsState()

    NavigationBar(containerColor = Background) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGreen,
            selectedTextColor = AccentGreen,
            indicatorColor = AccentGreenDim,
            unselectedIconColor = TextDisabled,
            unselectedTextColor = TextDisabled,
        )
        NavigationBarItem(
            selected = current?.destination?.route == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("Главная") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = current?.destination?.route == Screen.History.route,
            onClick = {
                navController.navigate(Screen.History.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Filled.History, contentDescription = null) },
            label = { Text("История") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = current?.destination?.route == Screen.Profile.route,
            onClick = {
                navController.navigate(Screen.Profile.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            label = { Text("Профиль") },
            colors = itemColors
        )
    }
}