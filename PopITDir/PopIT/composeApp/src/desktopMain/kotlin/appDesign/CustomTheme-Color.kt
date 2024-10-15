package appDesign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal class AppColors(
    primary: Color,
    secondary: Color,
    textPrimary: Color,
    background: Color,
    error: Color,
) {
    var primary by mutableStateOf(primary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var error by mutableStateOf(error)
        private set
    var background by mutableStateOf(background)
        private set


    fun lightColors(
        primary: Color = colorLightBlue,
        textPrimary: Color = colorLightTextPrimary,
        secondary: Color = colorLightTextSecondary,
        background: Color = colorLightBackground,
        error: Color = colorLightError,
    ): AppColors = AppColors(
        primary = primary,
        textPrimary = textPrimary,
        secondary = secondary,
        background = background,
        error = error,
    )

}

private val colorLightBlue = Color(0xff1d92bf)
private val colorLightTextPrimary = Color(0xFF000000)
private val colorLightTextSecondary = Color(0xFF6C727A)
private val colorLightBackground = Color(0xFFFFFFFF)
private val colorLightError = Color(0xFFD62222)

internal val LocalColors = staticCompositionLocalOf {
    AppColors(
        colorLightBlue,
        colorLightTextPrimary,
        colorLightTextSecondary,
        colorLightBackground,
        colorLightError
    )
}
