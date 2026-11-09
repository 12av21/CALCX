package com.example.calcx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.engine.AngleUnit
import com.example.calcx.ui.components.CalcButton
import com.example.calcx.ui.components.CalcDisplay
import com.example.calcx.viewmodel.CalculatorViewModel

@Composable
fun ScientificCalculatorScreen(
    viewModel: CalculatorViewModel,
    onOpenHistory: () -> Unit,
    onOpenThemes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = viewModel.themeManager.currentTheme
    val settings = viewModel.themeManager.currentSettings
    val is2nd = viewModel.isSecondActive

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        // Display with angle unit toggle
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
            onToggleAngleUnit = { viewModel.toggleAngleUnit() },
            modifier = Modifier.weight(1f)
        )

        // Scientific Keypad Grid (5 columns)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Sci Row 1: 2nd, sin/asin, cos/acos, tan/atan, π
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalcButton(
                    text = "2nd",
                    onClick = { viewModel.onKeyInput("2nd") },
                    backgroundColor = if (is2nd) theme.accent else theme.surfaceVariant,
                    textColor = if (is2nd) theme.buttonEqualsText else theme.textPrimary,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_2nd"
                )
                CalcButton(
                    text = if (is2nd) "sin⁻¹" else "sin",
                    onClick = { viewModel.onKeyInput(if (is2nd) "asin(" else "sin(") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_sin"
                )
                CalcButton(
                    text = if (is2nd) "cos⁻¹" else "cos",
                    onClick = { viewModel.onKeyInput(if (is2nd) "acos(" else "cos(") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_cos"
                )
                CalcButton(
                    text = if (is2nd) "tan⁻¹" else "tan",
                    onClick = { viewModel.onKeyInput(if (is2nd) "atan(" else "tan(") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_tan"
                )
                CalcButton(
                    text = "π",
                    onClick = { viewModel.onKeyInput("π") },
                    backgroundColor = theme.surface,
                    textColor = theme.accentSecondary,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_pi"
                )
            }

            // Sci Row 2: x^y / x², √ / ∛, log, ln, e
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalcButton(
                    text = if (is2nd) "x²" else "x^y",
                    onClick = { viewModel.onKeyInput(if (is2nd) "^2" else "^") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_pow"
                )
                CalcButton(
                    text = if (is2nd) "∛" else "√",
                    onClick = { viewModel.onKeyInput(if (is2nd) "cbrt(" else "sqrt(") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_sqrt"
                )
                CalcButton(
                    text = if (is2nd) "10^x" else "log",
                    onClick = { viewModel.onKeyInput(if (is2nd) "10^" else "log(") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_log"
                )
                CalcButton(
                    text = if (is2nd) "e^x" else "ln",
                    onClick = { viewModel.onKeyInput(if (is2nd) "exp(" else "ln(") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_ln"
                )
                CalcButton(
                    text = "e",
                    onClick = { viewModel.onKeyInput("e") },
                    backgroundColor = theme.surface,
                    textColor = theme.accentSecondary,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_e"
                )
            }

            // Sci Row 3: (, ), x!, 1/x, mod
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalcButton(
                    text = "(",
                    onClick = { viewModel.onKeyInput("(") },
                    backgroundColor = theme.surfaceVariant,
                    textColor = theme.textPrimary,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_open_paren"
                )
                CalcButton(
                    text = ")",
                    onClick = { viewModel.onKeyInput(")") },
                    backgroundColor = theme.surfaceVariant,
                    textColor = theme.textPrimary,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_close_paren"
                )
                CalcButton(
                    text = "x!",
                    onClick = { viewModel.onKeyInput("!") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 16.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_fact"
                )
                CalcButton(
                    text = "1/x",
                    onClick = { viewModel.onKeyInput("1/") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 15.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_reciprocal"
                )
                CalcButton(
                    text = "mod",
                    onClick = { viewModel.onKeyInput("mod") },
                    backgroundColor = theme.surface,
                    textColor = theme.accent,
                    shapeType = settings.buttonShape,
                    fontSize = 14.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp),
                    testTag = "btn_mod"
                )
            }

            // Numeric & Operator rows (5 columns: 7, 8, 9, DEL, AC)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("7", "8", "9").forEach { num ->
                    CalcButton(
                        text = num,
                        onClick = { viewModel.onKeyInput(num) },
                        backgroundColor = theme.buttonNumberBg,
                        textColor = theme.buttonNumberText,
                        shapeType = settings.buttonShape,
                        fontSize = 22.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(46.dp),
                        testTag = "btn_$num"
                    )
                }
                CalcButton(
                    icon = Icons.AutoMirrored.Filled.Backspace,
                    onClick = { viewModel.onKeyInput("DEL") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_del"
                )
                CalcButton(
                    text = "AC",
                    onClick = { viewModel.onKeyInput("AC") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_ac"
                )
            }

            // Row 5: 4, 5, 6, ×, ÷
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("4", "5", "6").forEach { num ->
                    CalcButton(
                        text = num,
                        onClick = { viewModel.onKeyInput(num) },
                        backgroundColor = theme.buttonNumberBg,
                        textColor = theme.buttonNumberText,
                        shapeType = settings.buttonShape,
                        fontSize = 22.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(46.dp),
                        testTag = "btn_$num"
                    )
                }
                CalcButton(
                    text = "×",
                    onClick = { viewModel.onKeyInput("×") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_mul"
                )
                CalcButton(
                    text = "÷",
                    onClick = { viewModel.onKeyInput("÷") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_div"
                )
            }

            // Row 6: 1, 2, 3, +, -
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("1", "2", "3").forEach { num ->
                    CalcButton(
                        text = num,
                        onClick = { viewModel.onKeyInput(num) },
                        backgroundColor = theme.buttonNumberBg,
                        textColor = theme.buttonNumberText,
                        shapeType = settings.buttonShape,
                        fontSize = 22.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(46.dp),
                        testTag = "btn_$num"
                    )
                }
                CalcButton(
                    text = "+",
                    onClick = { viewModel.onKeyInput("+") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_add"
                )
                CalcButton(
                    text = "-",
                    onClick = { viewModel.onKeyInput("-") },
                    backgroundColor = theme.buttonOperatorBg,
                    textColor = theme.buttonOperatorText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sub"
                )
            }

            // Row 7: +/-, 0, ., %, =
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalcButton(
                    text = "+/-",
                    onClick = { viewModel.onKeyInput("+/-") },
                    backgroundColor = theme.buttonNumberBg,
                    textColor = theme.buttonNumberText,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sign"
                )
                CalcButton(
                    text = "0",
                    onClick = { viewModel.onKeyInput("0") },
                    backgroundColor = theme.buttonNumberBg,
                    textColor = theme.buttonNumberText,
                    shapeType = settings.buttonShape,
                    fontSize = 22.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_0"
                )
                CalcButton(
                    text = ".",
                    onClick = { viewModel.onKeyInput(".") },
                    backgroundColor = theme.buttonNumberBg,
                    textColor = theme.buttonNumberText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_dot"
                )
                CalcButton(
                    text = "%",
                    onClick = { viewModel.onKeyInput("%") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    fontSize = 18.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_percent"
                )
                CalcButton(
                    text = "=",
                    onClick = { viewModel.calculateFinalResult("SCIENTIFIC") },
                    backgroundColor = theme.buttonEqualsBg,
                    textColor = theme.buttonEqualsText,
                    shapeType = settings.buttonShape,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_equals"
                )
            }
        }
    }
}
