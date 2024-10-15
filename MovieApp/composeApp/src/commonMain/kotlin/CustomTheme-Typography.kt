import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal data class AppTypography(
    val biggerTitle: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    ),
    val title: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    val biggerSubtitleBold: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    val biggerSubtitle: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val button: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val caption: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    val captionBold: TextStyle = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp
    ),
    val actorNameBold: TextStyle = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
