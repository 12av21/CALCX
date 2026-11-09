package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calcx.ui.components.CalcBottomNavigation
import com.example.calcx.ui.screens.*
import com.example.calcx.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalcxApp()
        }
    }
}

@Composable
fun CalcxApp() {
    val viewModel: CalculatorViewModel = viewModel()
    val theme = viewModel.themeManager.currentTheme
    var currentScreen by remember { mutableStateOf(viewModel.currentScreen) }
    var previousScreen by remember { mutableStateOf("STANDARD") }

    fun navigateTo(screen: String) {
        if (screen != currentScreen) {
            previousScreen = currentScreen
            currentScreen = screen
        }
    }

    fun navigateBack() {
        if (currentScreen in listOf("HISTORY", "THEMES", "SETTINGS")) {
            currentScreen = previousScreen.ifEmpty { "STANDARD" }
        } else if (currentScreen != "STANDARD") {
            currentScreen = "STANDARD"
        }
    }

    BackHandler(enabled = currentScreen != "STANDARD") {
        navigateBack()
    }

    val isSubScreen = currentScreen in listOf("HISTORY", "THEMES")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isSubScreen) {
                CalcBottomNavigation(
                    currentScreen = currentScreen,
                    onSelectScreen = { navigateTo(it) },
                    theme = theme
                )
            }
        },
        containerColor = theme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(theme.background)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    "STANDARD" -> StandardCalculatorScreen(
                        viewModel = viewModel,
                        onOpenHistory = { navigateTo("HISTORY") },
                        onOpenThemes = { navigateTo("THEMES") }
                    )
                    "SCIENTIFIC" -> ScientificCalculatorScreen(
                        viewModel = viewModel,
                        onOpenHistory = { navigateTo("HISTORY") },
                        onOpenThemes = { navigateTo("THEMES") }
                    )
                    "PROGRAMMER" -> ProgrammerCalculatorScreen(
                        viewModel = viewModel
                    )
                    "FINANCIAL" -> FinancialCalculatorScreen(
                        viewModel = viewModel
                    )
                    "UNIT" -> UnitConverterScreen(
                        viewModel = viewModel
                    )
                    "HISTORY" -> HistoryScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() }
                    )
                    "THEMES" -> ThemeGalleryScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() }
                    )
                    "SETTINGS" -> SettingsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onOpenThemes = { navigateTo("THEMES") }
                    )
                    else -> StandardCalculatorScreen(
                        viewModel = viewModel,
                        onOpenHistory = { navigateTo("HISTORY") },
                        onOpenThemes = { navigateTo("THEMES") }
                    )
                }
            }
        }
    }
}
