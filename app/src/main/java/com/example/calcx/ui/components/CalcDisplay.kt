package com.example.calcx.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.engine.AngleUnit
import com.example.calcx.theme.ThemeColors

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CalcDisplay(
    expression: String,
    result: String,
    preview: String?,
    theme: ThemeColors,
    angleUnit: AngleUnit,
    memoryValue: Double?,
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenThemes: () -> Unit,
    onToggleAngleUnit: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exprScrollState = rememberScrollState()

    // Auto scroll expression to end when it changes
    LaunchedEffect(expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(theme.displayBg)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .animateContentSize()
    ) {
        // Status Bar Row (DEG/RAD, Memory, Undo, Redo, Shortcuts)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onToggleAngleUnit != null) {
                    AssistChip(
                        onClick = onToggleAngleUnit,
                        label = {
                            Text(
                                text = if (angleUnit == AngleUnit.DEGREE) "DEG" else "RAD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = theme.accent
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = theme.surface.copy(alpha = 0.6f)
                        ),
                        border = AssistChipDefaults.assistChipBorder(
                            enabled = true,
                            borderColor = theme.accent.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.height(28.dp)
                    )
                }

                if (memoryValue != null && memoryValue != 0.0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(theme.accentSecondary.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "M",
                            color = theme.accentSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Toolbar Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onUndo,
                    enabled = canUndo,
                    modifier = Modifier.size(32.dp).testTag("action_undo")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (canUndo) theme.textSecondary else theme.textSecondary.copy(alpha = 0.25f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = onRedo,
                    enabled = canRedo,
                    modifier = Modifier.size(32.dp).testTag("action_redo")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (canRedo) theme.textSecondary else theme.textSecondary.copy(alpha = 0.25f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = {
                        val textToCopy = if (result.isNotEmpty() && result != "0") result else expression
                        if (textToCopy.isNotEmpty()) {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("CALCX Result", textToCopy)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied: $textToCopy", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.size(32.dp).testTag("action_copy")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy result",
                        tint = theme.textSecondary,
                        modifier = Modifier.size(17.dp)
                    )
                }
                IconButton(
                    onClick = {
                        val shareText = if (result.isNotEmpty()) "$expression = $result" else expression
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share calculation"))
                    },
                    modifier = Modifier.size(32.dp).testTag("action_share")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = theme.textSecondary,
                        modifier = Modifier.size(17.dp)
                    )
                }
                IconButton(
                    onClick = onOpenHistory,
                    modifier = Modifier.size(32.dp).testTag("action_history")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = theme.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = onOpenThemes,
                    modifier = Modifier.size(32.dp).testTag("action_theme")
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Themes",
                        tint = theme.accent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Expression display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(exprScrollState),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = if (expression.isEmpty()) " " else expression,
                color = theme.textSecondary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.testTag("expression_display")
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Live preview indicator if active
        AnimatedVisibility(
            visible = !preview.isNullOrEmpty() && preview != result,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "= $preview",
                color = theme.accent.copy(alpha = 0.85f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth().padding(end = 4.dp).testTag("preview_display")
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Main Result Display
        val resultFontSize = when {
            result.length > 14 -> 32.sp
            result.length > 9 -> 40.sp
            else -> 48.sp
        }

        Text(
            text = if (result.isEmpty()) "0" else result,
            color = theme.textPrimary,
            fontSize = resultFontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { },
                    onLongClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("CALCX Result", result)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied result: $result", Toast.LENGTH_SHORT).show()
                    }
                )
                .testTag("result_display")
        )
    }
}
