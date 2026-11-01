package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.Slate800
import kotlinx.coroutines.launch

enum class PolyMood {
    HAPPY,
    TALKING,
    THINKING,
    CHEERING,
    SURPRISED
}

@Composable
fun PolyMascot(
    modifier: Modifier = Modifier,
    mood: PolyMood = PolyMood.HAPPY,
    size: Dp = 100.dp,
    speechBubbleText: String? = null,
    showHalo: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val bounceScale = remember { Animatable(1f) }
    val floatOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Gentle floating idle animation
    LaunchedEffect(Unit) {
        floatOffset.animateTo(
            targetValue = -6f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Speech Bubble if text provided
        if (!speechBubbleText.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .fillMaxWidth(0.92f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "🦜",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = speechBubbleText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Mascot Badge Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset { IntOffset(0, floatOffset.value.toInt()) }
                .scale(bounceScale.value)
                .clickable(enabled = onClick != null) {
                    scope.launch {
                        bounceScale.animateTo(0.88f, spring())
                        bounceScale.animateTo(1f, spring())
                    }
                    onClick?.invoke()
                }
        ) {
            // Background glowing halo
            if (showHalo) {
                Box(
                    modifier = Modifier
                        .size(size + 14.dp)
                        .clip(CircleShape)
                        .background(PolyGreenPrimary.copy(alpha = 0.18f))
                )
            }

            // High-resolution Poly Mascot Image
            Image(
                painter = painterResource(id = R.drawable.ic_mascot_poly),
                contentDescription = "Poly the colorful Parrot mascot",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .border(3.dp, PolyGreenPrimary, CircleShape)
                    .shadow(8.dp, CircleShape)
            )

            // Mood Badge Indicator at bottom right
            val moodEmoji = when (mood) {
                PolyMood.HAPPY -> "✨"
                PolyMood.TALKING -> "🗣️"
                PolyMood.THINKING -> "💡"
                PolyMood.CHEERING -> "🎉"
                PolyMood.SURPRISED -> "⭐"
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-2).dp, y = (-2).dp)
                    .size((size.value * 0.32f).dp.coerceAtLeast(26.dp))
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Text(
                    text = moodEmoji,
                    fontSize = (size.value * 0.16f).coerceAtLeast(12f).sp
                )
            }
        }
    }
}
