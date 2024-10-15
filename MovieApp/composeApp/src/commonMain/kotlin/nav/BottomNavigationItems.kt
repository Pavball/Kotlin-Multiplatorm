package nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavigationItems("home", "Home", Icons.Default.Home)
    data object Favorites : BottomNavigationItems("favorites", "Favorites", Icons.Default.Favorite)
    data object Details : BottomNavigationItems("details", "Details", Icons.Default.Favorite)
}
