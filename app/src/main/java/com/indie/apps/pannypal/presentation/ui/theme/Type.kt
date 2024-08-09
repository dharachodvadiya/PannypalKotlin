package com.indie.apps.pannypal.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.indie.apps.pannypal.R

/*val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)*/

val displayFontFamily = FontFamily(
  /*  Font(
        googleFont = GoogleFont("Heebo"),
        fontProvider = provider,
    )*/

    Font(R.font.hebbo_font, weight = FontWeight.Normal),
)

// Default Material 3 typography values
val baseline = Typography()

val typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = displayFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = displayFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = displayFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = displayFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = displayFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = displayFontFamily),
)

val myAppTypography = MyAppTypography(
    Regular57 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(57f).sp,
        fontWeight = FontWeight.Normal
    ),
    Regular51 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(51f).sp,
        fontWeight = FontWeight.Normal
    ),
    Regular46 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(46f).sp,
        fontWeight = FontWeight.Normal
    ),
    Regular44 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(44f).sp,
        fontWeight = FontWeight.Normal
    ),
    Medium56 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(56f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium54 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(54f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium50 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(50f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium46 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(46f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium45_29 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(45.29f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium44 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(44f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium40 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(40f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium34 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(34f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium33 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(33f).sp,
        fontWeight = FontWeight.Medium
    ),
    Medium30 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(30f).sp,
        fontWeight = FontWeight.Medium
    ),
    Regular77_5 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(77.5f).sp,
        fontWeight = FontWeight.Normal
    ),
    Semibold90 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(90f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold80 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(80f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold67_5 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(67.5f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold60 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(60f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold57 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(57f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold56 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(56f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold54 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(54f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold52_5 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(52.5f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold50 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(50f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold48 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(48f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold45 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(45f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Semibold40 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(40f).sp,
        fontWeight = FontWeight.SemiBold
    ),
    Bold52_5 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(52.5f).sp,
        fontWeight = FontWeight.Bold
    ),
    Bold49_5 = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = pxlToSp(49.5f).sp,
        fontWeight = FontWeight.Bold
    )
)

fun pxlToSp(figmaSize: Float): Float {
    // Figma DPI is 72, Android base DPI is 160
    val scaleRatio =  72f/ 160f
    return figmaSize * scaleRatio * 0.8f
}

/**
 * Pannypal cuatom Typography
 */

@Immutable
data class MyAppTypography(
    val Regular57: TextStyle,
    val Regular51: TextStyle,
    val Regular46: TextStyle,
    val Regular44: TextStyle,

    val Medium56: TextStyle,
    val Medium54: TextStyle,
    val Medium50: TextStyle,
    val Medium46: TextStyle,
    val Medium45_29: TextStyle,
    val Medium44: TextStyle,
    val Medium40: TextStyle,
    val Medium34: TextStyle,
    val Medium33: TextStyle,
    val Medium30: TextStyle,

    val Regular77_5: TextStyle,
    val Semibold90: TextStyle,
    val Semibold80: TextStyle,
    val Semibold67_5: TextStyle,
    val Semibold60: TextStyle,
    val Semibold57: TextStyle,
    val Semibold56: TextStyle,
    val Semibold54: TextStyle,
    val Semibold52_5: TextStyle,
    val Semibold50: TextStyle,
    val Semibold48: TextStyle,
    val Semibold45: TextStyle,
    val Semibold40: TextStyle,

    val Bold52_5: TextStyle,
    val Bold49_5: TextStyle
)