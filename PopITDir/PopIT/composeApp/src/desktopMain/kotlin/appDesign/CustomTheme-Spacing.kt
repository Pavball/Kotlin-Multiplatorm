package appDesign

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val default: Dp = 0.dp,
    val extraSmall4: Dp = 4.dp,
    val small8: Dp = 8.dp,
    val smedium12: Dp = 12.dp,
    val medium16: Dp = 16.dp,
    val large24: Dp = 24.dp,
    val extraLarge32: Dp = 32.dp,
    val extreme48: Dp = 48.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }
