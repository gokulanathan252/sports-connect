package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SportsConnectBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sports_bg")

    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bg_pulse"
    )

    val surfaceColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Subtle angled sport field perspective lines
            val gridSpacing = 90f
            val gridColor = Color.White.copy(alpha = 0.025f)
            var y = 0f
            while (y < height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
                y += gridSpacing
            }
            var x = 0f
            while (x < width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1f
                )
                x += gridSpacing
            }

            // 2. Dynamic floating sports glowing orbs
            val orb1X = width * 0.2f + cos(pulsePhase) * 60f
            val orb1Y = height * 0.15f + sin(pulsePhase) * 45f

            val orb2X = width * 0.85f - sin(pulsePhase * 0.8f) * 70f
            val orb2Y = height * 0.65f + cos(pulsePhase * 0.8f) * 55f

            val orb3X = width * 0.45f + sin(pulsePhase * 0.5f) * 40f
            val orb3Y = height * 0.9f - cos(pulsePhase * 0.5f) * 50f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SportsCyan.copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(orb1X, orb1Y),
                    radius = width * 0.55f
                ),
                center = Offset(orb1X, orb1Y),
                radius = width * 0.55f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SportsEmerald.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(orb2X, orb2Y),
                    radius = width * 0.5f
                ),
                center = Offset(orb2X, orb2Y),
                radius = width * 0.5f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SportsGold.copy(alpha = 0.06f), Color.Transparent),
                    center = Offset(orb3X, orb3Y),
                    radius = width * 0.45f
                ),
                center = Offset(orb3X, orb3Y),
                radius = width * 0.45f
            )

            // 3. Floating athletic particles
            val particleOffsets = listOf(
                Offset(width * 0.12f, height * 0.28f) to 3f,
                Offset(width * 0.88f, height * 0.22f) to 2.5f,
                Offset(width * 0.25f, height * 0.72f) to 4f,
                Offset(width * 0.75f, height * 0.48f) to 3f,
                Offset(width * 0.52f, height * 0.35f) to 2f,
                Offset(width * 0.64f, height * 0.82f) to 3.5f
            )

            particleOffsets.forEachIndexed { i, (pos, rad) ->
                val dynamicY = pos.y + sin(pulsePhase + i.toFloat()) * 14f
                val dynamicX = pos.x + cos(pulsePhase + (i * 0.7f)) * 10f
                drawCircle(
                    color = SportsCyan.copy(alpha = 0.25f),
                    radius = rad,
                    center = Offset(dynamicX, dynamicY)
                )
            }
        }

        content()
    }
}
