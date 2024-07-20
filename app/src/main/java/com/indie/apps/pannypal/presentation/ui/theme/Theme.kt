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
    button_gradient_blue = listOf(
        Blue.blue500.color,
        Ocen.ocen500.color
    ),
    bg_gradient_green = listOf(
        Green.green500.color,
        Neutral.Neutral0.color
    ),
    bg_gradient_red = listOf(
        Red.red500.color,
        Neutral.Neutral0.color
    ),
    brand = Blue.blue500.color,
    red_bg_light = Red.red100.color,
    red_bg = Red.red500.color,
    red_text = Red.red500.color,
    green_bg_light = Green.green100.color,
    green_bg = Green.green500.color,
    green_text = Green.green500.color,
    brand_bg = Blue.blue100.color,
    black = Neutral.Neutral8.color,
    white = Neutral.Neutral0.color,
    gray_1 = Neutral.Neutral2.color,
    gray_2 = Neutral.Neutral3.color,
    gray_3 = Neutral.Neutral4.color,
    inActive_light = Neutral.Neutral1.color,
    inActive_dark = Neutral.Neutral5.color,
    divider = Neutral.Neutral1.color,
    field_bg = Neutral.Neutral1.color,
    data_bg = Neutral.Neutral2.color,
    isDark = false
)

private val darkScheme = MyAppColors(
    button_gradient_blue = listOf(
        Blue.blue500.color,
        Ocen.ocen500.color
    ),
    bg_gradient_green = listOf(
        Green.green500.color,
        Neutral.Neutral0.color
    ),
    bg_gradient_red = listOf(
        Red.red500.color,
        Neutral.Neutral0.color
    ),
    brand = Blue.blue500.color,
    red_bg_light = Red.red100.color,
    red_bg = Red.red500.color,
    red_text = Red.red500.color,
    green_bg_light = Green.green100.color,
    green_bg = Green.green500.color,
    green_text = Green.green500.color,
    brand_bg = Blue.blue100.color,
    black = Neutral.Neutral8.color,
    white = Neutral.Neutral0.color,
    gray_1 = Neutral.Neutral2.color,
    gray_2 = Neutral.Neutral3.color,
    gray_3 = Neutral.Neutral4.color,
    inActive_light = Neutral.Neutral2.color,
    inActive_dark = Neutral.Neutral4.color,
    divider = Neutral.Neutral1.color,
    field_bg = Neutral.Neutral1.color,
    data_bg = Neutral.Neutral2.color,
    isDark = false
)

@Composable
fun PannyPalTheme(
    dpi : Float = 1f,
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
    val button_gradient_blue: List<Color>,
    val bg_gradient_green: List<Color>,
    val bg_gradient_red: List<Color>,

    val brand: Color,

    val red_bg_light: Color,
    val red_bg: Color,
    val red_text: Color,
    val green_bg_light: Color,
    val green_bg: Color,
    val green_text: Color,
    val brand_bg: Color,

    val black: Color,
    val white: Color,

    val gray_1: Color,
    val gray_2: Color,
    val gray_3: Color,

    val inActive_light: Color,
    val inActive_dark: Color,
    val divider: Color,

    val field_bg: Color,
    val data_bg: Color,

    val isDark: Boolean
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue.blue500.color
)

private val LightColorScheme = lightColorScheme(
    primary = Blue.blue500.color
)

fun debugColors(
    darkTheme: Boolean
): ColorScheme {
    if(darkTheme)
        return DarkColorScheme
    else{
        return LightColorScheme
    }
}
