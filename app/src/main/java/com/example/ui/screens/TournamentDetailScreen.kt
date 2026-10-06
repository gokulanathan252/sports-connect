package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BracketMatch
import com.example.data.Match
import com.example.data.Player
import com.example.data.PointsTableRow
import com.example.data.Team
import com.example.data.Tournament
import com.example.ui.components.LiveMatchCard
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun TournamentDetailScreen(
    tournament: Tournament,
    matches: List<Match>,
    teams: List<Team>,
    players: List<Player>,
    pointsTable: List<PointsTableRow>,
    bracketMatches: List<BracketMatch>,
    onBack: () -> Unit,
    onViewMatch: (Match) -> Unit,
    onViewTeam: (Team) -> Unit,
    onViewPlayer: (Player) -> Unit,
    onAdvanceBracket: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Overview", "Fixtures", "Live Matches", "Points Table", "Teams", "Players", "Results", "Statistics")
    var selectedTab by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tournament_detail_screen"),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Banner Header with Back Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sports_stadium_hero),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color(0xFF090D14))
                            )
                        )
                )

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .testTag("tournament_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        // Tournament Title & Key Badges
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SportsCyan.copy(alpha = 0.15f))
                            .border(1.dp, SportsCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tournament.status.label,
                            color = SportsCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SportsGold.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Prize Pool: ${tournament.prizePool}",
                            color = SportsGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = tournament.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Info row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = SportsCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(tournament.location, color = Color(0xFFCBD5E1), fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, null, tint = SportsEmerald, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${tournament.startDate} – ${tournament.endDate}", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, null, tint = SportsGold, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${tournament.teamsCount} Teams", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Scrollable Navigation Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF131D2E),
                    contentColor = SportsCyan,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = SportsCyan
                        )
                    }
                ) {
                    tabs.forEachIndexed { idx, title ->
                        Tab(
                            selected = selectedTab == idx,
                            onClick = { selectedTab = idx },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == idx) SportsCyan else Color(0xFF94A3B8)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tab Content
        item {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_content"
            ) { tabIndex ->
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    when (tabIndex) {
                        0 -> TournamentOverviewTab(tournament = tournament, teams = teams)
                        1 -> BracketVisualizationView(bracketMatches = bracketMatches, onAdvanceBracket = onAdvanceBracket)
                        2 -> TournamentLiveMatchesTab(matches = matches, onViewMatch = onViewMatch)
                        3 -> TournamentPointsTableTab(pointsTable = pointsTable)
                        4 -> TournamentTeamsTab(teams = teams, onViewTeam = onViewTeam)
                        5 -> TournamentPlayersTab(players = players, onViewPlayer = onViewPlayer)
                        6 -> TournamentResultsTab(matches = matches, onViewMatch = onViewMatch)
                        7 -> TournamentStatsTab()
                    }
                }
            }
        }
    }
}

@Composable
private fun TournamentOverviewTab(tournament: Tournament, teams: List<Team>) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ABOUT THE TOURNAMENT",
                    color = SportsCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = tournament.description,
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Format", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        Text(tournament.format, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sport Category", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        Text(tournament.sport, color = SportsEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "PARTICIPATING TEAMS (${teams.size})",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        teams.forEach { team ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF101927))
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(team.logoEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(team.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Coach: ${team.coach} • Captain: ${team.captain}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    }
                    Text("${team.points} pts", color = SportsCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun TournamentLiveMatchesTab(matches: List<Match>, onViewMatch: (Match) -> Unit) {
    Column {
        matches.forEach { match ->
            LiveMatchCard(
                match = match,
                onMatchClick = { onViewMatch(match) },
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun TournamentPointsTableTab(pointsTable: List<PointsTableRow>) {
    PointsTableScreenView(pointsTable = pointsTable)
}

@Composable
private fun TournamentTeamsTab(teams: List<Team>, onViewTeam: (Team) -> Unit) {
    Column {
        teams.forEach { team ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onViewTeam(team) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E))
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(team.logoEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(team.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("${team.city} • Form: ${team.recentForm.joinToString(" ")}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                    }
                    Button(
                        onClick = { onViewTeam(team) },
                        colors = ButtonDefaults.buttonColors(containerColor = SportsCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Profile →", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TournamentPlayersTab(players: List<Player>, onViewPlayer: (Player) -> Unit) {
    Column {
        players.forEach { p ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onViewPlayer(p) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E))
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(p.avatarEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(p.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("#${p.jerseyNumber} • ${p.position} • ${p.teamName}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SportsGold.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("★ ${p.performanceRating}", color = SportsGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun TournamentResultsTab(matches: List<Match>, onViewMatch: (Match) -> Unit) {
    val completed = matches.filter { it.status == com.example.data.MatchStatus.COMPLETED }
    Column {
        if (completed.isEmpty()) {
            Text("No completed matches yet. Check live matches or finish one in Match Center!", color = Color(0xFF94A3B8), fontSize = 13.sp)
        } else {
            completed.forEach { match ->
                LiveMatchCard(
                    match = match,
                    onMatchClick = { onViewMatch(match) },
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun TournamentStatsTab() {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("TOURNAMENT LEADERS", color = SportsGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Top Scorer: K. Raman (24 Goals)", color = Color.White, fontSize = 13.sp)
                    Text("Chennai FC", color = SportsCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Top Playmaker: S. Vignesh (19 Assists)", color = Color.White, fontSize = 13.sp)
                    Text("Madurai United", color = SportsEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Best Defense: Chennai FC (14 conceded)", color = Color.White, fontSize = 13.sp)
                    Text("8 Clean Sheets", color = SportsGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
