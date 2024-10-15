package ui.main

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.initKoin

fun main() = application {

    initKoin()

    val windowState = rememberWindowState(
        width = 1920.dp,
        height = 1080.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "PopIT",
        state = windowState
    ) {
        App()
    }
}
