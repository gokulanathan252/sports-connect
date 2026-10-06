package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun HeroSection(
    onExploreSportsClick: () -> Unit,
    onStartTournamentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sportsIcons = listOf(
        "⚽", "🏏", "🏀", "🏑", "🏐", "🎾",
        "🏸", "🥋", "🏊", "🏃", "♟", "🎮"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "hero_orbit")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_logo"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Badge Header
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(SportsCyan.copy(alpha = 0.12f))
                .border(1.dp, SportsCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⚡ NEXT-GEN SPORTS PLATFORM",
                    color = SportsCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Brand Title
        Text(
            text = "SPORTSCONNECT",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Slogan with Gradient Glow
        Text(
            text = "“ONE PLATFORM. EVERY SPORT.”",
            color = SportsEmerald,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subtitle
        Text(
            text = "Manage tournaments, teams, players, fixtures, live scores and championships with one powerful platform.",
            color = Color(0xFF94A3B8),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Animated Sports Orbit Scene
        Box(
            modifier = Modifier
                .size(260.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Orbit Track Ring
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                SportsCyan.copy(alpha = 0.3f),
                                SportsEmerald.copy(alpha = 0.3f),
                                SportsGold.copy(alpha = 0.3f),
                                SportsCyan.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Inner Ring
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            )

            // Center Logo with pulse & glow
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF00E5FF), Color(0xFF0D1B2A))
                        )
                    )
                    .border(2.dp, SportsCyan, CircleShape)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sports_connect_logo),
                    contentDescription = "SportsConnect Crest",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // 12 Orbiting Floating Sports Icons
            val radiusPx = 100f // dp in float approx
            val angleStep = 360f / sportsIcons.size

            sportsIcons.forEachIndexed { index, icon ->
                val angleDeg = orbitAngle + (index * angleStep)
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val x = (radiusPx * cos(angleRad)).toFloat()
                val y = (radiusPx * sin(angleRad)).toFloat()

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x.roundToInt().dp.roundToPx(), y.roundToInt().dp.roundToPx()) }
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF151F30).copy(alpha = 0.92f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Call to action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = onExploreSportsClick,
                modifier = Modifier
                    .weight(1f)
                    .testTag("explore_sports_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SportsCyan,
                    contentColor = Color(0xFF04131D)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsScore,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = " Explore Sports",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            OutlinedButton(
                onClick = onStartTournamentClick,
                modifier = Modifier
                    .weight(1f)
                    .testTag("start_tournament_button"),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SportsEmerald),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SportsEmerald
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = " Start Tournament",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
