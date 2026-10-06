package com.example.data

enum class SportCategory(val label: String) {
    ALL("All Sports"),
    BALL("Ball Sports"),
    RACQUET("Racquet & Court"),
    COMBAT_STRATEGY("Combat & Strategy"),
    ATHLETICS_AQUATIC("Athletics & Water"),
    ESPORTS("Esports")
}

data class Sport(
    val id: String,
    val name: String,
    val icon: String,
    val tournamentsCount: Int,
    val teamsCount: Int,
    val category: SportCategory,
    val accentHex: Long,
    val description: String = ""
)

enum class TournamentStatus(val label: String) {
    REGISTRATION_OPEN("REGISTRATION OPEN"),
    ONGOING("LIVE & ONGOING"),
    COMPLETED("COMPLETED"),
    UPCOMING("UPCOMING")
}

data class Tournament(
    val id: String,
    val name: String,
    val sport: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val teamsCount: Int,
    val status: TournamentStatus,
    val prizePool: String,
    val format: String = "Knockout & League",
    val description: String = "",
    val championTeam: String? = null
)

data class Team(
    val id: String,
    val name: String,
    val shortCode: String,
    val sport: String,
    val logoEmoji: String,
    val coach: String,
    val captain: String,
    val matchesPlayed: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val points: Int,
    val city: String,
    val recentForm: List<String> = listOf("W", "W", "L", "W", "D")
)

data class Player(
    val id: String,
    val name: String,
    val teamId: String,
    val teamName: String,
    val sport: String,
    val position: String,
    val jerseyNumber: Int,
    val age: Int,
    val country: String,
    val matches: Int,
    val wins: Int,
    val goalsOrPoints: Int,
    val assists: Int,
    val performanceRating: Float, // e.g. 9.4f
    val avatarEmoji: String
)

enum class MatchStatus {
    LIVE,
    UPCOMING,
    COMPLETED
}

data class TeamMatchInfo(
    val teamId: String,
    val name: String,
    val shortCode: String,
    val score: Int,
    val secondaryScore: String = "",
    val logoEmoji: String
)

data class MatchDetailedStats(
    val possessionA: Int = 50,
    val possessionB: Int = 50,
    val shotsA: Int = 0,
    val shotsB: Int = 0,
    val foulsA: Int = 0,
    val foulsB: Int = 0,
    val yellowCardsA: Int = 0,
    val yellowCardsB: Int = 0,
    val redCardsA: Int = 0,
    val redCardsB: Int = 0
)

data class MatchEvent(
    val id: String,
    val minute: String,
    val description: String,
    val teamIndex: Int, // 0 for A, 1 for B
    val icon: String
)

data class Match(
    val id: String,
    val tournamentId: String,
    val tournamentName: String,
    val sport: String,
    val teamA: TeamMatchInfo,
    val teamB: TeamMatchInfo,
    val status: MatchStatus,
    val timeOrStage: String,
    val venue: String,
    val stats: MatchDetailedStats = MatchDetailedStats(),
    val events: List<MatchEvent> = emptyList(),
    val winnerTeamId: String? = null
)

data class PointsTableRow(
    val position: Int,
    val teamName: String,
    val shortCode: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val points: Int
)

data class BracketMatch(
    val id: String,
    val teamA: String,
    val scoreA: Int,
    val teamB: String,
    val scoreB: Int,
    val winner: String? = null,
    val stage: String
)

enum class NotificationType {
    LIVE,
    TROPHY,
    GOAL,
    SCHEDULE,
    PLAYER
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

data class Official(
    val id: String,
    val name: String,
    val role: String,
    val sport: String,
    val experienceYears: Int,
    val matchesOfficiated: Int,
    val badgeLevel: String
)

data class Venue(
    val id: String,
    val name: String,
    val city: String,
    val capacity: String,
    val sportSupported: List<String>,
    val address: String,
    val hasFloodlights: Boolean = true
)
