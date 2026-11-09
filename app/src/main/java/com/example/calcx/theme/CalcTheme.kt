package com.example.calcx.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

enum class ButtonShapeType(val displayName: String) {
    ROUNDED("Rounded"),
    PILL("Pill"),
    SQUARE("Square")
}

fun ButtonShapeType.toComposeShape(): Shape {
    return when (this) {
        ButtonShapeType.ROUNDED -> RoundedCornerShape(18.dp)
        ButtonShapeType.PILL -> CircleShape
        ButtonShapeType.SQUARE -> RoundedCornerShape(6.dp)
    }
}

enum class ButtonScale(val scale: Float, val label: String) {
    COMPACT(0.88f, "Compact"),
    NORMAL(1.0f, "Normal"),
    LARGE(1.12f, "Spacious")
}

data class ThemeColors(
    val id: String,
    val name: String,
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val accentSecondary: Color,
    val buttonNumberBg: Color,
    val buttonNumberText: Color,
    val buttonOperatorBg: Color,
    val buttonOperatorText: Color,
    val buttonActionBg: Color,
    val buttonActionText: Color,
    val buttonEqualsBg: Color,
    val buttonEqualsText: Color,
    val displayBg: Color,
    val isGlassmorphism: Boolean = false
)

object PresetThemes {
    val MidnightBlack = ThemeColors(
        id = "midnight_black",
        name = "Midnight Black",
        isDark = true,
        background = Color(0xFF0F172A),
        surface = Color(0xFF1E293B),
        surfaceVariant = Color(0xFF334155),
        textPrimary = Color(0xFFF8FAFC),
        textSecondary = Color(0xFF94A3B8),
        accent = Color(0xFF38BDF8),
        accentSecondary = Color(0xFF818CF8),
        buttonNumberBg = Color(0xFF1E293B),
        buttonNumberText = Color(0xFFF1F5F9),
        buttonOperatorBg = Color(0xFF0284C7),
        buttonOperatorText = Color(0xFFFFFFFF),
        buttonActionBg = Color(0xFF334155),
        buttonActionText = Color(0xFF38BDF8),
        buttonEqualsBg = Color(0xFF0EA5E9),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0xFF090E17)
    )

    val PureAmoled = ThemeColors(
        id = "pure_amoled",
        name = "Pure AMOLED",
        isDark = true,
        background = Color(0xFF000000),
        surface = Color(0xFF121212),
        surfaceVariant = Color(0xFF1E1E1E),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFF888888),
        accent = Color(0xFF00E5FF),
        accentSecondary = Color(0xFFFF4081),
        buttonNumberBg = Color(0xFF151515),
        buttonNumberText = Color(0xFFFFFFFF),
        buttonOperatorBg = Color(0xFF262626),
        buttonOperatorText = Color(0xFF00E5FF),
        buttonActionBg = Color(0xFF222222),
        buttonActionText = Color(0xFFFF5252),
        buttonEqualsBg = Color(0xFF00E5FF),
        buttonEqualsText = Color(0xFF000000),
        displayBg = Color(0xFF000000)
    )

    val OceanBlue = ThemeColors(
        id = "ocean_blue",
        name = "Ocean Blue",
        isDark = true,
        background = Color(0xFF0B192C),
        surface = Color(0xFF1E3E62),
        surfaceVariant = Color(0xFF2E5077),
        textPrimary = Color(0xFFF1F6F9),
        textSecondary = Color(0xFFB0C4DE),
        accent = Color(0xFF00D2D3),
        accentSecondary = Color(0xFFFF9F43),
        buttonNumberBg = Color(0xFF172C4C),
        buttonNumberText = Color(0xFFFFFFFF),
        buttonOperatorBg = Color(0xFF0077B6),
        buttonOperatorText = Color(0xFFFFFFFF),
        buttonActionBg = Color(0xFF224263),
        buttonActionText = Color(0xFF00D2D3),
        buttonEqualsBg = Color(0xFF00B4D8),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0xFF060F1A)
    )

    val SunsetOrange = ThemeColors(
        id = "sunset_orange",
        name = "Sunset Orange",
        isDark = true,
        background = Color(0xFF1F1717),
        surface = Color(0xFF3A2424),
        surfaceVariant = Color(0xFF5A3535),
        textPrimary = Color(0xFFFFF0ED),
        textSecondary = Color(0xFFE0B8B2),
        accent = Color(0xFFFF6B4A),
        accentSecondary = Color(0xFFFFB03A),
        buttonNumberBg = Color(0xFF2C1E1E),
        buttonNumberText = Color(0xFFFFF5F2),
        buttonOperatorBg = Color(0xFFD35400),
        buttonOperatorText = Color(0xFFFFFFFF),
        buttonActionBg = Color(0xFF3F2727),
        buttonActionText = Color(0xFFFF9E7D),
        buttonEqualsBg = Color(0xFFFF6B4A),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0xFF140D0D)
    )

    val EmeraldGreen = ThemeColors(
        id = "emerald_green",
        name = "Emerald Green",
        isDark = true,
        background = Color(0xFF0A1C16),
        surface = Color(0xFF133E31),
        surfaceVariant = Color(0xFF1C5745),
        textPrimary = Color(0xFFE8F5E9),
        textSecondary = Color(0xFFA5D6A7),
        accent = Color(0xFF00E676),
        accentSecondary = Color(0xFF69F0AE),
        buttonNumberBg = Color(0xFF102E24),
        buttonNumberText = Color(0xFFE8F5E9),
        buttonOperatorBg = Color(0xFF1B5E20),
        buttonOperatorText = Color(0xFFE8F5E9),
        buttonActionBg = Color(0xFF174234),
        buttonActionText = Color(0xFF00E676),
        buttonEqualsBg = Color(0xFF00C853),
        buttonEqualsText = Color(0xFF0A1C16),
        displayBg = Color(0xFF05110D)
    )

    val LavenderPurple = ThemeColors(
        id = "lavender_purple",
        name = "Lavender Purple",
        isDark = true,
        background = Color(0xFF1A132F),
        surface = Color(0xFF2B1B4D),
        surfaceVariant = Color(0xFF3E286D),
        textPrimary = Color(0xFFF3E8FF),
        textSecondary = Color(0xFFD8B4FE),
        accent = Color(0xFFC084FC),
        accentSecondary = Color(0xFFA855F7),
        buttonNumberBg = Color(0xFF261842),
        buttonNumberText = Color(0xFFFAF5FF),
        buttonOperatorBg = Color(0xFF7E22CE),
        buttonOperatorText = Color(0xFFFFFFFF),
        buttonActionBg = Color(0xFF35205A),
        buttonActionText = Color(0xFFE9D5FF),
        buttonEqualsBg = Color(0xFFA855F7),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0xFF100A1F)
    )

    val RosePink = ThemeColors(
        id = "rose_pink",
        name = "Rose Pink",
        isDark = true,
        background = Color(0xFF210F19),
        surface = Color(0xFF3D1B2D),
        surfaceVariant = Color(0xFF5E2644),
        textPrimary = Color(0xFFFDF2F8),
        textSecondary = Color(0xFFF472B6),
        accent = Color(0xFFFB7185),
        accentSecondary = Color(0xFFFDA4AF),
        buttonNumberBg = Color(0xFF311524),
        buttonNumberText = Color(0xFFFFFFFF),
        buttonOperatorBg = Color(0xFFBE123C),
        buttonOperatorText = Color(0xFFFFFFFF),
        buttonActionBg = Color(0xFF451930),
        buttonActionText = Color(0xFFFDA4AF),
        buttonEqualsBg = Color(0xFFF43F5E),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0xFF160810)
    )

    val MinimalWhite = ThemeColors(
        id = "minimal_white",
        name = "Minimal White",
        isDark = false,
        background = Color(0xFFF8FAFC),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F5F9),
        textPrimary = Color(0xFF0F172A),
        textSecondary = Color(0xFF64748B),
        accent = Color(0xFF2563EB),
        accentSecondary = Color(0xFF4F46E5),
        buttonNumberBg = Color(0xFFFFFFFF),
        buttonNumberText = Color(0xFF1E293B),
        buttonOperatorBg = Color(0xFFE2E8F0),
        buttonOperatorText = Color(0xFF0F172A),
        buttonActionBg = Color(0xFFEEF2F6),
        buttonActionText = Color(0xFF2563EB),
        buttonEqualsBg = Color(0xFF2563EB),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0xFFF1F5F9)
    )

    val CyberNeon = ThemeColors(
        id = "cyber_neon",
        name = "Cyber Neon",
        isDark = true,
        background = Color(0xFF05050A),
        surface = Color(0xFF10101C),
        surfaceVariant = Color(0xFF1B1B30),
        textPrimary = Color(0xFF00FFCC),
        textSecondary = Color(0xFFFF007F),
        accent = Color(0xFF00FF66),
        accentSecondary = Color(0xFFFF007F),
        buttonNumberBg = Color(0xFF121224),
        buttonNumberText = Color(0xFFE0FFFF),
        buttonOperatorBg = Color(0xFF2A163B),
        buttonOperatorText = Color(0xFFFF00AA),
        buttonActionBg = Color(0xFF122828),
        buttonActionText = Color(0xFF00FFCC),
        buttonEqualsBg = Color(0xFF00FF88),
        buttonEqualsText = Color(0xFF000000),
        displayBg = Color(0xFF000000)
    )

    val Glassmorphism = ThemeColors(
        id = "glassmorphism",
        name = "Glassmorphism",
        isDark = true,
        background = Color(0xFF111827),
        surface = Color(0x33374151),
        surfaceVariant = Color(0x444B5563),
        textPrimary = Color(0xFFF9FAFB),
        textSecondary = Color(0xFFD1D5DB),
        accent = Color(0xFF60A5FA),
        accentSecondary = Color(0xFFA78BFA),
        buttonNumberBg = Color(0x2E6B7280),
        buttonNumberText = Color(0xFFFFFFFF),
        buttonOperatorBg = Color(0x4D3B82F6),
        buttonOperatorText = Color(0xFF93C5FD),
        buttonActionBg = Color(0x384B5563),
        buttonActionText = Color(0xFFF87171),
        buttonEqualsBg = Color(0x992563EB),
        buttonEqualsText = Color(0xFFFFFFFF),
        displayBg = Color(0x1F1F2937),
        isGlassmorphism = true
    )

    val allPresets = listOf(
        MidnightBlack,
        PureAmoled,
        OceanBlue,
        SunsetOrange,
        EmeraldGreen,
        LavenderPurple,
        RosePink,
        MinimalWhite,
        CyberNeon,
        Glassmorphism
    )

    fun getById(id: String): ThemeColors {
        return allPresets.find { it.id == id } ?: MidnightBlack
    }
}
