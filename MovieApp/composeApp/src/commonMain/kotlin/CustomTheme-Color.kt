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
    progressBar: Color,
    progressBarBackground: Color,
    favoriteButtonBackground : Color,
    starColor : Color,
    isLight: Boolean
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
    var progressBar by mutableStateOf(progressBar)
        private set
    var progressBarBackground by mutableStateOf(progressBarBackground)
        private set
    var favoriteButtonBackground by mutableStateOf(favoriteButtonBackground)
        private set
    var starColor by mutableStateOf(starColor)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun lightColors(
        primary: Color = colorDarkBlue,
        textPrimary: Color = colorLightTextPrimary,
        secondary: Color = colorLightTextSecondary,
        background: Color = colorLightBackground,
        error: Color = colorLightError,
        progressBar: Color = colorProgressBar,
        progressBarBackground: Color = colorProgressBarBackground,
        favoriteButtonBackground: Color = colorFavoriteButtonBackground,
        starColor: Color = colorStar
    ): AppColors = AppColors(
        primary = primary,
        textPrimary = textPrimary,
        secondary = secondary,
        background = background,
        error = error,
        progressBar = progressBar,
        progressBarBackground = progressBarBackground,
        favoriteButtonBackground = favoriteButtonBackground,
        starColor = starColor,
        isLight = true
    )

    fun darkColors(
        primary: Color = colorDarkBlue,
        textPrimary: Color = colorLightTextPrimary,
        secondary: Color = colorLightTextSecondary,
        background: Color = colorLightBackground,
        error: Color = colorLightError,
        progressBar: Color = colorProgressBar,
        progressBarBackground: Color = colorProgressBarBackground,
        favoriteButtonBackground: Color = colorFavoriteButtonBackground,
        starColor: Color = colorStar
    ): AppColors = AppColors(
        primary = primary,
        textPrimary = textPrimary,
        secondary = secondary,
        background = background,
        error = error,
        progressBar = progressBar,
        progressBarBackground = progressBarBackground,
        favoriteButtonBackground = favoriteButtonBackground,
        starColor = starColor,
        isLight = false
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        textPrimary = other.textPrimary
        secondary = other.secondary
        background = other.background
        error = other.error
        progressBar = other.progressBar
        progressBarBackground = other.progressBarBackground
        favoriteButtonBackground = other.favoriteButtonBackground
        starColor = other.starColor
    }

}

private val colorDarkBlue = Color(0xff0b253f)
private val colorLightTextPrimary = Color(0xFF000000)
private val colorLightTextSecondary = Color(0xFF6C727A)
private val colorLightBackground = Color(0xFFFFFFFF)
private val colorLightError = Color(0xFFD62222)
private val colorProgressBar = Color(0xff22f259)
private val colorProgressBarBackground = Color(0xff204529)
private val colorFavoriteButtonBackground = Color(0xff757575)
private val colorStar = Color(0xffFFC656)

internal val LocalColors = staticCompositionLocalOf {
    AppColors(
        colorDarkBlue,
        colorLightTextPrimary,
        colorLightTextSecondary,
        colorLightBackground,
        colorLightError,
        colorProgressBar,
        colorProgressBarBackground,
        colorFavoriteButtonBackground,
        colorStar,
        true
    )
}
