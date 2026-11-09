package com.example.calcx.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.engine.AngleUnit
import com.example.calcx.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: CalculatorViewModel,
    onBack: () -> Unit,
    onOpenThemes: () -> Unit,
    onOpenGuide: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val theme = viewModel.themeManager.currentTheme
    val settings = viewModel.themeManager.currentSettings
    val context = LocalContext.current

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold, color = theme.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = theme.textPrimary)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section: Appearance & Feedback
            SettingsSectionHeader(title = "Appearance & Feedback", color = theme.accent)

            SettingsCard(theme = theme) {
                SettingsNavigationRow(
                    title = "Theme & Customization",
                    subtitle = "10 presets, custom colors & button shapes",
                    icon = Icons.Default.Palette,
                    accentColor = theme.accent,
                    onClick = onOpenThemes
                )

                HorizontalDivider(color = theme.surfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                SettingsSwitchRow(
                    title = "Haptic Tactile Feedback",
                    subtitle = "Vibrate subtly on keypad press",
                    checked = settings.hapticEnabled,
                    icon = Icons.Default.Vibration,
                    accentColor = theme.accent,
                    onCheckedChange = { viewModel.themeManager.setHapticEnabled(it) }
                )

                HorizontalDivider(color = theme.surfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                SettingsSwitchRow(
                    title = "Sound Effects",
                    subtitle = "Audio feedback click on button tap",
                    checked = settings.soundEnabled,
                    icon = Icons.Default.VolumeUp,
                    accentColor = theme.accent,
                    onCheckedChange = { viewModel.themeManager.setSoundEnabled(it) }
                )
            }

            // Section: Calculation Engine
            SettingsSectionHeader(title = "Calculation Engine", color = theme.accent)

            SettingsCard(theme = theme) {
                // Decimal Precision
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Decimal Precision", color = theme.textPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Number of decimal digits in result", color = theme.textSecondary, fontSize = 12.sp)
                        }
                        Text("${settings.precision} decimals", color = theme.accent, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(2, 4, 6, 8, 10).forEach { p ->
                            FilterChip(
                                selected = settings.precision == p,
                                onClick = { viewModel.themeManager.setPrecision(p) },
                                label = { Text("$p") }
                            )
                        }
                    }
                }

                HorizontalDivider(color = theme.surfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                // Angle Unit
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Trigonometric Angle Unit", color = theme.textPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Degree (360°) vs Radian (2π)", color = theme.textSecondary, fontSize = 12.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(
                            selected = settings.angleUnit == AngleUnit.DEGREE,
                            onClick = { viewModel.themeManager.setAngleUnit(AngleUnit.DEGREE) },
                            label = { Text("DEG") }
                        )
                        FilterChip(
                            selected = settings.angleUnit == AngleUnit.RADIAN,
                            onClick = { viewModel.themeManager.setAngleUnit(AngleUnit.RADIAN) },
                            label = { Text("RAD") }
                        )
                    }
                }

                HorizontalDivider(color = theme.surfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                // Thousands separator
                SettingsSwitchRow(
                    title = "Thousands Separator",
                    subtitle = "Format large numbers with commas (e.g. 1,000,000)",
                    checked = settings.thousandsSeparator,
                    icon = Icons.Default.FormatListNumbered,
                    accentColor = theme.accent,
                    onCheckedChange = { viewModel.themeManager.setThousandsSeparator(it) }
                )
            }

            // Section: Data & Storage
            SettingsSectionHeader(title = "Data & Storage", color = theme.accent)

            SettingsCard(theme = theme) {
                SettingsActionRow(
                    title = "Clear Calculation History",
                    subtitle = "Delete all stored calculations in local SQLite database",
                    icon = Icons.Default.DeleteSweep,
                    tint = Color(0xFFFF5252),
                    onClick = { showClearHistoryDialog = true }
                )
            }

            // Section: Help & Guide
            SettingsSectionHeader(title = "Help & Documentation", color = theme.accent)

            SettingsCard(theme = theme) {
                SettingsActionRow(
                    title = "How to Use CALCX",
                    subtitle = "Interactive tutorial for basic, scientific, programmer & finance tools",
                    icon = Icons.Default.HelpOutline,
                    tint = theme.accent,
                    onClick = onOpenGuide
                )
            }

            // Section: About
            SettingsSectionHeader(title = "About Application", color = theme.accent)

            SettingsCard(theme = theme) {
                SettingsActionRow(
                    title = "CALCX – Smart Calculator",
                    subtitle = "Version 1.0.0 • \"Calculate Smarter. Live Simpler.\"",
                    icon = Icons.Default.Info,
                    tint = theme.accent,
                    onClick = { showAboutDialog = true }
                )
            }
        }
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text("Clear All History?") },
            text = { Text("Are you sure you want to delete all saved calculations?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearHistoryDialog = false
                        Toast.makeText(context, "All history cleared", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Clear All", color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("CALCX – Ultimate Smart Calculator", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tagline: Calculate Smarter. Live Simpler.", fontWeight = FontWeight.SemiBold, color = theme.accent)
                    Text("A modern, fast, offline-first calculator for Android with Standard, Scientific, Programmer, Financial, and Unit Converter engines.")
                    Text("Features Room SQLite local persistence, customizable themes, bitboards, and high-precision mathematics.")
                    Text("Version: 1.0.0")
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String, color: Color) {
    Text(
        text = title,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
fun SettingsCard(theme: com.example.calcx.theme.ThemeColors, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        content = content
    )
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    icon: ImageVector,
    accentColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor)
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsNavigationRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor)
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint)
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = tint)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}
