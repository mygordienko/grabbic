package com.mygordienko.grabbic.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


fun String.toColorFromHex(): Color {
    val cleanedHex = this.removePrefix("#")
    val argbLong = cleanedHex.toLong(16)
    return Color(argbLong)
}

fun Color.toHexString(): String {
    val argb = this.toArgb()
    // Mask out the alpha channel bits
    val rgb = argb and 0x00FFFFFF
    return String.format("FF%06X", rgb)
}
