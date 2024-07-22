package com.indie.apps.pannypal.presentation.ui.theme

import androidx.compose.ui.graphics.Color

//val primaryLight = Color(0xFF004CA8)
//val primaryContainerLight = Color(0xFF006FEF)
val primaryLight = Color(0xFF227CFF)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF1AA9F8)
val onPrimaryContainerLight = Color(0xFFFFFFFF)
val secondaryLight = Color(0xFF435E94)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFB2C9FF)
val onSecondaryContainerLight = Color(0xFF19366B)
val tertiaryLight = Color(0xFF811CA1)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFAA4AC9)
val onTertiaryContainerLight = Color(0xFFFFFFFF)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFFAF9FF)
val onBackgroundLight = Color(0xFF181B23)
val surfaceLight = Color(0xFFFAF9FF)
val onSurfaceLight = Color(0xFF181B23)
val surfaceVariantLight = Color(0xFFDEE2F3)
val onSurfaceVariantLight = Color(0xFF414754)
val outlineLight = Color(0xFF727786)
val outlineVariantLight = Color(0xFFC1C6D7)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2D3039)
val inverseOnSurfaceLight = Color(0xFFEFF0FB)
val inversePrimaryLight = Color(0xFFAEC6FF)

val primaryDark = Color(0xFFAEC6FF)
val onPrimaryDark = Color(0xFF002E6B)
val primaryContainerDark = Color(0xFF006FEF)
val onPrimaryContainerDark = Color(0xFFFFFFFF)
val secondaryDark = Color(0xFFAEC6FF)
val onSecondaryDark = Color(0xFF0E2F63)
val secondaryContainerDark = Color(0xFF223E73)
val onSecondaryContainerDark = Color(0xFFC4D4FF)
val tertiaryDark = Color(0xFFF0B0FF)
val onTertiaryDark = Color(0xFF54006D)
val tertiaryContainerDark = Color(0xFFAA4AC9)
val onTertiaryContainerDark = Color(0xFFFFFFFF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF10131B)
val onBackgroundDark = Color(0xFFE0E2ED)
val surfaceDark = Color(0xFF10131B)
val onSurfaceDark = Color(0xFFE0E2ED)
val surfaceVariantDark = Color(0xFF414754)
val onSurfaceVariantDark = Color(0xFFC1C6D7)
val outlineDark = Color(0xFF8B90A0)
val outlineVariantDark = Color(0xFF414754)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE0E2ED)
val inverseOnSurfaceDark = Color(0xFF2D3039)
val inversePrimaryDark = Color(0xFF005AC4)

val incomeLight = Color(0xFF006B57)
val onIncomeLight = Color(0xFFFFFFFF)
val incomeContainerLight = Color(0xFF26D9B4)
val onIncomeContainerLight = Color(0xFF003A2E)
val expenseLight = Color(0xFFAE2F34)
val onExpenseLight = Color(0xFFFFFFFF)
val expenseContainerLight = Color(0xFFFF7674)
val onExpenseContainerLight = Color(0xFF340004)

val incomeDark = Color(0xFF4EF1CB)
val onIncomeDark = Color(0xFF00382C)
val incomeContainerDark = Color(0xFF00C6A3)
val onIncomeContainerDark = Color(0xFF002A21)
val expenseDark = Color(0xFFFFB3B0)
val onExpenseDark = Color(0xFF68000F)
val expenseContainerDark = Color(0xFFCC4547)
val onExpenseContainerDark = Color(0xFFFFFFFF)

enum class Neutral(val color: Color) {
    Neutral0(Color(0xffffffff)),
    Neutral1(Color(0xbdffffff)),
    Neutral2(Color(0x61ffffff)),
    Neutral3(Color(0x0D000000)),
    Neutral4(Color(0x1f000000)),
    Neutral5(Color(0x61000000)),
    Neutral6(Color(0x99000000)),
    Neutral7(Color(0xde000000)),
    Neutral8(Color(0xff121212))
}



enum class Blue(val color: Color) {
    blue50(Color(0xFFF4FBFF)),
    blue100(Color(0xFFBDD8FF)),
    blue200(Color(0xFF91BEFF)),
    blue300(Color(0xFF64A3FF)),
    blue400(Color(0xFF4390FF)),
    blue500(Color(_bluePrimaryValue)),
    blue600(Color(0xFF1E74FF)),
    blue700(Color(0xFF1969FF)),
    blue800(Color(0xFF145FFF)),
    blue900(Color(0xFF0C4CFF))
}

private const val _bluePrimaryValue = 0xFF227CFF

enum class BlueAccent(val color: Color) {
    blueAccent100(Color(0xFFFFFFFF)),
    blueAccent200(Color(_blueAccentValue)),
    blueAccent400(Color(0xFFC3D0FF)),
    blueAccent700(Color(0xFFA9BCFF))
}

private const val _blueAccentValue = 0xFFF6F8FF

enum class Ocen(val color: Color) {
    ocen50(Color(0xFFE4F5FE)),
    ocen100(Color(0xFFBAE5FD)),
    ocen200(Color(0xFF8DD4FC)),
    ocen300(Color(0xFF5FC3FA)),
    ocen400(Color(0xFF3CB6F9)),
    ocen500(Color(_ocenPrimaryValue)),
    ocen600(Color(0xFF17A2F7)),
    ocen700(Color(0xFF1398F6)),
    ocen800(Color(0xFF0F8FF5)),
    ocen900(Color(0xFF087EF3))
}

private const val _ocenPrimaryValue = 0xFF1AA9F8

enum class OcenAccent(val color: Color) {
    ocenAccent100(Color(0xFFFFFFFF)),
    ocenAccent200(Color(_ocenAccentValue)),
    ocenAccent400(Color(0xFFB5D7FF)),
    ocenAccent700(Color(0xFF9CC9FF))
}

private const val _ocenAccentValue = 0xFFE8F3FF

enum class Green(val color: Color) {
    green50(Color(0xFFE0F9F5)),
    green100(Color(0xFFB3F0E6)),
    green200(Color(0xFF80E7D5)),
    green300(Color(0xFF4DDDC4)),
    green400(Color(0xFF26D5B7)),
    green500(Color(_greenPrimaryValue)),
    green600(Color(0xFF00C9A3)),
    green700(Color(0xFF00C299)),
    green800(Color(0xFF00BC90)),
    green900(Color(0xFF00B07F))
}

private const val _greenPrimaryValue = 0xFF00CEAA

enum class GreenAccent(val color: Color) {
    greenAccent100(Color(0xFFDAFFF3)),
    greenAccent200(Color(_greenAccentValue)),
    greenAccent400(Color(0xFF74FFD2)),
    greenAccent700(Color(0xFF5AFFCA))
}

private const val _greenAccentValue = 0xFFA7FFE3

enum class Red(val color: Color) {
    red50(Color(0xFFFEEDED)),
    red100(Color(0xFFFDD1D1)),
    red200(Color(0xFFFCB2B2)),
    red300(Color(0xFFFA9393)),
    red400(Color(0xFFF97C7C)),
    red500(Color(_redPrimaryValue)),
    red600(Color(0xFFF75D5D)),
    red700(Color(0xFFF65353)),
    red800(Color(0xFFF54949)),
    red900(Color(0xFFF33737))
}

private const val _redPrimaryValue = 0xFFF86565

enum class RedAccent(val color: Color) {
    redAccent100(Color(0xFFFFFFFF)),
    redAccent200(Color(_redAccentValue)),
    redAccent400(Color(0xFFFFD5D5)),
    redAccent700(Color(0xFFFFBCBC))
}

private const val _redAccentValue = 0xFFFFFFFF

enum class Sky(val color: Color) {
    sky50(Color(0xFFFDFEFF)),
    sky100(Color(0xFFFBFDFF)),
    sky200(Color(0xFFF8FCFF)),
    sky300(Color(0xFFF5FBFF)),
    sky400(Color(0xFFF3FAFF)),
    sky500(Color(_skyPrimaryValue)),
    sky600(Color(0xFFEFF8FF)),
    sky700(Color(0xFFEDF7FF)),
    sky800(Color(0xFFEBF6FF)),
    sky900(Color(0xFFE7F5FF))
}

private const val _skyPrimaryValue = 0xFFF1F9FF

enum class SkyAccent(val color: Color) {
    skyAccent100(Color(0xFFFFFFFF)),
    skyAccent200(Color(_skyAccentValue)),
    skyAccent400(Color(0xFFFFFFFF)),
    skyAccent700(Color(0xFFFFFFFF))
}

private const val _skyAccentValue = 0xFFFFFFFF
