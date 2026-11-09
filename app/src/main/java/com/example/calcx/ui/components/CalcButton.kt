package com.example.calcx.ui.components

import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.theme.ButtonShapeType
import com.example.calcx.theme.toComposeShape

@Composable
fun CalcButton(
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    shapeType: ButtonShapeType = ButtonShapeType.ROUNDED,
    fontSize: TextUnit = 22.sp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    isGlassmorphism: Boolean = false,
    hapticEnabled: Boolean = true,
    soundEnabled: Boolean = true,
    testTag: String = text ?: "calc_btn"
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f),
        label = "btn_scale"
    )

    val shape = shapeType.toComposeShape()

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .then(
                if (isGlassmorphism) {
                    Modifier.border(
                        BorderStroke(
                            1.dp,
                            if (isPressed) textColor.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.18f)
                        ),
                        shape
                    )
                } else {
                    Modifier
                }
            )
            .background(
                color = if (isPressed) backgroundColor.copy(alpha = 0.75f) else backgroundColor,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (hapticEnabled) {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                }
                if (soundEnabled) {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                }
                onClick()
            }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = text ?: "Button icon",
                tint = textColor
            )
        } else if (text != null) {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        }
    }
}
