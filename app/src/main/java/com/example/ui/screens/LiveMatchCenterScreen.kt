package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Match
import com.example.data.MatchStatus
import com.example.ui.components.LiveMatchCard
import com.example.ui.components.PulsingLiveIndicator
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun LiveMatchCenterScreen(
    matches: List<Match>,
    selectedMatch: Match?,
    onSelectMatch: (Match) -> Unit,
    onUpdateScore: (String, Boolean, Int) -> Unit,
    onCompleteMatch: (String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var filterTab by remember { mutableIntStateOf(0) } // 0: All Live, 1: Completed, 2: All
    val filteredMatches = matches.filter {
        when (filterTab) {
            0 -> it.status == MatchStatus.LIVE
            1 -> it.status == MatchStatus.COMPLETED
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("live_match_center_screen"),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PulsingLiveIndicator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MATCH CENTER",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "Real-time dynamic scoreboards & instant telemetry",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Filter Tabs
                TabRow(
                    selectedTabIndex = filterTab,
                    containerColor = Color(0xFF131D2E),
                    contentColor = SportsCyan,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[filterTab]),
                            color = SportsCyan
                        )
                    }
                ) {
                    Tab(
                        selected = filterTab == 0,
                        onClick = { filterTab = 0 },
                        text = { Text("🔴 Live Now (${matches.count { it.status == MatchStatus.LIVE }})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = filterTab == 1,
                        onClick = { filterTab = 1 },
                        text = { Text("Final / Completed", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = filterTab == 2,
                        onClick = { filterTab = 2 },
                        text = { Text("All Matches", fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }

        // Active Detailed Match Scoreboard (if a match is selected or defaulted)
        val activeMatch = selectedMatch ?: matches.firstOrNull()
        if (activeMatch != null) {
            item {
                DetailedScoreboardCard(
                    match = activeMatch,
                    onUpdateScore = onUpdateScore,
                    onCompleteMatch = onCompleteMatch
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Other matches list header
        item {
            Text(
                text = "MORE MATCHES",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        items(filteredMatches) { match ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                LiveMatchCard(
                    match = match,
                    onMatchClick = { onSelectMatch(match) }
                )
            }
        }
    }
}

@Composable
fun DetailedScoreboardCard(
    match: Match,
    onUpdateScore: (String, Boolean, Int) -> Unit,
    onCompleteMatch: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .testTag("detailed_scoreboard_${match.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF10192A)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SportsCyan.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Tournament & Venue Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = match.tournamentName.uppercase(),
                        color = SportsCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = match.venue,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                if (match.status == MatchStatus.LIVE) {
                    PulsingLiveIndicator()
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SportsGold.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("FINAL", color = SportsGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Large Animated Score Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team A
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = match.teamA.logoEmoji, fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = match.teamA.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    if (match.teamA.secondaryScore.isNotEmpty()) {
                        Text(match.teamA.secondaryScore, color = SportsCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Middle Score Numbers
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${match.teamA.score}",
                            color = SportsCyan,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = " : ",
                            color = Color(0xFF64748B),
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${match.teamB.score}",
                            color = SportsCyan,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = match.timeOrStage,
                        color = if (match.status == MatchStatus.LIVE) Color(0xFFFF4D4D) else SportsEmerald,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Team B
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = match.teamB.logoEmoji, fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = match.teamB.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    if (match.teamB.secondaryScore.isNotEmpty()) {
                        Text(match.teamB.secondaryScore, color = SportsCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Live Score mutation buttons (if match is live)
            if (match.status == MatchStatus.LIVE) {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Team A score adjustment
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onUpdateScore(match.id, true, -1) },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .testTag("score_minus_team_a")
                        ) {
                            Icon(Icons.Default.Remove, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = { onUpdateScore(match.id, true, 1) },
                            colors = ButtonDefaults.buttonColors(containerColor = SportsCyan, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("score_plus_team_a")
                        ) {
                            Text("+ ${match.teamA.shortCode}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Complete Match Button
                    Button(
                        onClick = {
                            val winnerId = if (match.teamA.score >= match.teamB.score) match.teamA.teamId else match.teamB.teamId
                            onCompleteMatch(match.id, winnerId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SportsGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("complete_match_button")
                    ) {
                        Text("Finish 🏆", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Team B score adjustment
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { onUpdateScore(match.id, false, 1) },
                            colors = ButtonDefaults.buttonColors(containerColor = SportsCyan, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("score_plus_team_b")
                        ) {
                            Text("+ ${match.teamB.shortCode}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { onUpdateScore(match.id, false, -1) },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .testTag("score_minus_team_b")
                        ) {
                            Icon(Icons.Default.Remove, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Match Telemetry Stats
            Text(
                text = "MATCH STATISTICS",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Possession bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${match.stats.possessionA}%", color = SportsCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Possession", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text("${match.stats.possessionB}%", color = SportsEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { match.stats.possessionA / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SportsCyan,
                    trackColor = SportsEmerald,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stat Rows: Shots, Fouls, Cards
            StatComparisonRow(label = "Shots / Kills", valA = "${match.stats.shotsA}", valB = "${match.stats.shotsB}")
            StatComparisonRow(label = "Fouls", valA = "${match.stats.foulsA}", valB = "${match.stats.foulsB}")
            StatComparisonRow(label = "Yellow Cards", valA = "${match.stats.yellowCardsA}", valB = "${match.stats.yellowCardsB}")

            // Live Events Timeline
            if (match.events.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "MATCH TIMELINE",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                match.events.take(4).forEach { ev ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ev.minute,
                            color = SportsCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(44.dp)
                        )
                        Text(text = ev.icon, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = ev.description,
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatComparisonRow(label: String, valA: String, valB: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(valA, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(36.dp), textAlign = TextAlign.Start)
        Text(label, color = Color(0xFF94A3B8), fontSize = 11.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(valB, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(36.dp), textAlign = TextAlign.End)
    }
}
