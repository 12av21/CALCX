package com.example.calcx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.engine.UnitCategory
import com.example.calcx.engine.UnitConverterEngine
import com.example.calcx.engine.UnitItem
import com.example.calcx.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = viewModel.themeManager.currentTheme
    var selectedCategory by remember { mutableStateOf(UnitCategory.LENGTH) }

    val unitsForCategory = remember(selectedCategory) {
        UnitConverterEngine.unitsByCategory[selectedCategory] ?: emptyList()
    }

    var fromUnit by remember(selectedCategory) {
        mutableStateOf(unitsForCategory.firstOrNull() ?: UnitItem("Default", "", 1.0))
    }
    var toUnit by remember(selectedCategory) {
        mutableStateOf(if (unitsForCategory.size > 1) unitsForCategory[1] else fromUnit)
    }

    var inputValueStr by remember { mutableStateOf("1") }
    val inputValue = inputValueStr.toDoubleOrNull() ?: 0.0

    val convertedValue = remember(inputValue, fromUnit, toUnit, selectedCategory) {
        UnitConverterEngine.convert(inputValue, fromUnit, toUnit, selectedCategory)
    }

    var showFromMenu by remember { mutableStateOf(false) }
    var showToMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        // Categories Horizontal Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.surface)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UnitCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedCategory = category
                        val list = UnitConverterEngine.unitsByCategory[category] ?: emptyList()
                        if (list.isNotEmpty()) {
                            fromUnit = list.first()
                            toUnit = if (list.size > 1) list[1] else list.first()
                        }
                    },
                    label = {
                        Text(
                            text = category.displayName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) theme.buttonEqualsText else theme.textSecondary
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = theme.accent,
                        containerColor = theme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) theme.accent else Color.Transparent
                    )
                )
            }
        }

        // Conversion Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // "From" Input Box
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("FROM", color = theme.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        // From Dropdown
                        Box {
                            TextButton(onClick = { showFromMenu = true }) {
                                Text("${fromUnit.name} (${fromUnit.symbol})", color = theme.accent, fontWeight = FontWeight.Bold)
                            }
                            DropdownMenu(
                                expanded = showFromMenu,
                                onDismissRequest = { showFromMenu = false },
                                modifier = Modifier.background(theme.surface)
                            ) {
                                unitsForCategory.forEach { u ->
                                    DropdownMenuItem(
                                        text = { Text("${u.name} (${u.symbol})", color = theme.textPrimary) },
                                        onClick = {
                                            fromUnit = u
                                            showFromMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = inputValueStr,
                        onValueChange = { inputValueStr = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Swap Button Center
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(theme.accent)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap units",
                        tint = theme.buttonEqualsText
                    )
                }
            }

            // "To" Result Box
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TO", color = theme.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        // To Dropdown
                        Box {
                            TextButton(onClick = { showToMenu = true }) {
                                Text("${toUnit.name} (${toUnit.symbol})", color = theme.accent, fontWeight = FontWeight.Bold)
                            }
                            DropdownMenu(
                                expanded = showToMenu,
                                onDismissRequest = { showToMenu = false },
                                modifier = Modifier.background(theme.surface)
                            ) {
                                unitsForCategory.forEach { u ->
                                    DropdownMenuItem(
                                        text = { Text("${u.name} (${u.symbol})", color = theme.textPrimary) },
                                        onClick = {
                                            toUnit = u
                                            showToMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val formatted = if (convertedValue.isNaN() || convertedValue.isInfinite()) {
                        "Error"
                    } else if (kotlin.math.abs(convertedValue) < 1e-4 || kotlin.math.abs(convertedValue) >= 1e9) {
                        "%.6e".format(convertedValue)
                    } else {
                        "%.6f".format(convertedValue).trimEnd('0').trimEnd('.')
                    }

                    Text(
                        text = "$formatted ${toUnit.symbol}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.accent
                    )
                }
            }

            // Formula / Equivalent note
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Conversion Rate", color = theme.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    val oneUnit = UnitConverterEngine.convert(1.0, fromUnit, toUnit, selectedCategory)
                    Text(
                        text = "1 ${fromUnit.symbol} = ${"%.6f".format(oneUnit).trimEnd('0').trimEnd('.')} ${toUnit.symbol}",
                        color = theme.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
