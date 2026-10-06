package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Match
import com.example.data.Sport
import com.example.data.Tournament
import com.example.ui.components.HeroSection
import com.example.ui.components.LiveMatchCard
import com.example.ui.components.PulsingLiveIndicator
import com.example.ui.components.TournamentCard
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun LandingScreen(
    tournaments: List<Tournament>,
    liveMatches: List<Match>,
    sports: List<Sport>,
    onExploreSportsClick: () -> Unit,
    onStartTournamentClick: () -> Unit,
    onViewTournament: (Tournament) -> Unit,
    onViewMatch: (Match) -> Unit,
    onOpenDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("landing_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Section
        item {
            HeroSection(
                onExploreSportsClick = onExploreSportsClick,
                onStartTournamentClick = onStartTournamentClick
            )
        }

        // Live Ticker Banner
        if (liveMatches.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PulsingLiveIndicator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "HAPPENING NOW",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${liveMatches.size} Matches Active",
                            color = SportsCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LiveMatchCard(
                        match = liveMatches.first(),
                        onMatchClick = { onViewMatch(liveMatches.first()) }
                    )
                }
            }
        }

        // Key Value Propositions / Feature highlights
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeaturePill(
                    icon = Icons.Default.Bolt,
                    title = "Instant Scores",
                    subtitle = "Real-time sync",
                    color = SportsCyan,
                    modifier = Modifier.weight(1f)
                )
                FeaturePill(
                    icon = Icons.Default.EmojiEvents,
                    title = "Auto Brackets",
                    subtitle = "Knockout trees",
                    color = SportsGold,
                    modifier = Modifier.weight(1f)
                )
                FeaturePill(
                    icon = Icons.Default.Security,
                    title = "Certified Tech",
                    subtitle = "Official data",
                    color = SportsEmerald,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Featured Tournaments Header
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FEATURED TOURNAMENTS",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Top tier championships open for registration & viewing",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Featured Tournament list
        items(tournaments.take(3)) { tour ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                TournamentCard(
                    tournament = tour,
                    onViewTournament = { onViewTournament(tour) }
                )
            }
        }

        // Enter Admin Dashboard Banner
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E)),
                border = androidx.compose.foundation.BorderStroke(1.dp, SportsCyan.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Admin Control Center",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage teams, schedules, brackets, officials and venues",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = onOpenDashboard,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SportsCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Dashboard →", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF101927)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = Color(0xFF64748B),
                fontSize = 10.sp
            )
        }
    }
}
