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
    blue50(Color(0xFFE7F4FD)),
    blue100(Color(0xFFBDD8FF)),
    blue200(Color(0xFF91BEFF)),
    blue300(Color(0xFF64A3FF)),
    blue400(Color(0xFF4390FF)),
    blue500(Color(_bluePrimaryValue)),
    blue600(Color(0xFF1E74FF)),
    blue700(Color(0xFF1969FF)),
    blue800(Color(0xFF145FFF)),
    blue900(Color(0xFF0C4CFF)),
}

private const val _bluePrimaryValue = 0xFF227CFF

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
