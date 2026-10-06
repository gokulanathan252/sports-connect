package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.Match
import com.example.data.MatchStatus
import com.example.data.NotificationItem
import com.example.data.NotificationType
import com.example.data.Player
import com.example.data.Team
import com.example.data.TeamMatchInfo
import com.example.data.Tournament
import com.example.data.TournamentStatus
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun AddTournamentDialog(
    onDismiss: () -> Unit,
    onConfirm: (Tournament) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("Football") }
    var location by remember { mutableStateOf("Chennai, Tamil Nadu") }
    var prizePool by remember { mutableStateOf("₹ 10,00,000") }
    var teamsCount by remember { mutableStateOf("16") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141D2C)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SportsCyan)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("CREATE NEW TOURNAMENT", color = SportsCyan, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(14.dp))

                SportsDialogInput(label = "Tournament Name", value = name, onValueChange = { name = it }, placeholder = "e.g. State Premier Cup")
                SportsDialogInput(label = "Sport", value = sport, onValueChange = { sport = it }, placeholder = "Football, Cricket, etc.")
                SportsDialogInput(label = "Location / Venue City", value = location, onValueChange = { location = it }, placeholder = "City, Stadium")
                SportsDialogInput(label = "Prize Pool", value = prizePool, onValueChange = { prizePool = it }, placeholder = "₹ 5,00,000")
                SportsDialogInput(label = "Teams Count", value = teamsCount, onValueChange = { teamsCount = it }, placeholder = "16")

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(
                                    Tournament(
                                        id = "tour_${System.currentTimeMillis()}",
                                        name = name,
                                        sport = sport,
                                        location = location,
                                        startDate = "Nov 01",
                                        endDate = "Nov 20",
                                        teamsCount = teamsCount.toIntOrNull() ?: 16,
                                        status = TournamentStatus.REGISTRATION_OPEN,
                                        prizePool = prizePool,
                                        description = "Official tournament sanctioned under SportsConnect regional division."
                                    )
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SportsCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("submit_tournament_button")
                    ) {
                        Text("Create Tournament", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddTeamDialog(
    onDismiss: () -> Unit,
    onConfirm: (Team) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var shortCode by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("Football") }
    var coach by remember { mutableStateOf("") }
    var captain by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Chennai") }
    var emoji by remember { mutableStateOf("⚡") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141D2C)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SportsEmerald)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("REGISTER NEW TEAM", color = SportsEmerald, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(14.dp))

                SportsDialogInput(label = "Team Name", value = name, onValueChange = { name = it }, placeholder = "e.g. Coimbatore Titans")
                SportsDialogInput(label = "Short Code (3 letters)", value = shortCode, onValueChange = { shortCode = it.take(3).uppercase() }, placeholder = "CBT")
                SportsDialogInput(label = "Sport", value = sport, onValueChange = { sport = it }, placeholder = "Football, Cricket, etc.")
                SportsDialogInput(label = "Coach Name", value = coach, onValueChange = { coach = it }, placeholder = "Coach Name")
                SportsDialogInput(label = "Captain Name", value = captain, onValueChange = { captain = it }, placeholder = "Captain Name")
                SportsDialogInput(label = "City", value = city, onValueChange = { city = it }, placeholder = "City")

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(
                                    Team(
                                        id = "team_${System.currentTimeMillis()}",
                                        name = name,
                                        shortCode = if (shortCode.isNotBlank()) shortCode else name.take(3).uppercase(),
                                        sport = sport,
                                        logoEmoji = emoji,
                                        coach = if (coach.isNotBlank()) coach else "Head Coach",
                                        captain = if (captain.isNotBlank()) captain else "Captain",
                                        matchesPlayed = 0,
                                        wins = 0,
                                        draws = 0,
                                        losses = 0,
                                        points = 0,
                                        city = city
                                    )
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SportsEmerald, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("submit_team_button")
                    ) {
                        Text("Register Team", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddPlayerDialog(
    teams: List<Team>,
    onDismiss: () -> Unit,
    onConfirm: (Player) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf(teams.firstOrNull()?.name ?: "Chennai FC") }
    var jersey by remember { mutableStateOf("10") }
    var position by remember { mutableStateOf("Forward") }
    var age by remember { mutableStateOf("22") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141D2C)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SportsGold)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("ENROLL NEW PLAYER", color = SportsGold, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(14.dp))

                SportsDialogInput(label = "Player Full Name", value = name, onValueChange = { name = it }, placeholder = "Athlete Name")
                SportsDialogInput(label = "Team Name", value = selectedTeam, onValueChange = { selectedTeam = it }, placeholder = "Team")
                SportsDialogInput(label = "Jersey Number", value = jersey, onValueChange = { jersey = it }, placeholder = "7, 10, etc.")
                SportsDialogInput(label = "Position", value = position, onValueChange = { position = it }, placeholder = "Striker, Defender, All-Rounder")
                SportsDialogInput(label = "Age", value = age, onValueChange = { age = it }, placeholder = "Age")

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(
                                    Player(
                                        id = "p_${System.currentTimeMillis()}",
                                        name = name,
                                        teamId = teams.find { it.name == selectedTeam }?.id ?: "team_1",
                                        teamName = selectedTeam,
                                        sport = "Sports",
                                        position = position,
                                        jerseyNumber = jersey.toIntOrNull() ?: 10,
                                        age = age.toIntOrNull() ?: 22,
                                        country = "India",
                                        matches = 0,
                                        wins = 0,
                                        goalsOrPoints = 0,
                                        assists = 0,
                                        performanceRating = 8.5f,
                                        avatarEmoji = "👟"
                                    )
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SportsGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("submit_player_button")
                    ) {
                        Text("Save Player", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleMatchDialog(
    tournaments: List<Tournament>,
    teams: List<Team>,
    onDismiss: () -> Unit,
    onConfirm: (Match) -> Unit
) {
    var teamAName by remember { mutableStateOf(teams.firstOrNull()?.name ?: "Chennai FC") }
    var teamBName by remember { mutableStateOf(teams.getOrNull(1)?.name ?: "Madurai United") }
    var venue by remember { mutableStateOf("Jawaharlal Nehru Stadium, Chennai") }
    var sport by remember { mutableStateOf("Football") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141D2C)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SportsCyan)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("SCHEDULE MATCH", color = SportsCyan, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(14.dp))

                SportsDialogInput(label = "Sport", value = sport, onValueChange = { sport = it }, placeholder = "Football, Cricket, etc.")
                SportsDialogInput(label = "Team 1", value = teamAName, onValueChange = { teamAName = it }, placeholder = "Team 1")
                SportsDialogInput(label = "Team 2", value = teamBName, onValueChange = { teamBName = it }, placeholder = "Team 2")
                SportsDialogInput(label = "Venue Stadium", value = venue, onValueChange = { venue = it }, placeholder = "Stadium name")

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val tA = teams.find { it.name == teamAName }
                            val tB = teams.find { it.name == teamBName }
                            onConfirm(
                                Match(
                                    id = "match_${System.currentTimeMillis()}",
                                    tournamentId = tournaments.firstOrNull()?.id ?: "tour_1",
                                    tournamentName = tournaments.firstOrNull()?.name ?: "Championship",
                                    sport = sport,
                                    teamA = TeamMatchInfo(tA?.id ?: "t1", teamAName, tA?.shortCode ?: "T1", 0, "", tA?.logoEmoji ?: "🛡️"),
                                    teamB = TeamMatchInfo(tB?.id ?: "t2", teamBName, tB?.shortCode ?: "T2", 0, "", tB?.logoEmoji ?: "⚔️"),
                                    status = MatchStatus.LIVE,
                                    timeOrStage = "LIVE 00:00",
                                    venue = venue
                                )
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SportsCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("submit_schedule_match_button")
                    ) {
                        Text("Schedule & Go Live", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsDialog(
    notifications: List<NotificationItem>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SportsCyan.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("NOTIFICATIONS", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(notifications) { notif ->
                        val icon = when (notif.type) {
                            NotificationType.LIVE -> "🔴"
                            NotificationType.TROPHY -> "🏆"
                            NotificationType.GOAL -> "⚽"
                            NotificationType.SCHEDULE -> "📅"
                            NotificationType.PLAYER -> "👤"
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1726))
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                                Text(icon, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(notif.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(notif.message, color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                    Text(notif.timeAgo, color = Color(0xFF64748B), fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onMarkAllRead()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SportsCyan, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Mark All Read", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SportsDialogInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF475569), fontSize = 12.sp) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SportsCyan,
                unfocusedBorderColor = Color(0xFF243248),
                focusedContainerColor = Color(0xFF0C1320),
                unfocusedContainerColor = Color(0xFF0C1320),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}
