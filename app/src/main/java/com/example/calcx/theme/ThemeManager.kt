package com.example.calcx.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.calcx.engine.AngleUnit

data class AppSettings(
    val themeId: String = PresetThemes.MidnightBlack.id,
    val buttonShape: ButtonShapeType = ButtonShapeType.ROUNDED,
    val buttonScale: ButtonScale = ButtonScale.NORMAL,
    val hapticEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val precision: Int = 8,
    val angleUnit: AngleUnit = AngleUnit.DEGREE,
    val thousandsSeparator: Boolean = true,
    val defaultMode: String = "STANDARD",
    // Custom theme overrides
    val customThemeActive: Boolean = false,
    val customBgColor: Int = 0xFF0F172A.toInt(),
    val customAccentColor: Int = 0xFF38BDF8.toInt(),
    val customButtonColor: Int = 0xFF1E293B.toInt()
)

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("calcx_prefs", Context.MODE_PRIVATE)

    var currentSettings by mutableStateOf(loadSettings())
        private set

    var currentTheme by mutableStateOf(resolveTheme(currentSettings))
        private set

    private fun loadSettings(): AppSettings {
        val themeId = prefs.getString("theme_id", PresetThemes.MidnightBlack.id) ?: PresetThemes.MidnightBlack.id
        val shapeName = prefs.getString("button_shape", ButtonShapeType.ROUNDED.name) ?: ButtonShapeType.ROUNDED.name
        val scaleName = prefs.getString("button_scale", ButtonScale.NORMAL.name) ?: ButtonScale.NORMAL.name
        val haptic = prefs.getBoolean("haptic_enabled", true)
        val sound = prefs.getBoolean("sound_enabled", true)
        val precision = prefs.getInt("precision", 8)
        val angleUnitName = prefs.getString("angle_unit", AngleUnit.DEGREE.name) ?: AngleUnit.DEGREE.name
        val separator = prefs.getBoolean("thousands_separator", true)
        val defaultMode = prefs.getString("default_mode", "STANDARD") ?: "STANDARD"
        val customActive = prefs.getBoolean("custom_theme_active", false)
        val customBg = prefs.getInt("custom_bg", 0xFF0F172A.toInt())
        val customAccent = prefs.getInt("custom_accent", 0xFF38BDF8.toInt())
        val customBtn = prefs.getInt("custom_btn", 0xFF1E293B.toInt())

        val shape = try { ButtonShapeType.valueOf(shapeName) } catch (e: Exception) { ButtonShapeType.ROUNDED }
        val scale = try { ButtonScale.valueOf(scaleName) } catch (e: Exception) { ButtonScale.NORMAL }
        val angle = try { AngleUnit.valueOf(angleUnitName) } catch (e: Exception) { AngleUnit.DEGREE }

        return AppSettings(
            themeId = themeId,
            buttonShape = shape,
            buttonScale = scale,
            hapticEnabled = haptic,
            soundEnabled = sound,
            precision = precision,
            angleUnit = angle,
            thousandsSeparator = separator,
            defaultMode = defaultMode,
            customThemeActive = customActive,
            customBgColor = customBg,
            customAccentColor = customAccent,
            customButtonColor = customBtn
        )
    }

    private fun resolveTheme(settings: AppSettings): ThemeColors {
        if (settings.customThemeActive) {
            val bg = Color(settings.customBgColor)
            val accent = Color(settings.customAccentColor)
            val btn = Color(settings.customButtonColor)
            return ThemeColors(
                id = "custom",
                name = "Custom Theme",
                isDark = true,
                background = bg,
                surface = btn,
                surfaceVariant = btn.copy(alpha = 0.8f),
                textPrimary = Color(0xFFF8FAFC),
                textSecondary = Color(0xFF94A3B8),
                accent = accent,
                accentSecondary = accent.copy(alpha = 0.7f),
                buttonNumberBg = btn,
                buttonNumberText = Color(0xFFFFFFFF),
                buttonOperatorBg = accent.copy(alpha = 0.85f),
                buttonOperatorText = Color(0xFFFFFFFF),
                buttonActionBg = btn.copy(alpha = 0.7f),
                buttonActionText = accent,
                buttonEqualsBg = accent,
                buttonEqualsText = Color(0xFF000000),
                displayBg = bg.copy(alpha = 0.9f)
            )
        }
        return PresetThemes.getById(settings.themeId)
    }

    fun setTheme(themeId: String) {
        val updated = currentSettings.copy(themeId = themeId, customThemeActive = false)
        saveSettings(updated)
    }

    fun setButtonShape(shape: ButtonShapeType) {
        val updated = currentSettings.copy(buttonShape = shape)
        saveSettings(updated)
    }

    fun setButtonScale(scale: ButtonScale) {
        val updated = currentSettings.copy(buttonScale = scale)
        saveSettings(updated)
    }

    fun setHapticEnabled(enabled: Boolean) {
        val updated = currentSettings.copy(hapticEnabled = enabled)
        saveSettings(updated)
    }

    fun setSoundEnabled(enabled: Boolean) {
        val updated = currentSettings.copy(soundEnabled = enabled)
        saveSettings(updated)
    }

    fun setPrecision(precision: Int) {
        val updated = currentSettings.copy(precision = precision)
        saveSettings(updated)
    }

    fun setAngleUnit(unit: AngleUnit) {
        val updated = currentSettings.copy(angleUnit = unit)
        saveSettings(updated)
    }

    fun setThousandsSeparator(enabled: Boolean) {
        val updated = currentSettings.copy(thousandsSeparator = enabled)
        saveSettings(updated)
    }

    fun setDefaultMode(mode: String) {
        val updated = currentSettings.copy(defaultMode = mode)
        saveSettings(updated)
    }

    fun saveCustomTheme(bgColor: Color, accentColor: Color, buttonColor: Color) {
        val updated = currentSettings.copy(
            customThemeActive = true,
            customBgColor = bgColor.toArgb(),
            customAccentColor = accentColor.toArgb(),
            customButtonColor = buttonColor.toArgb()
        )
        saveSettings(updated)
    }

    fun resetToDefaults() {
        val defaults = AppSettings()
        saveSettings(defaults)
    }

    private fun saveSettings(settings: AppSettings) {
        currentSettings = settings
        currentTheme = resolveTheme(settings)
        prefs.edit().apply {
            putString("theme_id", settings.themeId)
            putString("button_shape", settings.buttonShape.name)
            putString("button_scale", settings.buttonScale.name)
            putBoolean("haptic_enabled", settings.hapticEnabled)
            putBoolean("sound_enabled", settings.soundEnabled)
            putInt("precision", settings.precision)
            putString("angle_unit", settings.angleUnit.name)
            putBoolean("thousands_separator", settings.thousandsSeparator)
            putString("default_mode", settings.defaultMode)
            putBoolean("custom_theme_active", settings.customThemeActive)
            putInt("custom_bg", settings.customBgColor)
            putInt("custom_accent", settings.customAccentColor)
            putInt("custom_btn", settings.customButtonColor)
            apply()
        }
    }
}
