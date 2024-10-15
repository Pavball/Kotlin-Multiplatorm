package appDesign

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal data class AppTypography(
    val biggerTitle26: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    ),
    val titleBold24: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    val biggerSubtitleBold18: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    val biggerSubtitle18: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    val subtitle16: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val bodyBold16: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val button16: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val caption12: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    val captionBold12: TextStyle = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
