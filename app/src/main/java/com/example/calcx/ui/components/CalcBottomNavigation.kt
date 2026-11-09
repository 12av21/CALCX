package com.example.calcx.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
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
    NavTabItem("STANDARD", "Basic", Icons.Default.Calculate),
    NavTabItem("SCIENTIFIC", "Science", Icons.Default.Functions),
    NavTabItem("PROGRAMMER", "Code", Icons.Default.Terminal),
    NavTabItem("FINANCIAL", "Finance", Icons.Default.MonetizationOn),
    NavTabItem("UNIT", "Convert", Icons.Default.SwapHoriz)
)

@Composable
fun CalcBottomNavigation(
    currentScreen: String,
    onSelectScreen: (String) -> Unit,
    theme: ThemeColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = theme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = theme.surfaceVariant.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainNavTabs.forEach { tab ->
                    val isSelected = currentScreen == tab.route

                    val indicatorBg by animateColorAsState(
                        targetValue = if (isSelected) theme.accent else Color.Transparent,
                        label = "indicator_color"
                    )
                    val iconTint by animateColorAsState(
                        targetValue = if (isSelected) theme.buttonEqualsText else theme.textSecondary,
                        label = "icon_tint"
                    )
                    val labelColor by animateColorAsState(
                        targetValue = if (isSelected) theme.accent else theme.textSecondary.copy(alpha = 0.8f),
                        label = "label_color"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onSelectScreen(tab.route)
                            }
                            .testTag("nav_tab_${tab.route.lowercase()}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Icon with sleek active pill indicator
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .widthIn(min = 44.dp, max = 56.dp)
                                .clip(CircleShape)
                                .background(indicatorBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = iconTint,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        // Single-line crisp label
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = labelColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
