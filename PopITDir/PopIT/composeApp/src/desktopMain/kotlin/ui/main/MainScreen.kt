package ui.main

import ui.admins.ManageEmployeesScreen
import ui.products.ProductDetailsScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import appDesign.PopItTheme
import domain.model.EmployeeType
import nav.LeftNavigationItems
import nav.NavigationMenu
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import popit.composeapp.generated.resources.Res
import popit.composeapp.generated.resources.compose_multiplatform
import popit.composeapp.generated.resources.logo
import ui.admins.LoginScreen
import ui.admins.ManageEmployeeServicesScreen
import ui.home.HomeScreen
import ui.orders.OrderDetailsScreen
import ui.orders.OrdersScreen
import ui.products.ManageProductsScreen
import ui.products.ProductsScreen
import ui.services.ManageServicesScreen
import ui.services.ServiceDetailsScreen
import ui.services.ServicesScreen
import ui.viewmodels.MainScreenViewModel

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val mainScreenViewModel = koinViewModel<MainScreenViewModel> {
        parametersOf(0, "", false)
    }

    var isLoggedIn by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf<EmployeeType?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        if (isLoggedIn) {
            Column(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .background(PopItTheme.colors.primary),
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PopItTheme.spacing.medium16),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(resource = Res.drawable.logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .aspectRatio(1f/1f),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(PopItTheme.spacing.medium16))

                NavigationMenu(navController = navController, userType = userType, onLogout = {
                    isLoggedIn = false
                    userType = null
                    mainScreenViewModel.setEmployeeDetails(0, "", false)
                })
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) LeftNavigationItems.Home.route else LeftNavigationItems.Login.route,
            ) {
                composable(LeftNavigationItems.Login.route) {
                    LoginScreen(
                        onLoginSuccess = { employeeId, employeeName, isAdmin, loggedInUserType ->
                            isLoggedIn = true
                            userType = loggedInUserType
                            mainScreenViewModel.setEmployeeDetails(
                                employeeId,
                                employeeName,
                                isAdmin
                            )
                            navController.navigate(LeftNavigationItems.Home.route)
                        }
                    )
                }

                composable(LeftNavigationItems.Home.route) {
                    val employeeId = mainScreenViewModel.retrieveEmployeeId()
                    val isAdmin = mainScreenViewModel.retrieveIsAdmin()
                    if (employeeId != null && isAdmin != null) {
                        HomeScreen(employeeId = employeeId, isAdmin = isAdmin)
                    }
                }

                composable(LeftNavigationItems.Orders.route) { OrdersScreen(navController) }

                composable("${LeftNavigationItems.OrderDetails.route}/{orderId}") { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId")?.toIntOrNull()
                    if (orderId != null) {
                        OrderDetailsScreen(navController, orderId)
                    }
                }

                composable(LeftNavigationItems.Products.route) { ProductsScreen(navController = navController) }

                composable("${LeftNavigationItems.ProductDetails.route}/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                    if (productId != null) {
                        ProductDetailsScreen(navController, productId)
                    }
                }

                composable(LeftNavigationItems.Services.route) {
                    ServicesScreen(
                        navController = navController
                    )
                }

                composable("${LeftNavigationItems.ServiceRequestDetails.route}/{requestId}/{serviceId}") { backStackEntry ->
                    val requestId = backStackEntry.arguments?.getString("requestId")?.toIntOrNull()
                    val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()
                    val employeeId = mainScreenViewModel.retrieveEmployeeId()
                    val employeeName = mainScreenViewModel.retrieveEmployeeName()
                    if (requestId != null && employeeId != null && employeeName != null && serviceId != null) {
                        ServiceDetailsScreen(
                            navController,
                            requestId,
                            employeeId,
                            serviceId,
                            employeeName
                        )
                    }
                }

                composable(LeftNavigationItems.ManageProducts.route) {
                    val employeeId = mainScreenViewModel.retrieveEmployeeId()
                    if (employeeId != null) {
                        ManageProductsScreen(employeeId)
                    }
                }

                composable(LeftNavigationItems.ManageEmployees.route) {
                    ManageEmployeesScreen()
                }

                composable(LeftNavigationItems.ManageServices.route) {
                    val employeeId = mainScreenViewModel.retrieveEmployeeId()
                    if (employeeId != null) {
                        ManageServicesScreen(employeeId)
                    }
                }

                composable(LeftNavigationItems.ManageEmployeeServices.route) {
                    ManageEmployeeServicesScreen()
                }
            }
        }
    }
}
