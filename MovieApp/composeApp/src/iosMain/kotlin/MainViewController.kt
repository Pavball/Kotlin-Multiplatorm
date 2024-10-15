import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import ui.home.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
)
{ App() }