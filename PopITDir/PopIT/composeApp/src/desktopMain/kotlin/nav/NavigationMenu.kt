package nav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import appDesign.PopItTheme
import domain.model.EmployeeType

@Composable
fun NavigationMenu(navController: NavController, userType: EmployeeType?, onLogout: () -> Unit) {
    // Remember the current route and update it whenever the navigation stack changes
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = when (userType) {
        EmployeeType.Admin -> listOf(
            LeftNavigationItems.Home,
            LeftNavigationItems.ManageProducts,
            LeftNavigationItems.ManageEmployees,
            LeftNavigationItems.ManageServices,
            LeftNavigationItems.ManageEmployeeServices
        )

        EmployeeType.Employee -> listOf(
            LeftNavigationItems.Home,
            LeftNavigationItems.Orders,
            LeftNavigationItems.Products,
            LeftNavigationItems.Services
        )

        else -> emptyList()
    }

    Column {
        Divider(color = PopItTheme.colors.background, thickness = 1.dp)

        items.forEach { item ->
            TextButton(
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = item.title,
                    color = if (currentRoute == item.route) PopItTheme.colors.background else Color.Black
                )
            }

            Divider(color = PopItTheme.colors.background, thickness = 1.dp)
        }

        // Add logout button
        TextButton(
            onClick = {
                onLogout()
                navController.navigate(LeftNavigationItems.Login.route) {
                    popUpTo(navController.graph.startDestinationRoute ?: "login") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Logout", color = PopItTheme.colors.error)
        }
    }
}
