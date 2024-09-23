package com.indie.apps.pennypal.presentation.ui.theme

import androidx.compose.ui.graphics.Color

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
    Blue50(Color(0xFFE7F4FD)),
    Blue100(Color(0xFFBDD8FF)),
    Blue200(Color(0xFF91BEFF)),
    Blue300(Color(0xFF64A3FF)),
    Blue400(Color(0xFF4390FF)),
    Blue500(Color(bluePrimaryValue)),
    Blue600(Color(0xFF1E74FF)),
    Blue700(Color(0xFF1969FF)),
    Blue800(Color(0xFF145FFF)),
    Blue900(Color(0xFF0C4CFF)),
}

private const val bluePrimaryValue = 0xFF227CFF

enum class DarkBlue(val color: Color) {
    DarkBlue1(Color(0xFFEDEFFB)),
    DarkBlue2(Color(0xFF98ABCA)),
    DarkBlue3(Color(0xFF607698)),
    DarkBlue4(Color(0xFF425779)),
    DarkBlue5(Color(0xFF2F4365)),
    DarkBlue6(Color(0xFF223759)),
    DarkBlue7(Color(0xFF1B283D)),
    DarkBlue8(Color(0xFF1F242D)),
}

enum class Brand(val color: Color) {
    White(Color(0xFFF1F6FA)),
    Gray0(Color(0xFFD1CFCF)),
    Gray1(Color(0xFFACACAC)),
    Gray2(Color(0xFF6B6A6A)),
    Gray3(Color(0xFF3F3E3E)),
    Gray4(Color(0xFF3F3E3E)),
    Black(Color(0xFF16161D)),
    Blue1(Color(0xFF317EDB))
}


enum class BlueAccent(val color: Color) {
    BlueAccent100(Color(0xFFFFFFFF)),
    BlueAccent200(Color(blueAccentValue)),
    BlueAccent400(Color(0xFFC3D0FF)),
    BlueAccent700(Color(0xFFA9BCFF))
}

private const val blueAccentValue = 0xFFF6F8FF

enum class Ocean(val color: Color) {
    Ocean50(Color(0xFFE4F5FE)),
    Ocean100(Color(0xFFBAE5FD)),
    Ocean200(Color(0xFF8DD4FC)),
    Ocean300(Color(0xFF5FC3FA)),
    Ocean400(Color(0xFF3CB6F9)),
    Ocean500(Color(oceanPrimaryValue)),
    Ocean600(Color(0xFF17A2F7)),
    Ocean700(Color(0xFF1398F6)),
    Ocean800(Color(0xFF0F8FF5)),
    Ocean900(Color(0xFF087EF3))
}

private const val oceanPrimaryValue = 0xFF1AA9F8

enum class Green(val color: Color) {
    Green50(Color(0xFFE0F9F5)),
    Green100(Color(0xFFB3F0E6)),
    Green200(Color(0xFF80E7D5)),
    Green300(Color(0xFF4DDDC4)),
    Green400(Color(0xFF26D5B7)),
    Green500(Color(greenPrimaryValue)),
    Green600(Color(0xFF00C9A3)),
    Green700(Color(0xFF00C299)),
    Green800(Color(0xFF00BC90)),
    Green900(Color(0xFF00B07F))
}

private const val greenPrimaryValue = 0xFF00CEAA

enum class Red(val color: Color) {
    Red50(Color(0xFFFEEDED)),
    Red100(Color(0xFFFDD1D1)),
    Red200(Color(0xFFFCB2B2)),
    Red300(Color(0xFFFA9393)),
    Red400(Color(0xFFF97C7C)),
    Red500(Color(redPrimaryValue)),
    Red600(Color(0xFFF75D5D)),
    Red700(Color(0xFFF65353)),
    Red800(Color(0xFFF54949)),
    Red900(Color(0xFFF33737))
}

private const val redPrimaryValue = 0xFFF86565

enum class CategoryDark(val color: Color) {
    Other(Color(0xFFB0B0B0)),
    Bills(Color(0xFF7CA0E6)),
    Education(Color(0xFFFFEF60)),
    Entertainment(Color(0xFFFFC55F)),
    Food(Color(0xFF6EFF6E)),
    Gift(Color(0xFFCC6FCC)),
    Insurance(Color(0xFFFF9BCE)),
    Investment(Color(0xFF5BDBF1)),
    Medical(Color(0xFFFF667E)),
    PersonalCare(Color(0xFFFFCF91)),
    Salary(Color(0xFF86D1D1)),
    Rewards(Color(0xFFCCCCFF)),
    Rent(Color(0xFFC7D13F)),
    Shopping(Color(0xFFFF8F82)),
    Tax(Color(0xFFFFDAB9)),
    Traveling(Color(0xFF87CEFA))
}


enum class CategoryLight(val color: Color) {
    Other(Color(0xFF404040)),
    Bills(Color(0xFF00BFFF)),
    Education(Color(0xFFFFD700)),
    Entertainment(Color(0xFFFF8C00)),
    Food(Color(0xFF228B22)),
    Gift(Color(0xFF800080)),
    Insurance(Color(0xFF8B0000)),
    Investment(Color(0xFF78E4E4)),
    Medical(Color(0xFFC71585)),
    PersonalCare(Color(0xFF3E2723)),
    Salary(Color(0xFF008B8B)),
    Rewards(Color(0xFF191970)),
    Rent(Color(0xFF6B493A)),
    Shopping(Color(0xFFB22222)),
    Tax(Color(0xFF8B4513)),
    Traveling(Color(0xFF4682B4))
}
