package com.indie.apps.pannypal.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val lightScheme = MyAppColors(
    gradientBlue = listOf(
        Blue.blue500.color,
        Ocen.ocen500.color
    ),
    gradientGreen = listOf(
        Green.green50.color,
        Neutral.Neutral0.color
    ),
    gradientRed = listOf(
        Red.red50.color,
        Neutral.Neutral0.color
    ),
    brand = Blue.blue500.color,
    redBgLight = Red.red100.color,
    redBg = Red.red500.color,
    redText = Red.red500.color,
    greenBgLight = Green.green100.color,
    greenBg = Green.green500.color,
    greenText = Green.green500.color,
    brandBg = Blue.blue50.color,
    black = Neutral.Neutral8.color,
    white = Neutral.Neutral0.color,
    gray0 = Neutral.Neutral3.color,
    gray1 = Neutral.Neutral4.color,
    gray2 = Neutral.Neutral5.color,
    gray3 = Neutral.Neutral6.color,
    inactiveLight = Neutral.Neutral1.color,
    inactiveDark = Neutral.Neutral5.color,
    divider = Neutral.Neutral1.color,
    fieldBg = Neutral.Neutral1.color,
    dataBg = Neutral.Neutral2.color,
    transparent = Color.Transparent,
    isDark = false
)

private val darkScheme = MyAppColors(
    gradientBlue = listOf(
        Blue.blue500.color,
        Ocen.ocen500.color
    ),
    gradientGreen = listOf(
        Green.green500.color,
        Neutral.Neutral0.color
    ),
    gradientRed = listOf(
        Red.red500.color,
        Neutral.Neutral0.color
    ),
    brand = Blue.blue500.color,
    redBgLight = Red.red100.color,
    redBg = Red.red500.color,
    redText = Red.red500.color,
    greenBgLight = Green.green100.color,
    greenBg = Green.green500.color,
    greenText = Green.green500.color,
    brandBg = Blue.blue100.color,
    black = Neutral.Neutral8.color,
    white = Neutral.Neutral0.color,
    gray0 = Neutral.Neutral1.color,
    gray1 = Neutral.Neutral2.color,
    gray2 = Neutral.Neutral3.color,
    gray3 = Neutral.Neutral4.color,
    inactiveLight = Neutral.Neutral2.color,
    inactiveDark = Neutral.Neutral4.color,
    divider = Neutral.Neutral1.color,
    fieldBg = Neutral.Neutral1.color,
    dataBg = Neutral.Neutral2.color,
    transparent = Color.Transparent,
    isDark = false
)

@Composable
fun PannyPalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
   /* val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }*/
/*
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )*/

    val colors = if (darkTheme) darkScheme else lightScheme

    ProvideMyAppTheme(
        colors = colors,
        typography = myAppTypography) {
        MaterialTheme(
            colorScheme = debugColors(darkTheme),
            typography = typography,
            content = content
        )
    }
}

@Composable
fun ProvideMyAppTheme(
    colors: MyAppColors,
    typography: MyAppTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMyAppColors provides colors,
        LocalMyAppTypography provides typography,
        content = content)
}




object MyAppTheme {
    val colors: MyAppColors
        @Composable
        get() = LocalMyAppColors.current
    val typography: MyAppTypography
        @Composable
        get() = LocalMyAppTypography.current
}

private val LocalMyAppColors = staticCompositionLocalOf<MyAppColors> {
    error("No Pannypal ColorPalette provided")
}

private val LocalMyAppTypography = staticCompositionLocalOf<MyAppTypography> {
    error("No Pannypal Typography provided")
}

/**
 * Pannypal custom Color Palette
 */
@Immutable
data class MyAppColors(
    val gradientBlue: List<Color>,
    val gradientGreen: List<Color>,
    val gradientRed: List<Color>,

    val brand: Color,

    val redBgLight: Color,
    val redBg: Color,
    val redText: Color,
    val greenBgLight: Color,
    val greenBg: Color,
    val greenText: Color,
    val brandBg: Color,

    val black: Color,
    val white: Color,

    val gray0: Color,
    val gray1: Color,
    val gray2: Color,
    val gray3: Color,

    val inactiveLight: Color,
    val inactiveDark: Color,
    val divider: Color,

    val fieldBg: Color,
    val dataBg: Color,

    val transparent: Color,

    val isDark: Boolean
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue.blue500.color,
    background = Neutral.Neutral0.color


)

private val LightColorScheme = lightColorScheme(
    primary = Blue.blue500.color,
    background = Neutral.Neutral0.color
)

fun debugColors(
    darkTheme: Boolean
): ColorScheme {
    return if(darkTheme)
        DarkColorScheme
    else{
        LightColorScheme
    }
}
