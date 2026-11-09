package com.example.calcx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.theme.ThemeColors
import com.example.calcx.ui.components.CalcButton
import com.example.calcx.ui.components.CalcDisplay
import com.example.calcx.viewmodel.CalculatorViewModel

@Composable
fun StandardCalculatorScreen(
    viewModel: CalculatorViewModel,
    onOpenHistory: () -> Unit,
    onOpenThemes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = viewModel.themeManager.currentTheme
    val settings = viewModel.themeManager.currentSettings

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        // Display Section
        CalcDisplay(
            expression = viewModel.expression,
            result = viewModel.result,
            preview = viewModel.previewResult,
            theme = theme,
            angleUnit = settings.angleUnit,
            memoryValue = viewModel.memoryValue,
            canUndo = viewModel.canUndo,
            canRedo = viewModel.canRedo,
            onUndo = { viewModel.undo() },
            onRedo = { viewModel.redo() },
            onOpenHistory = onOpenHistory,
            onOpenThemes = onOpenThemes,
            onToggleAngleUnit = null,
            modifier = Modifier.weight(1f)
        )

        // Memory Toolbar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val memButtons = listOf("MC", "MR", "M+", "M-")
            memButtons.forEach { memKey ->
                CalcButton(
                    text = memKey,
                    onClick = { viewModel.onKeyInput(memKey) },
                    backgroundColor = theme.surface.copy(alpha = 0.5f),
                    textColor = if (memKey == "MC" || memKey == "MR") {
                        if (viewModel.memoryValue != null) theme.accent else theme.textSecondary.copy(alpha = 0.4f)
                    } else theme.textSecondary,
                    shapeType = settings.buttonShape,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    testTag = "mem_$memKey"
                )
            }
        }

        // Keypad Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 14.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: AC, DEL, %, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalcButton(
                    text = if (viewModel.expression.isNotEmpty()) "C" else "AC",
                    onClick = { viewModel.onKeyInput("AC") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_ac"
                )
                CalcButton(
                    icon = Icons.AutoMirrored.Filled.Backspace,
                    onClick = { viewModel.onKeyInput("DEL") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_del"
                )
                CalcButton(
                    text = "%",
                    onClick = { viewModel.onKeyInput("%") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_percent"
                )
                CalcButton(
                    text = "÷",
                    onClick = { viewModel.onKeyInput("÷") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 26.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_div"
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("7", "8", "9").forEach { num ->
                    CalcButton(
                        text = num,
                        onClick = { viewModel.onKeyInput(num) },
                        backgroundColor = theme.buttonNumberBg,
                        textColor = theme.buttonNumberText,
                        shapeType = settings.buttonShape,
                        fontSize = 24.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).aspectRatio(1.22f),
                        testTag = "btn_$num"
                    )
                }
                CalcButton(
                    text = "×",
                    onClick = { viewModel.onKeyInput("×") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 26.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_mul"
                )
            }

            // Row 3: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("4", "5", "6").forEach { num ->
                    CalcButton(
                        text = num,
                        onClick = { viewModel.onKeyInput(num) },
                        backgroundColor = theme.buttonNumberBg,
                        textColor = theme.buttonNumberText,
                        shapeType = settings.buttonShape,
                        fontSize = 24.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).aspectRatio(1.22f),
                        testTag = "btn_$num"
                    )
                }
                CalcButton(
                    text = "-",
                    onClick = { viewModel.onKeyInput("-") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 26.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_sub"
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("1", "2", "3").forEach { num ->
                    CalcButton(
                        text = num,
                        onClick = { viewModel.onKeyInput(num) },
                        backgroundColor = theme.buttonNumberBg,
                        textColor = theme.buttonNumberText,
                        shapeType = settings.buttonShape,
                        fontSize = 24.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).aspectRatio(1.22f),
                        testTag = "btn_$num"
                    )
                }
                CalcButton(
                    text = "+",
                    onClick = { viewModel.onKeyInput("+") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 26.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_add"
                )
            }

            // Row 5: +/-, 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalcButton(
                    text = "+/-",
                    onClick = { viewModel.onKeyInput("+/-") },
                    backgroundColor = theme.buttonNumberBg,
                    textColor = theme.buttonNumberText,
                    shapeType = settings.buttonShape,
                    fontSize = 20.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_sign"
                )
                CalcButton(
                    text = "0",
                    onClick = { viewModel.onKeyInput("0") },
                    backgroundColor = theme.buttonNumberBg,
                    textColor = theme.buttonNumberText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_0"
                )
                CalcButton(
                    text = ".",
                    onClick = { viewModel.onKeyInput(".") },
                    backgroundColor = theme.buttonNumberBg,
                    textColor = theme.buttonNumberText,
                    shapeType = settings.buttonShape,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_dot"
                )
                CalcButton(
                    text = "=",
                    onClick = { viewModel.calculateFinalResult("STANDARD") },
                    backgroundColor = theme.buttonEqualsBg,
                    textColor = theme.buttonEqualsText,
                    shapeType = settings.buttonShape,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).aspectRatio(1.22f),
                    testTag = "btn_equals"
                )
            }
        }
    }
}
