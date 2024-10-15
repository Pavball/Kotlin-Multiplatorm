package nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class LeftNavigationItems(val route: String, val title: String, val icon: ImageVector) {
    data object Home : LeftNavigationItems("home", "Home", Icons.Default.Home)
    data object Orders : LeftNavigationItems("orders", "Orders", Icons.Default.Favorite)
    data object OrderDetails : LeftNavigationItems("orderDetails", "Order Details", Icons.Default.Favorite)
    data object Products : LeftNavigationItems("products", "Products", Icons.Default.Favorite)
    data object ProductDetails : LeftNavigationItems("productDetails", "Product Details", Icons.Default.Favorite)
    data object ManageProducts : LeftNavigationItems("manageProducts", "Manage Products", Icons.Default.Favorite)
    data object ManageEmployees : LeftNavigationItems("manageEmployees", "Manage Employees", Icons.Default.Favorite)
    data object ManageServices : LeftNavigationItems("manageServices", "Manage Services", Icons.Default.Favorite)
    data object ManageEmployeeServices : LeftNavigationItems("manageEmployeeServices", "Manage Employee Services", Icons.Default.Favorite)
    data object Services : LeftNavigationItems("services", "Services", Icons.Default.Favorite)
    data object ServiceRequestDetails: LeftNavigationItems("serviceRequestDetails", "Service Request Details", Icons.Default.Favorite)
    data object Login : LeftNavigationItems("login", "Login", Icons.Default.Favorite)
}
