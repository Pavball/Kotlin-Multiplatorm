package ui.main

import AppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import composetesting.composeapp.generated.resources.Res
import composetesting.composeapp.generated.resources.tmdb_logo
import nav.BottomNavigationBar
import nav.BottomNavigationItems
import org.jetbrains.compose.resources.painterResource
import ui.details.DetailsScreen
import ui.favorites.FavoritesScreen
import ui.home.HomeScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopToolbar(navController) },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = BottomNavigationItems.Home.route,
        ) {
            composable(BottomNavigationItems.Home.route) { HomeScreen(navController) }
            composable(BottomNavigationItems.Favorites.route) { FavoritesScreen(navController) }
            composable("${BottomNavigationItems.Details.route}/{movieId}/{movieType}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                val movieType = backStackEntry.arguments?.getString("movieType")
                if (movieId != null && movieType != null) {
                    DetailsScreen(navController, movieId, movieType)
                }
            }
        }
    }
}

@Composable
fun TopToolbar(navController: NavController, modifier: Modifier = Modifier) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    TopAppBar(
        modifier = modifier.background(AppTheme.colors.primary),
        title = {
            Box(
                modifier = modifier.fillMaxSize()
                    .padding(end = AppTheme.spacing.extraLarge),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.padding(AppTheme.spacing.medium),
                    painter = painterResource(resource = Res.drawable.tmdb_logo),
                    contentDescription = null,
                )
            }
        },
        backgroundColor = AppTheme.colors.primary,
        navigationIcon = {
            if (currentRoute?.startsWith("details") == true) {
                IconButton(onClick = {
                    navController.navigate("home")
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AppTheme.colors.background
                    )
                }
            }
        }
    )
}