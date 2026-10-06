package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PointsTableRow
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold

@Composable
fun PointsTableScreenView(
    pointsTable: List<PointsTableRow>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("points_table_view")
    ) {
        // Table Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF162032))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("POS", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(34.dp))
                Text("TEAM", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("P", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("W", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("D", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("L", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("GD", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
                Text("PTS", color = SportsCyan, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(32.dp), textAlign = TextAlign.End)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Table Rows
        pointsTable.forEach { row ->
            val isGold = row.position == 1
            val isSilver = row.position == 2
            val isBronze = row.position == 3

            val rowBorder = when {
                isGold -> androidx.compose.foundation.BorderStroke(1.2.dp, SportsGold.copy(alpha = 0.6f))
                isSilver -> androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC0C0C0).copy(alpha = 0.4f))
                isBronze -> androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCD7F32).copy(alpha = 0.4f))
                else -> null
            }

            val posBadgeColor = when {
                isGold -> SportsGold
                isSilver -> Color(0xFFCBD5E1)
                isBronze -> Color(0xFFD97706)
                else -> Color(0xFF64748B)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .testTag("standing_row_${row.position}"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isGold -> Color(0xFF1E2838)
                        isSilver -> Color(0xFF162132)
                        isBronze -> Color(0xFF141D2B)
                        else -> Color(0xFF0F1724)
                    }
                ),
                border = rowBorder
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position badge
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(posBadgeColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (row.position) {
                                1 -> "🥇"
                                2 -> "🥈"
                                3 -> "🥉"
                                else -> "${row.position}"
                            },
                            color = posBadgeColor,
                            fontSize = if (row.position <= 3) 12.sp else 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Team Name
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = row.teamName,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = if (row.position <= 3) FontWeight.Bold else FontWeight.Medium
                        )
                    }

                    Text("${row.played}", color = Color(0xFFCBD5E1), fontSize = 12.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text("${row.won}", color = SportsEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text("${row.drawn}", color = Color(0xFF94A3B8), fontSize = 12.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text("${row.lost}", color = Color(0xFFFF5252), fontSize = 12.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text(
                        text = if (row.goalDifference > 0) "+${row.goalDifference}" else "${row.goalDifference}",
                        color = if (row.goalDifference >= 0) Color(0xFF94A3B8) else Color(0xFFFF8A80),
                        fontSize = 11.sp,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${row.points}",
                        color = if (row.position == 1) SportsGold else SportsCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
