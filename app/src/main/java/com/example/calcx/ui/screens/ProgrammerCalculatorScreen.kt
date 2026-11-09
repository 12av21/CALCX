package com.example.calcx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.engine.NumberBase
import com.example.calcx.engine.ProgrammerEngine
import com.example.calcx.engine.WordSize
import com.example.calcx.ui.components.CalcButton
import com.example.calcx.viewmodel.CalculatorViewModel

@Composable
fun ProgrammerCalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = viewModel.themeManager.currentTheme
    val settings = viewModel.themeManager.currentSettings
    val activeBase = viewModel.programmerBase
    val wordSize = viewModel.programmerWordSize
    val currentValue = viewModel.programmerValue

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        // Base Display Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(theme.displayBg)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Word Size Selection Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROGRAMMER",
                    color = theme.accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    WordSize.entries.forEach { size ->
                        val isSelected = wordSize == size
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) theme.accent else theme.surface)
                                .clickable { viewModel.setProgrammerWord(size) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = size.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) theme.buttonEqualsText else theme.textSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Base Readout Rows (HEX, DEC, OCT, BIN)
            NumberBase.entries.forEach { base ->
                val isSelected = activeBase == base
                val formatted = ProgrammerEngine.formatInBase(currentValue, base, wordSize)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) theme.surfaceVariant.copy(alpha = 0.6f) else Color.Transparent)
                        .clickable { viewModel.setProgrammerActiveBase(base) }
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = base.name,
                        color = if (isSelected) theme.accent else theme.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = formatted,
                        color = if (isSelected) theme.textPrimary else theme.textSecondary,
                        fontSize = if (base == NumberBase.BIN) 11.sp else 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.weight(1f).padding(start = 12.dp)
                    )
                }
            }
        }

        // Interactive Bitboard (Show lower 32 or 16 or 8 bits based on word size)
        val bitsToShow = wordSize.bits
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Bitboard ($bitsToShow bits - Tap to toggle)",
                fontSize = 11.sp,
                color = theme.textSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Group by 4 bits
                for (i in (bitsToShow - 1) downTo 0) {
                    val bitVal = ((currentValue shr i) and 1uL).toInt()
                    val isSet = bitVal == 1
                    Box(
                        modifier = Modifier
                            .size(width = 17.dp, height = 24.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSet) theme.accent else theme.surface)
                            .border(
                                0.5.dp,
                                if (isSet) theme.accentSecondary else theme.surfaceVariant,
                                RoundedCornerShape(3.dp)
                            )
                            .clickable { viewModel.toggleProgrammerBit(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = bitVal.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSet) theme.buttonEqualsText else theme.textSecondary
                        )
                    }
                    if (i % 4 == 0 && i != 0) {
                        Spacer(modifier = Modifier.width(3.dp))
                    }
                }
            }
        }

        // Programmer Keypad (Bitwise operators + Hex + Numbers)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Row 1: AND, OR, XOR, NOT, ROL, ROR
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("AND", "OR", "XOR", "NOT", "ROL", "ROR").forEach { op ->
                    CalcButton(
                        text = op,
                        onClick = { viewModel.onProgrammerKey(op) },
                        backgroundColor = theme.surface,
                        textColor = theme.accent,
                        shapeType = settings.buttonShape,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                }
            }

            // Row 2: LSH, RSH, NAND, NOR, XNOR, AC
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("LSH", "RSH", "NAND", "NOR", "XNOR").forEach { op ->
                    CalcButton(
                        text = op,
                        onClick = { viewModel.onProgrammerKey(op) },
                        backgroundColor = theme.surface,
                        textColor = theme.accent,
                        shapeType = settings.buttonShape,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                }
                CalcButton(
                    text = "AC",
                    onClick = { viewModel.onProgrammerKey("AC") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(38.dp)
                )
            }

            // Hex letters A-F row (5 items + DEL)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("A", "B", "C", "D", "E").forEach { hexChar ->
                    val enabled = ProgrammerEngine.isCharAllowed(hexChar[0], activeBase)
                    CalcButton(
                        text = hexChar,
                        onClick = { if (enabled) viewModel.onProgrammerKey(hexChar) },
                        backgroundColor = if (enabled) theme.surfaceVariant else theme.surface.copy(alpha = 0.2f),
                        textColor = if (enabled) theme.accentSecondary else theme.textSecondary.copy(alpha = 0.2f),
                        shapeType = settings.buttonShape,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(42.dp)
                    )
                }
                CalcButton(
                    icon = Icons.AutoMirrored.Filled.Backspace,
                    onClick = { viewModel.onProgrammerKey("DEL") },
                    backgroundColor = theme.buttonActionBg,
                    textColor = theme.buttonActionText,
                    shapeType = settings.buttonShape,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(42.dp)
                )
            }

            // Row 4: F, 7, 8, 9, =
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val fEnabled = ProgrammerEngine.isCharAllowed('F', activeBase)
                CalcButton(
                    text = "F",
                    onClick = { if (fEnabled) viewModel.onProgrammerKey("F") },
                    backgroundColor = if (fEnabled) theme.surfaceVariant else theme.surface.copy(alpha = 0.2f),
                    textColor = if (fEnabled) theme.accentSecondary else theme.textSecondary.copy(alpha = 0.2f),
                    shapeType = settings.buttonShape,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp)
                )
                listOf("7", "8", "9").forEach { num ->
                    val enabled = ProgrammerEngine.isCharAllowed(num[0], activeBase)
                    CalcButton(
                        text = num,
                        onClick = { if (enabled) viewModel.onProgrammerKey(num) },
                        backgroundColor = if (enabled) theme.buttonNumberBg else theme.surface.copy(alpha = 0.2f),
                        textColor = if (enabled) theme.buttonNumberText else theme.textSecondary.copy(alpha = 0.2f),
                        shapeType = settings.buttonShape,
                        fontSize = 20.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(44.dp)
                    )
                }
                CalcButton(
                    text = "=",
                    onClick = { viewModel.onProgrammerKey("=") },
                    backgroundColor = theme.buttonEqualsBg,
                    textColor = theme.buttonEqualsText,
                    shapeType = settings.buttonShape,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp)
                )
            }

            // Row 5: 4, 5, 6, 1, 2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("4", "5", "6", "1", "2").forEach { num ->
                    val enabled = ProgrammerEngine.isCharAllowed(num[0], activeBase)
                    CalcButton(
                        text = num,
                        onClick = { if (enabled) viewModel.onProgrammerKey(num) },
                        backgroundColor = if (enabled) theme.buttonNumberBg else theme.surface.copy(alpha = 0.2f),
                        textColor = if (enabled) theme.buttonNumberText else theme.textSecondary.copy(alpha = 0.2f),
                        shapeType = settings.buttonShape,
                        fontSize = 20.sp,
                        isGlassmorphism = theme.isGlassmorphism,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        modifier = Modifier.weight(1f).height(44.dp)
                    )
                }
            }

            // Row 6: 3, 0
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val threeEnabled = ProgrammerEngine.isCharAllowed('3', activeBase)
                val zeroEnabled = ProgrammerEngine.isCharAllowed('0', activeBase)
                CalcButton(
                    text = "3",
                    onClick = { if (threeEnabled) viewModel.onProgrammerKey("3") },
                    backgroundColor = if (threeEnabled) theme.buttonNumberBg else theme.surface.copy(alpha = 0.2f),
                    textColor = if (threeEnabled) theme.buttonNumberText else theme.textSecondary.copy(alpha = 0.2f),
                    shapeType = settings.buttonShape,
                    fontSize = 20.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(1f).height(44.dp)
                )
                CalcButton(
                    text = "0",
                    onClick = { if (zeroEnabled) viewModel.onProgrammerKey("0") },
                    backgroundColor = if (zeroEnabled) theme.buttonNumberBg else theme.surface.copy(alpha = 0.2f),
                    textColor = if (zeroEnabled) theme.buttonNumberText else theme.textSecondary.copy(alpha = 0.2f),
                    shapeType = settings.buttonShape,
                    fontSize = 20.sp,
                    isGlassmorphism = theme.isGlassmorphism,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    modifier = Modifier.weight(2f).height(44.dp)
                )
            }
        }
    }
}
