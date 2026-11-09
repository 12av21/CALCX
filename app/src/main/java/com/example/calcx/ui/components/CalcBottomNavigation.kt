package com.example.calcx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.theme.ThemeColors

data class NavTabItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val MainNavTabs = listOf(
    NavTabItem("STANDARD", "Standard", Icons.Default.Calculate),
    NavTabItem("SCIENTIFIC", "Scientific", Icons.Default.Functions),
    NavTabItem("PROGRAMMER", "Code", Icons.Default.Terminal),
    NavTabItem("FINANCIAL", "Finance", Icons.Default.MonetizationOn),
    NavTabItem("UNIT", "Convert", Icons.Default.SyncAlt),
    NavTabItem("SETTINGS", "Settings", Icons.Default.Settings)
)

@Composable
fun CalcBottomNavigation(
    currentScreen: String,
    onSelectScreen: (String) -> Unit,
    theme: ThemeColors,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = theme.surface,
        tonalElevation = 6.dp
    ) {
        MainNavTabs.forEach { tab ->
            val isSelected = currentScreen == tab.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectScreen(tab.route) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        tint = if (isSelected) theme.buttonEqualsText else theme.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) theme.accent else theme.textSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = theme.accent
                )
            )
        }
    }
}
