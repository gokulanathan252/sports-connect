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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.Player
import com.example.data.Team
import com.example.ui.components.StatCounterCard
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun TeamProfileScreen(
    team: Team,
    players: List<Player>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val teamPlayers = players.filter { it.teamId == team.id }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("team_profile_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Back Button
        item {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF162132))
                    .testTag("team_back_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E)),
                border = androidx.compose.foundation.BorderStroke(1.dp, SportsCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SportsCyan.copy(alpha = 0.15f))
                            .border(2.dp, SportsCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(team.logoEmoji, fontSize = 42.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(team.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("${team.city} • ${team.sport}", color = SportsCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Coach: ${team.coach}", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                        Text("  •  ", color = Color(0xFF64748B), fontSize = 12.sp)
                        Text("Captain: ${team.captain}", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Form badges
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Recent Form: ", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        team.recentForm.forEach { result ->
                            val color = when (result) {
                                "W" -> SportsEmerald
                                "D" -> SportsGold
                                else -> Color(0xFFFF4D4D)
                            }
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(result, color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }

        // Stats Overview
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text("CLUB RECORD", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCounterCard("Played", team.matchesPlayed, "", "🛡️", SportsCyan, Modifier.weight(1f))
                StatCounterCard("Wins", team.wins, "", "🏆", SportsEmerald, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCounterCard("Losses", team.losses, "", "❌", Color(0xFFFF5252), Modifier.weight(1f))
                StatCounterCard("Total Points", team.points, " pts", "⭐", SportsGold, Modifier.weight(1f))
            }
        }

        // Squad list
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text("REGISTERED SQUAD (${if (teamPlayers.isNotEmpty()) teamPlayers.size else "Key Players"})", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        val displayPlayers = if (teamPlayers.isNotEmpty()) teamPlayers else players.take(3)
        displayPlayers.forEach { pl ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(pl.avatarEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(pl.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("#${pl.jerseyNumber} • ${pl.position}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        }
                        Text("Rating: ${pl.performanceRating}", color = SportsGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
