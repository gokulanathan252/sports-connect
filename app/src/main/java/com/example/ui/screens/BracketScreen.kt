package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.BracketMatch
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun BracketVisualizationView(
    bracketMatches: List<BracketMatch>,
    onAdvanceBracket: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val qfMatches = bracketMatches.filter { it.stage == "Quarter Finals" }
    val sfMatches = bracketMatches.filter { it.stage == "Semi Finals" }
    val finalMatch = bracketMatches.find { it.stage == "FINAL" }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bracket_visualization_view")
    ) {
        // Explanatory note
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SportsCyan.copy(alpha = 0.1f))
                .border(1.dp, SportsCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡 ", fontSize = 16.sp)
                Text(
                    text = "Tap any advancing team to simulate victory progression into the next round!",
                    color = SportsCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // STAGE 1: QUARTER FINALS
        BracketStageHeader(title = "QUARTER FINALS", count = "4 Matches", color = SportsCyan)
        Spacer(modifier = Modifier.height(10.dp))
        qfMatches.forEach { match ->
            BracketMatchCard(
                match = match,
                onSelectWinner = { team -> onAdvanceBracket(match.id, team) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Connecting Indicator
        StageConnector()

        // STAGE 2: SEMI FINALS
        BracketStageHeader(title = "SEMI FINALS", count = "2 Matches", color = SportsEmerald)
        Spacer(modifier = Modifier.height(10.dp))
        sfMatches.forEach { match ->
            BracketMatchCard(
                match = match,
                onSelectWinner = { team -> onAdvanceBracket(match.id, team) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Connecting Indicator
        StageConnector()

        // STAGE 3: THE GRAND FINAL
        BracketStageHeader(title = "🏆 THE GRAND FINAL", count = "Championship Match", color = SportsGold)
        Spacer(modifier = Modifier.height(10.dp))
        if (finalMatch != null) {
            BracketMatchCard(
                match = finalMatch,
                isFinal = true,
                onSelectWinner = { team -> onAdvanceBracket(finalMatch.id, team) }
            )
        }

        // Champion Banner
        finalMatch?.winner?.let { champion ->
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bracket_champion_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B283E)),
                border = androidx.compose.foundation.BorderStroke(2.dp, SportsGold)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "👑 TOURNAMENT CHAMPION", color = SportsGold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = champion, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("GOLD MEDAL & TROPHY WINNERS", color = SportsEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BracketStageHeader(title: String, count: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Text(
            text = count,
            color = Color(0xFF94A3B8),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun StageConnector() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(18.dp)
                .background(SportsCyan.copy(alpha = 0.5f))
        )
        Text("↓", color = SportsCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BracketMatchCard(
    match: BracketMatch,
    isFinal: Boolean = false,
    onSelectWinner: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bracket_card_${match.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFinal) Color(0xFF162338) else Color(0xFF111A29)
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isFinal) 1.5.dp else 1.dp,
            if (isFinal) SportsGold else Color.White.copy(alpha = 0.12f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Team A
            BracketTeamRow(
                teamName = match.teamA,
                score = match.scoreA,
                isWinner = match.winner == match.teamA,
                onClick = { onSelectWinner(match.teamA) }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Team B
            BracketTeamRow(
                teamName = match.teamB,
                score = match.scoreB,
                isWinner = match.winner == match.teamB,
                onClick = { onSelectWinner(match.teamB) }
            )
        }
    }
}

@Composable
private fun BracketTeamRow(
    teamName: String,
    score: Int,
    isWinner: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isWinner) SportsEmerald.copy(alpha = 0.15f) else Color(0xFF0D1420))
            .border(
                1.dp,
                if (isWinner) SportsEmerald.copy(alpha = 0.5f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isWinner) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Winner",
                    tint = SportsEmerald,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = teamName,
                color = if (isWinner) Color.White else Color(0xFFCBD5E1),
                fontSize = 13.sp,
                fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Medium
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isWinner) SportsEmerald else Color(0xFF1E293B))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$score",
                color = if (isWinner) Color.Black else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
