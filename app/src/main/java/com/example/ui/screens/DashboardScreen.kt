package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Match
import com.example.data.Tournament
import com.example.ui.components.LiveMatchCard
import com.example.ui.components.PulsingLiveIndicator
import com.example.ui.components.StatCounterCard
import com.example.ui.components.TournamentCard
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun DashboardScreen(
    liveMatches: List<Match>,
    tournaments: List<Tournament>,
    onAddTournamentClick: () -> Unit,
    onAddMatchClick: () -> Unit,
    onAddTeamClick: () -> Unit,
    onAddPlayerClick: () -> Unit,
    onViewMatch: (Match) -> Unit,
    onViewTournament: (Tournament) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToPointsTable: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Welcome Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good Morning, Admin 👋",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Manage your sports universe",
                            color = SportsCyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SportsEmerald.copy(alpha = 0.15f))
                            .border(1.dp, SportsEmerald.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "PRO ADMIN",
                            color = SportsEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // Animated Statistics Grid
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCounterCard(
                        title = "Total Sports",
                        targetValue = 20,
                        suffix = "+",
                        icon = "🏆",
                        accentColor = SportsCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatCounterCard(
                        title = "Tournaments",
                        targetValue = 128,
                        icon = "🏟️",
                        accentColor = SportsGold,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCounterCard(
                        title = "Teams",
                        targetValue = 450,
                        suffix = "+",
                        icon = "🛡️",
                        accentColor = SportsEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    StatCounterCard(
                        title = "Players",
                        targetValue = 5200,
                        suffix = "+",
                        icon = "👟",
                        accentColor = Color(0xFF64B5F6),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCounterCard(
                        title = "Live Matches",
                        targetValue = 12,
                        icon = "🔴",
                        accentColor = Color(0xFFFF3366),
                        modifier = Modifier.weight(1f)
                    )
                    StatCounterCard(
                        title = "Completed",
                        targetValue = 1250,
                        suffix = "+",
                        icon = "✅",
                        accentColor = Color(0xFF81C784),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Admin Actions
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "QUICK ACTIONS",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        ActionChip(
                            icon = Icons.Default.EmojiEvents,
                            label = "+ Tournament",
                            onClick = onAddTournamentClick,
                            color = SportsCyan
                        )
                    }
                    item {
                        ActionChip(
                            icon = Icons.Default.SportsScore,
                            label = "+ Match",
                            onClick = onAddMatchClick,
                            color = SportsEmerald
                        )
                    }
                    item {
                        ActionChip(
                            icon = Icons.Default.Groups,
                            label = "+ Team",
                            onClick = onAddTeamClick,
                            color = SportsGold
                        )
                    }
                    item {
                        ActionChip(
                            icon = Icons.Default.PersonAdd,
                            label = "+ Player",
                            onClick = onAddPlayerClick,
                            color = Color(0xFF64B5F6)
                        )
                    }
                }
            }
        }

        // Live Matches Section
        item {
            Spacer(modifier = Modifier.height(26.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulsingLiveIndicator()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE MATCH CENTER",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(liveMatches) { match ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                LiveMatchCard(
                    match = match,
                    onMatchClick = { onViewMatch(match) }
                )
            }
        }

        // Shortcuts: Analytics & Points Table
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAnalytics() }
                        .testTag("dashboard_analytics_shortcut"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SportsCyan.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.QueryStats, null, tint = SportsCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Analytics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Growth & KPIs", color = Color(0xFF94A3B8), fontSize = 10.sp)
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToPointsTable() }
                        .testTag("dashboard_standings_shortcut"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SportsGold.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.EmojiEvents, null, tint = SportsGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Standings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Points Table", color = Color(0xFF94A3B8), fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Active Tournaments Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "ACTIVE TOURNAMENTS",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(tournaments) { tour ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                TournamentCard(
                    tournament = tour,
                    onViewTournament = { onViewTournament(tour) }
                )
            }
        }
    }
}

@Composable
private fun ActionChip(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141F30)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
