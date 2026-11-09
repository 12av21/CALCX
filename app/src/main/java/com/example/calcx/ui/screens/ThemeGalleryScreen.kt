package com.example.calcx.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.theme.*
import com.example.calcx.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeGalleryScreen(
    viewModel: CalculatorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val theme = viewModel.themeManager.currentTheme
    val settings = viewModel.themeManager.currentSettings
    val context = LocalContext.current

    var showCustomEditor by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Gallery", fontWeight = FontWeight.Bold, color = theme.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = theme.textPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.themeManager.resetToDefaults()
                        Toast.makeText(context, "Reset to default theme", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.RestartAlt, contentDescription = "Reset", tint = theme.textSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = theme.background)
            )
        },
        containerColor = theme.background,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Button Shape Selector Section
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Button Shape",
                        color = theme.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ButtonShapeType.entries.forEach { shape ->
                            val isSelected = settings.buttonShape == shape
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(shape.toComposeShape())
                                    .background(if (isSelected) theme.accent else theme.surfaceVariant)
                                    .border(
                                        1.dp,
                                        if (isSelected) theme.accentSecondary else Color.Transparent,
                                        shape.toComposeShape()
                                    )
                                    .clickable { viewModel.themeManager.setButtonShape(shape) }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = shape.displayName,
                                    color = if (isSelected) theme.buttonEqualsText else theme.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Button Scale Selector
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Button Density",
                        color = theme.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ButtonScale.entries.forEach { scale ->
                            val isSelected = settings.buttonScale == scale
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.themeManager.setButtonScale(scale) },
                                label = { Text(scale.label) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Custom Theme Editor Trigger
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCustomEditor = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(theme.accent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = theme.buttonEqualsText)
                        }
                        Column {
                            Text("Custom Theme Creator", fontWeight = FontWeight.Bold, color = theme.textPrimary, fontSize = 16.sp)
                            Text("Choose your own background, accents & button colors", color = theme.textSecondary, fontSize = 12.sp)
                        }
                    }
                    if (settings.customThemeActive) {
                        Text("Active", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // 10 Preset Themes Grid
            Text(
                text = "Preset Themes",
                color = theme.accent,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            PresetThemes.allPresets.chunked(2).forEach { rowPresets ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowPresets.forEach { preset ->
                        val isSelected = !settings.customThemeActive && settings.themeId == preset.id
                        ThemeCard(
                            preset = preset,
                            isSelected = isSelected,
                            shapeType = settings.buttonShape,
                            onSelect = {
                                viewModel.themeManager.setTheme(preset.id)
                                Toast.makeText(context, "Applied: ${preset.name}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowPresets.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    if (showCustomEditor) {
        CustomThemeEditorDialog(
            theme = theme,
            onDismiss = { showCustomEditor = false },
            onSave = { bg, accent, btn ->
                viewModel.themeManager.saveCustomTheme(bg, accent, btn)
                showCustomEditor = false
                Toast.makeText(context, "Custom theme saved!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun ThemeCard(
    preset: ThemeColors,
    isSelected: Boolean,
    shapeType: ButtonShapeType,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = preset.background),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(preset.accent), width = 2.dp) else null,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Theme title and checkmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = preset.name,
                    color = preset.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = preset.accent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mini calculator preview inside card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(preset.displayBg)
                    .padding(6.dp)
            ) {
                Text("1,248", color = preset.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                // Mini 3x3 button row
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(18.dp).clip(shapeType.toComposeShape()).background(preset.buttonNumberBg))
                    Box(modifier = Modifier.size(18.dp).clip(shapeType.toComposeShape()).background(preset.buttonNumberBg))
                    Box(modifier = Modifier.size(18.dp).clip(shapeType.toComposeShape()).background(preset.buttonOperatorBg))
                    Box(modifier = Modifier.size(18.dp).clip(shapeType.toComposeShape()).background(preset.buttonEqualsBg))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Swatches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(preset.accent, preset.buttonOperatorBg, preset.buttonNumberBg, preset.surface).forEach { c ->
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(c)
                            .border(0.5.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomThemeEditorDialog(
    theme: ThemeColors,
    onDismiss: () -> Unit,
    onSave: (bgColor: Color, accentColor: Color, btnColor: Color) -> Unit
) {
    val bgColors = listOf(
        Color(0xFF0F172A), Color(0xFF000000), Color(0xFF1E1E2E),
        Color(0xFF181824), Color(0xFF1A1A1A), Color(0xFF262626), Color(0xFFF8FAFC)
    )
    val accentColors = listOf(
        Color(0xFF38BDF8), Color(0xFF00E5FF), Color(0xFF00E676),
        Color(0xFFFF6B4A), Color(0xFFA855F7), Color(0xFFFB7185), Color(0xFFFFD600)
    )
    val btnColors = listOf(
        Color(0xFF1E293B), Color(0xFF27272A), Color(0xFF333333),
        Color(0xFF1B2838), Color(0xFF2A1B3D), Color(0xFF3D271D), Color(0xFFE2E8F0)
    )

    var selectedBg by remember { mutableStateOf(bgColors.first()) }
    var selectedAccent by remember { mutableStateOf(accentColors.first()) }
    var selectedBtn by remember { mutableStateOf(btnColors.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Customize Theme", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Background Color", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    bgColors.forEach { c ->
                        ColorSelectCircle(color = c, isSelected = selectedBg == c) { selectedBg = c }
                    }
                }

                Text("Accent Color", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    accentColors.forEach { c ->
                        ColorSelectCircle(color = c, isSelected = selectedAccent == c) { selectedAccent = c }
                    }
                }

                Text("Button Color", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    btnColors.forEach { c ->
                        ColorSelectCircle(color = c, isSelected = selectedBtn == c) { selectedBtn = c }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Live Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(selectedBg)
                        .padding(12.dp)
                ) {
                    Column {
                        Text("Preview", color = selectedAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("3,450", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(selectedBtn))
                            Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(selectedAccent))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(selectedBg, selectedAccent, selectedBtn) }) {
                Text("Apply & Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ColorSelectCircle(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                if (isSelected) 2.5.dp else 0.5.dp,
                if (isSelected) Color.White else Color.Gray,
                CircleShape
            )
            .clickable { onClick() }
    )
}
