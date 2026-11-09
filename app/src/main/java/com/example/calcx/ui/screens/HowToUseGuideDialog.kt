package com.example.calcx.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.calcx.theme.ThemeColors

data class GuideStep(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val tagColor: Color,
    val highlights: List<Pair<String, String>>
)

@Composable
fun HowToUseGuideDialog(
    theme: ThemeColors,
    onDismiss: () -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }

    val steps = listOf(
        GuideStep(
            title = "Welcome to CALCX",
            subtitle = "Calculate Smarter. Live Simpler.",
            icon = Icons.Default.Calculate,
            tagColor = theme.accent,
            highlights = listOf(
                "5 Engines in One" to "Seamlessly switch between Basic, Science, Programmer, Finance, and Unit Converter via the bottom bar.",
                "Live Preview" to "Results evaluate in real-time as you type, before you even press '='.",
                "Undo & Redo" to "Fix typos instantly with the top Undo & Redo arrow buttons without clearing your work.",
                "Tap to Copy" to "Tap or long-press the large result number to copy it to your clipboard."
            )
        ),
        GuideStep(
            title = "Scientific Calculator",
            subtitle = "Advanced trigonometric, logarithmic & power math",
            icon = Icons.Default.Functions,
            tagColor = Color(0xFF00E5FF),
            highlights = listOf(
                "DEG / RAD Toggle" to "Tap the DEG/RAD badge at the top to toggle between degrees and radians.",
                "2nd Function Key" to "Tap '2nd' to unlock inverse trig (sin⁻¹, cos⁻¹, tan⁻¹), cube root (∛), 10ˣ, and eˣ.",
                "Powers & Roots" to "Use 'x^y' for exponents, '√' for square roots, and '!' for factorials (e.g. 5! = 120).",
                "Parentheses" to "Group complex equations with '(' and ')' with strict mathematical precedence."
            )
        ),
        GuideStep(
            title = "Programmer Calculator",
            subtitle = "Bitwise logic, multi-radix & interactive Bitboard",
            icon = Icons.Default.Terminal,
            tagColor = Color(0xFF00FF66),
            highlights = listOf(
                "4-Base Sync" to "Live simultaneous readouts in HEX, DEC, OCT, and BIN. Tap any base to switch active input.",
                "Interactive Bitboard" to "Tap any individual bit chip (0 or 1) in the bitboard row to flip its state directly!",
                "Word Sizing" to "Toggle between QWORD (64-bit), DWORD (32-bit), WORD (16-bit), and BYTE (8-bit).",
                "Bitwise Ops" to "Execute AND, OR, XOR, NOT, NAND, NOR, XNOR, shifts (LSH, RSH), and rotations (ROL, ROR)."
            )
        ),
        GuideStep(
            title = "Finance & Unit Converter",
            subtitle = "Everyday practical money and measurement tools",
            icon = Icons.Default.MonetizationOn,
            tagColor = Color(0xFFFFB03A),
            highlights = listOf(
                "Loan / EMI Calculator" to "Calculate monthly installments, total interest, and total repayment effortlessly.",
                "Tip & Bill Splitter" to "Choose tip % and split bill amounts among any number of people with live breakdowns.",
                "Tax & GST" to "Add or remove GST with automatic CGST and SGST splits.",
                "12 Unit Categories" to "Instant conversion for Length, Weight, Temp, Area, Volume, Speed, Time, Data Storage, Energy, Pressure, Power, and Currency."
            )
        ),
        GuideStep(
            title = "Hacker Themes & Customization",
            subtitle = "Personalize your workspace aesthetic",
            icon = Icons.Default.Palette,
            tagColor = Color(0xFFBD93F9),
            highlights = listOf(
                "Hacker Favorites" to "Choose from Matrix CRT, Kali Linux, Dracula Dark, Nord Frost, Mr. Robot Red, and Cyber Neon.",
                "Button Shapes" to "Switch between Rounded (18dp), Pill (circular), or Square (6dp) buttons.",
                "Custom Theme Creator" to "Pick your own custom background, accent, and button color palette with live preview.",
                "Persistent History" to "All completed calculations are automatically saved in SQLite. Tap any item to reload it."
            )
        )
    )

    val currentStep = steps[currentStepIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.82f)
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = theme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(currentStep.tagColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = currentStep.icon,
                                contentDescription = null,
                                tint = currentStep.tagColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "HOW TO USE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentStep.tagColor,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = theme.textSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Content Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column {
                        Text(
                            text = currentStep.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = theme.textPrimary
                        )
                        Text(
                            text = currentStep.subtitle,
                            fontSize = 14.sp,
                            color = theme.textSecondary
                        )
                    }

                    HorizontalDivider(color = theme.surfaceVariant.copy(alpha = 0.5f))

                    currentStep.highlights.forEach { (heading, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.surfaceVariant.copy(alpha = 0.35f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(currentStep.tagColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = currentStep.tagColor,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = heading,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = theme.textPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = desc,
                                    fontSize = 13.sp,
                                    color = theme.textSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer Row with Step Dots & Navigation
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Step Dots Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        steps.indices.forEach { index ->
                            val isSelected = index == currentStepIndex
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .height(6.dp)
                                    .width(if (isSelected) 24.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) currentStep.tagColor else theme.surfaceVariant)
                                    .clickable { currentStepIndex = index }
                            )
                        }
                    }

                    // Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentStepIndex > 0) {
                            TextButton(onClick = { currentStepIndex-- }) {
                                Text("Previous", color = theme.textSecondary)
                            }
                        } else {
                            TextButton(onClick = onDismiss) {
                                Text("Skip", color = theme.textSecondary)
                            }
                        }

                        Button(
                            onClick = {
                                if (currentStepIndex < steps.lastIndex) {
                                    currentStepIndex++
                                } else {
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentStep.tagColor,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (currentStepIndex < steps.lastIndex) "Next Tip" else "Got It! Start",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
