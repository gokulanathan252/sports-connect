package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SportsConnectRepository {

    private val _sports = MutableStateFlow<List<Sport>>(initialSports)
    val sports: StateFlow<List<Sport>> = _sports.asStateFlow()

    private val _tournaments = MutableStateFlow<List<Tournament>>(initialTournaments)
    val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

    private val _teams = MutableStateFlow<List<Team>>(initialTeams)
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(initialPlayers)
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _matches = MutableStateFlow<List<Match>>(initialMatches)
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _pointsTable = MutableStateFlow<List<PointsTableRow>>(initialPointsTable)
    val pointsTable: StateFlow<List<PointsTableRow>> = _pointsTable.asStateFlow()

    private val _bracketMatches = MutableStateFlow<List<BracketMatch>>(initialBracketMatches)
    val bracketMatches: StateFlow<List<BracketMatch>> = _bracketMatches.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _officials = MutableStateFlow<List<Official>>(initialOfficials)
    val officials: StateFlow<List<Official>> = _officials.asStateFlow()

    private val _venues = MutableStateFlow<List<Venue>>(initialVenues)
    val venues: StateFlow<List<Venue>> = _venues.asStateFlow()

    fun updateScore(matchId: String, isTeamA: Boolean, delta: Int) {
        _matches.update { list ->
            list.map { match ->
                if (match.id == matchId) {
                    val updatedA = if (isTeamA) {
                        val newScore = (match.teamA.score + delta).coerceAtLeast(0)
                        match.teamA.copy(score = newScore)
                    } else match.teamA

                    val updatedB = if (!isTeamA) {
                        val newScore = (match.teamB.score + delta).coerceAtLeast(0)
                        match.teamB.copy(score = newScore)
                    } else match.teamB

                    val scoringTeamName = if (isTeamA) match.teamA.name else match.teamB.name
                    val newEvent = MatchEvent(
                        id = "evt_${System.currentTimeMillis()}",
                        minute = if (match.sport == "Football") "${(60..90).random()}'" else "Live",
                        description = if (delta > 0) "Score point added for $scoringTeamName" else "Score adjustment for $scoringTeamName",
                        teamIndex = if (isTeamA) 0 else 1,
                        icon = if (delta > 0) "⚡" else "🔄"
                    )

                    match.copy(
                        teamA = updatedA,
                        teamB = updatedB,
                        events = listOf(newEvent) + match.events
                    )
                } else match
            }
        }
    }

    fun completeMatch(matchId: String, winnerTeamId: String) {
        _matches.update { list ->
            list.map { match ->
                if (match.id == matchId) {
                    match.copy(
                        status = MatchStatus.COMPLETED,
                        timeOrStage = "FINAL",
                        winnerTeamId = winnerTeamId
                    )
                } else match
            }
        }

        val completedMatch = _matches.value.find { it.id == matchId }
        val winnerName = if (completedMatch?.teamA?.teamId == winnerTeamId) completedMatch.teamA.name else completedMatch?.teamB?.name ?: "Champion"

        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                title = "🏆 Match Completed",
                message = "$winnerName won against ${if (completedMatch?.teamA?.teamId == winnerTeamId) completedMatch.teamB.name else completedMatch?.teamA?.name}!",
                timeAgo = "Just now",
                type = NotificationType.TROPHY
            )
        )
    }

    fun advanceBracket(bracketId: String, winnerTeam: String) {
        _bracketMatches.update { list ->
            list.map { bm ->
                if (bm.id == bracketId) {
                    bm.copy(winner = winnerTeam)
                } else bm
            }
        }
    }

    fun addTournament(tournament: Tournament) {
        _tournaments.update { listOf(tournament) + it }
        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                title = "📅 Tournament Created",
                message = "${tournament.name} (${tournament.sport}) was scheduled.",
                timeAgo = "Just now",
                type = NotificationType.SCHEDULE
            )
        )
    }

    fun addTeam(team: Team) {
        _teams.update { listOf(team) + it }
    }

    fun addPlayer(player: Player) {
        _players.update { listOf(player) + it }
        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                title = "👤 New Player Registered",
                message = "${player.name} joined ${player.teamName} for ${player.sport}.",
                timeAgo = "Just now",
                type = NotificationType.PLAYER
            )
        )
    }

    fun scheduleMatch(match: Match) {
        _matches.update { listOf(match) + it }
        addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                title = "📅 Match Scheduled",
                message = "${match.teamA.name} vs ${match.teamB.name} scheduled at ${match.venue}.",
                timeAgo = "Just now",
                type = NotificationType.SCHEDULE
            )
        )
    }

    fun addNotification(item: NotificationItem) {
        _notifications.update { listOf(item) + it }
    }

    fun markAllNotificationsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }

    companion object {
        val initialSports = listOf(
            Sport("football", "Football", "⚽", 32, 120, SportCategory.BALL, 0xFF00E676, "Global standard 11v11 & futsal championship tournaments."),
            Sport("cricket", "Cricket", "🏏", 24, 88, SportCategory.BALL, 0xFFFF9100, "T20, One Day & Premier Super League divisions."),
            Sport("basketball", "Basketball", "🏀", 18, 64, SportCategory.BALL, 0xFFFF5722, "Full-court and 3x3 high-flying hoops championships."),
            Sport("hockey", "Hockey", "🏑", 14, 42, SportCategory.BALL, 0xFF00B0FF, "Field hockey synthetic turf championship circuits."),
            Sport("volleyball", "Volleyball", "🏐", 16, 50, SportCategory.BALL, 0xFFFFEA00, "Indoor six-a-side and pro beach spike leagues."),
            Sport("tennis", "Tennis", "🎾", 12, 36, SportCategory.RACQUET, 0xFF76FF03, "Hard-court, clay, and grass knockout brackets."),
            Sport("badminton", "Badminton", "🏸", 20, 75, SportCategory.RACQUET, 0xFF00E5FF, "Singles & doubles international shuttle tournaments."),
            Sport("table_tennis", "Table Tennis", "🏓", 15, 60, SportCategory.RACQUET, 0xFFFF3D00, "Fast-paced spin masters and inter-club trophies."),
            Sport("kabaddi", "Kabaddi", "🤼", 10, 32, SportCategory.COMBAT_STRATEGY, 0xFFFF6D00, "High-intensity raids, super tackles and pro league mat."),
            Sport("kho_kho", "Kho-Kho", "🏃‍♂️", 8, 28, SportCategory.ATHLETICS_AQUATIC, 0xFFFFD600, "Traditional tag sport with lightning-quick chases."),
            Sport("handball", "Handball", "🤾", 9, 30, SportCategory.BALL, 0xFF00BFA5, "Dynamic 7-a-side Olympic handball leagues."),
            Sport("baseball", "Baseball", "⚾", 6, 24, SportCategory.BALL, 0xFFE040FB, "Diamond diamond derbies and knockout series."),
            Sport("rugby", "Rugby", "🏉", 8, 22, SportCategory.BALL, 0xFFFF1744, "Full-contact rugby sevens and union tournaments."),
            Sport("athletics", "Athletics", "🏃", 25, 110, SportCategory.ATHLETICS_AQUATIC, 0xFFFFAB00, "Track & field sprints, relays, hurdles and long jumps."),
            Sport("swimming", "Swimming", "🏊", 14, 45, SportCategory.ATHLETICS_AQUATIC, 0xFF00E5FF, "Olympic 50m lap freestyle, backstroke and medley races."),
            Sport("boxing", "Boxing", "🥊", 11, 38, SportCategory.COMBAT_STRATEGY, 0xFFFF5252, "Weight-class bouts, golden glove belts and rings."),
            Sport("wrestling", "Wrestling", "🥋", 12, 40, SportCategory.COMBAT_STRATEGY, 0xFFFF3D00, "Freestyle and Greco-Roman gold medal tournaments."),
            Sport("chess", "Chess", "♟", 22, 90, SportCategory.COMBAT_STRATEGY, 0xFFB388FF, "FIDE rated blitz, rapid, and classical grandmaster open."),
            Sport("carrom", "Carrom", "⚪", 7, 30, SportCategory.COMBAT_STRATEGY, 0xFF8D6E63, "Striker accuracy and queen pocketing master trophies."),
            Sport("esports", "Esports", "🎮", 19, 85, SportCategory.ESPORTS, 0xFF651FFF, "Valorant, MOBA, Battle Royale and tactical arena cups.")
        )

        val initialTournaments = listOf(
            Tournament(
                id = "tour_1",
                name = "South India Football Championship",
                sport = "Football",
                location = "Chennai, Tamil Nadu",
                startDate = "Oct 20",
                endDate = "Nov 10",
                teamsCount = 32,
                status = TournamentStatus.REGISTRATION_OPEN,
                prizePool = "₹ 15,00,000",
                format = "Knockout + League",
                description = "Premier tier South India football open featuring 32 professional clubs vying for the championship trophy."
            ),
            Tournament(
                id = "tour_2",
                name = "All-India T20 Premier Cup",
                sport = "Cricket",
                location = "Bengaluru, Karnataka",
                startDate = "Oct 15",
                endDate = "Nov 02",
                teamsCount = 16,
                status = TournamentStatus.ONGOING,
                prizePool = "₹ 25,00,000",
                format = "Group Stage & Super 4s",
                description = "High octane T20 tournament bringing together regional top franchises and powerhouse hitters."
            ),
            Tournament(
                id = "tour_3",
                name = "National Volleyball Super League",
                sport = "Volleyball",
                location = "Kochi, Kerala",
                startDate = "Oct 05",
                endDate = "Oct 28",
                teamsCount = 12,
                status = TournamentStatus.ONGOING,
                prizePool = "₹ 8,00,000",
                format = "Round Robin + Finals",
                description = "Top tier spike showdown with the finest spikers and setters competing in a 5-set league format."
            ),
            Tournament(
                id = "tour_4",
                name = "Apex Esports Championship",
                sport = "Esports",
                location = "Hyderabad Arena",
                startDate = "Nov 12",
                endDate = "Nov 16",
                teamsCount = 24,
                status = TournamentStatus.UPCOMING,
                prizePool = "₹ 10,00,000",
                format = "Double Elimination",
                description = "Tactical arena FPS championship streamed live across multiple digital platforms."
            ),
            Tournament(
                id = "tour_5",
                name = "Grand Slam Badminton Open",
                sport = "Badminton",
                location = "New Delhi Stadium",
                startDate = "Sep 10",
                endDate = "Sep 25",
                teamsCount = 16,
                status = TournamentStatus.COMPLETED,
                prizePool = "₹ 5,00,000",
                format = "Knockout",
                description = "Super 500 graded singles and doubles tournament showcasing national stars.",
                championTeam = "Delhi Smashers"
            )
        )

        val initialTeams = listOf(
            Team(
                id = "team_chennai_fc",
                name = "Chennai FC",
                shortCode = "CFC",
                sport = "Football",
                logoEmoji = "🦁",
                coach = "Marco Silva",
                captain = "K. Raman",
                matchesPlayed = 14,
                wins = 10,
                draws = 2,
                losses = 2,
                points = 32,
                city = "Chennai",
                recentForm = listOf("W", "W", "D", "W", "W")
            ),
            Team(
                id = "team_madurai_utd",
                name = "Madurai United",
                shortCode = "MDU",
                sport = "Football",
                logoEmoji = "🐂",
                coach = "Arun Kumar",
                captain = "S. Vignesh",
                matchesPlayed = 14,
                wins = 8,
                draws = 3,
                losses = 3,
                points = 27,
                city = "Madurai",
                recentForm = listOf("W", "L", "W", "W", "D")
            ),
            Team(
                id = "team_chennai_warriors",
                name = "Chennai Warriors",
                shortCode = "CW",
                sport = "Cricket",
                logoEmoji = "⚔️",
                coach = "R. Chandran",
                captain = "Ashwin Raj",
                matchesPlayed = 10,
                wins = 8,
                draws = 0,
                losses = 2,
                points = 16,
                city = "Chennai",
                recentForm = listOf("W", "W", "W", "L", "W")
            ),
            Team(
                id = "team_coimbatore_kings",
                name = "Coimbatore Kings",
                shortCode = "CK",
                sport = "Cricket",
                logoEmoji = "👑",
                coach = "G. Nathan",
                captain = "M. Karthik",
                matchesPlayed = 10,
                wins = 6,
                draws = 0,
                losses = 4,
                points = 12,
                city = "Coimbatore",
                recentForm = listOf("L", "W", "W", "L", "W")
            ),
            Team(
                id = "team_alpha",
                name = "Team Alpha",
                shortCode = "ALP",
                sport = "Volleyball",
                logoEmoji = "⚡",
                coach = "David John",
                captain = "Rahul Nair",
                matchesPlayed = 8,
                wins = 6,
                draws = 0,
                losses = 2,
                points = 18,
                city = "Kochi",
                recentForm = listOf("W", "W", "L", "W", "W")
            ),
            Team(
                id = "team_thunder",
                name = "Team Thunder",
                shortCode = "THU",
                sport = "Volleyball",
                logoEmoji = "🌩️",
                coach = "S. George",
                captain = "Jithin Paul",
                matchesPlayed = 8,
                wins = 5,
                draws = 0,
                losses = 3,
                points = 15,
                city = "Trivandrum",
                recentForm = listOf("W", "L", "W", "W", "L")
            ),
            Team(
                id = "team_bengaluru_bulls",
                name = "Bengaluru Titans",
                shortCode = "BLR",
                sport = "Football",
                logoEmoji = "⚡",
                coach = "Trevor Morgan",
                captain = "Sunil C.",
                matchesPlayed = 14,
                wins = 8,
                draws = 2,
                losses = 4,
                points = 26,
                city = "Bengaluru",
                recentForm = listOf("D", "W", "W", "L", "W")
            ),
            Team(
                id = "team_kerala_blasters",
                name = "Malabar Strikers",
                shortCode = "MLS",
                sport = "Football",
                logoEmoji = "🐘",
                coach = "Ivan Vuk",
                captain = "Adrian L.",
                matchesPlayed = 14,
                wins = 7,
                draws = 3,
                losses = 4,
                points = 24,
                city = "Kozhikode",
                recentForm = listOf("L", "W", "D", "W", "W")
            )
        )

        val initialPlayers = listOf(
            Player(
                id = "p_1",
                name = "K. Raman",
                teamId = "team_chennai_fc",
                teamName = "Chennai FC",
                sport = "Football",
                position = "Striker / Forward",
                jerseyNumber = 10,
                age = 24,
                country = "India",
                matches = 28,
                wins = 22,
                goalsOrPoints = 24,
                assists = 14,
                performanceRating = 9.4f,
                avatarEmoji = "⚽"
            ),
            Player(
                id = "p_2",
                name = "S. Vignesh",
                teamId = "team_madurai_utd",
                teamName = "Madurai United",
                sport = "Football",
                position = "Attacking Midfielder",
                jerseyNumber = 8,
                age = 23,
                country = "India",
                matches = 26,
                wins = 18,
                goalsOrPoints = 17,
                assists = 19,
                performanceRating = 9.1f,
                avatarEmoji = "🏃"
            ),
            Player(
                id = "p_3",
                name = "Ashwin Raj",
                teamId = "team_chennai_warriors",
                teamName = "Chennai Warriors",
                sport = "Cricket",
                position = "All-Rounder",
                jerseyNumber = 7,
                age = 27,
                country = "India",
                matches = 35,
                wins = 29,
                goalsOrPoints = 840,
                assists = 32, // wickets
                performanceRating = 9.6f,
                avatarEmoji = "🏏"
            ),
            Player(
                id = "p_4",
                name = "Rahul Nair",
                teamId = "team_alpha",
                teamName = "Team Alpha",
                sport = "Volleyball",
                position = "Power Spiker",
                jerseyNumber = 4,
                age = 22,
                country = "India",
                matches = 20,
                wins = 16,
                goalsOrPoints = 310,
                assists = 48,
                performanceRating = 8.9f,
                avatarEmoji = "🏐"
            ),
            Player(
                id = "p_5",
                name = "Sunil C.",
                teamId = "team_bengaluru_bulls",
                teamName = "Bengaluru Titans",
                sport = "Football",
                position = "Center Forward",
                jerseyNumber = 11,
                age = 29,
                country = "India",
                matches = 42,
                wins = 30,
                goalsOrPoints = 34,
                assists = 12,
                performanceRating = 9.3f,
                avatarEmoji = "🔥"
            )
        )

        val initialMatches = listOf(
            Match(
                id = "match_1",
                tournamentId = "tour_1",
                tournamentName = "South India Football Championship",
                sport = "Football",
                teamA = TeamMatchInfo("team_chennai_fc", "Chennai FC", "CFC", 2, "Goals", "🦁"),
                teamB = TeamMatchInfo("team_madurai_utd", "Madurai United", "MDU", 1, "Goals", "🐂"),
                status = MatchStatus.LIVE,
                timeOrStage = "LIVE 72:34",
                venue = "Jawaharlal Nehru Stadium, Chennai",
                stats = MatchDetailedStats(
                    possessionA = 58,
                    possessionB = 42,
                    shotsA = 14,
                    shotsB = 8,
                    foulsA = 7,
                    foulsB = 11,
                    yellowCardsA = 2,
                    yellowCardsB = 3,
                    redCardsA = 0,
                    redCardsB = 0
                ),
                events = listOf(
                    MatchEvent("e1", "68'", "Goal by K. Raman! Right foot curl into top corner", 0, "⚽"),
                    MatchEvent("e2", "54'", "Goal by S. Vignesh (Madurai United)", 1, "⚽"),
                    MatchEvent("e3", "32'", "Goal by K. Raman (Header)", 0, "⚽"),
                    MatchEvent("e4", "22'", "Yellow card to Madurai Defender", 1, "🟨")
                )
            ),
            Match(
                id = "match_2",
                tournamentId = "tour_2",
                tournamentName = "All-India T20 Premier Cup",
                sport = "Cricket",
                teamA = TeamMatchInfo("team_chennai_warriors", "Chennai Warriors", "CW", 156, "4 Wkts", "⚔️"),
                teamB = TeamMatchInfo("team_coimbatore_kings", "Coimbatore Kings", "CK", 0, "12 Overs", "👑"),
                status = MatchStatus.LIVE,
                timeOrStage = "12 Overs",
                venue = "M. Chinnaswamy Stadium, Bengaluru",
                stats = MatchDetailedStats(
                    possessionA = 62,
                    possessionB = 38,
                    shotsA = 18, // Sixes & Fours
                    shotsB = 10,
                    foulsA = 2,
                    foulsB = 4
                ),
                events = listOf(
                    MatchEvent("cr1", "11.4 ov", "SIX! Smashed over long on by Ashwin Raj", 0, "🏏"),
                    MatchEvent("cr2", "9.2 ov", "WICKET! Caught at deep midwicket", 1, "☝️"),
                    MatchEvent("cr3", "6.1 ov", "Fifty up for Chennai Warriors opener", 0, "🎖️")
                )
            ),
            Match(
                id = "match_3",
                tournamentId = "tour_3",
                tournamentName = "National Volleyball Super League",
                sport = "Volleyball",
                teamA = TeamMatchInfo("team_alpha", "Team Alpha", "ALP", 2, "Sets", "⚡"),
                teamB = TeamMatchInfo("team_thunder", "Team Thunder", "THU", 1, "Sets", "🌩️"),
                status = MatchStatus.LIVE,
                timeOrStage = "SET 4: 18 - 15",
                venue = "Regional Indoor Sports Arena, Kochi",
                stats = MatchDetailedStats(
                    possessionA = 55,
                    possessionB = 45,
                    shotsA = 42, // Spike kills
                    shotsB = 36,
                    foulsA = 3,
                    foulsB = 5
                ),
                events = listOf(
                    MatchEvent("vb1", "Set 4", "Monster Block by Rahul Nair (Alpha)", 0, "🛡️"),
                    MatchEvent("vb2", "Set 4", "Ace Serve by Jithin Paul (Thunder)", 1, "⚡"),
                    MatchEvent("vb3", "Set 3", "Alpha takes Set 3 (25 - 22)", 0, "🏆")
                )
            ),
            Match(
                id = "match_4",
                tournamentId = "tour_1",
                tournamentName = "South India Football Championship",
                sport = "Football",
                teamA = TeamMatchInfo("team_bengaluru_bulls", "Bengaluru Titans", "BLR", 3, "Goals", "⚡"),
                teamB = TeamMatchInfo("team_kerala_blasters", "Malabar Strikers", "MLS", 1, "Goals", "🐘"),
                status = MatchStatus.COMPLETED,
                timeOrStage = "FINAL",
                venue = "Kanteerava Stadium, Bengaluru",
                stats = MatchDetailedStats(
                    possessionA = 52,
                    possessionB = 48,
                    shotsA = 11,
                    shotsB = 9,
                    foulsA = 8,
                    foulsB = 9,
                    yellowCardsA = 1,
                    yellowCardsB = 2
                ),
                events = listOf(
                    MatchEvent("fb_fin1", "88'", "Goal sealed by Sunil C.", 0, "⚽"),
                    MatchEvent("fb_fin2", "71'", "Strikers pull one back", 1, "⚽"),
                    MatchEvent("fb_fin3", "24'", "Opener by Titans striker", 0, "⚽")
                ),
                winnerTeamId = "team_bengaluru_bulls"
            )
        )

        val initialPointsTable = listOf(
            PointsTableRow(1, "Chennai FC", "CFC", 14, 10, 2, 2, 34, 14, +20, 32),
            PointsTableRow(2, "Madurai United", "MDU", 14, 8, 3, 3, 29, 16, +13, 27),
            PointsTableRow(3, "Bengaluru Titans", "BLR", 14, 8, 2, 4, 26, 17, +9, 26),
            PointsTableRow(4, "Malabar Strikers", "MLS", 14, 7, 3, 4, 25, 19, +6, 24),
            PointsTableRow(5, "Hyderabad Strikers", "HYD", 14, 6, 4, 4, 22, 20, +2, 22),
            PointsTableRow(6, "Coimbatore Royals", "CBR", 14, 5, 2, 7, 18, 24, -6, 17),
            PointsTableRow(7, "Trichy Warriors", "TRY", 14, 3, 3, 8, 14, 28, -14, 12),
            PointsTableRow(8, "Salem City FC", "SLM", 14, 1, 1, 12, 9, 39, -30, 4)
        )

        val initialBracketMatches = listOf(
            // Quarter Finals
            BracketMatch("qf_1", "Chennai FC", 3, "Salem City", 0, "Chennai FC", "Quarter Finals"),
            BracketMatch("qf_2", "Madurai United", 2, "Trichy Warriors", 1, "Madurai United", "Quarter Finals"),
            BracketMatch("qf_3", "Bengaluru Titans", 2, "Coimbatore Royals", 0, "Bengaluru Titans", "Quarter Finals"),
            BracketMatch("qf_4", "Malabar Strikers", 3, "Hyderabad Strikers", 2, "Malabar Strikers", "Quarter Finals"),
            // Semi Finals
            BracketMatch("sf_1", "Chennai FC", 2, "Bengaluru Titans", 1, "Chennai FC", "Semi Finals"),
            BracketMatch("sf_2", "Madurai United", 1, "Malabar Strikers", 0, "Madurai United", "Semi Finals"),
            // Final
            BracketMatch("fn_1", "Chennai FC", 3, "Madurai United", 1, "Chennai FC", "FINAL")
        )

        val initialNotifications = listOf(
            NotificationItem("notif_1", "🔴 Live Match Started", "Chennai FC vs Madurai United is now LIVE in the Championship!", "5m ago", NotificationType.LIVE),
            NotificationItem("notif_2", "⚽ Goal Scored!", "K. Raman scores an absolute stunner for Chennai FC (68')", "12m ago", NotificationType.GOAL),
            NotificationItem("notif_3", "🏆 Tournament Completed", "Delhi Smashers crowned champions of Grand Slam Badminton Open!", "1h ago", NotificationType.TROPHY),
            NotificationItem("notif_4", "📅 Match Scheduled", "All-India T20 Semifinal fixed for tomorrow 18:00 IST", "3h ago", NotificationType.SCHEDULE),
            NotificationItem("notif_5", "👤 New Player Registered", "Rahul Nair registered for Team Alpha Volleyball squad", "5h ago", NotificationType.PLAYER)
        )

        val initialOfficials = listOf(
            Official("off_1", "Marcus Anthony", "FIFA Certified Head Referee", "Football", 12, 184, "Grade A National"),
            Official("off_2", "K. Sreenivasan", "ICC Elite Panel Umpire", "Cricket", 15, 230, "Elite International"),
            Official("off_3", "Sarah Jenkins", "FIVB Level 3 Referee", "Volleyball", 8, 96, "Grade A"),
            Official("off_4", "D. Narayanan", "Senior Chief Umpire", "Badminton", 10, 142, "BWF Certified")
        )

        val initialVenues = listOf(
            Venue("ven_1", "Jawaharlal Nehru International Stadium", "Chennai", "40,000 Capacity", listOf("Football", "Athletics"), "Sydenhams Road, Park Town, Chennai", true),
            Venue("ven_2", "M. Chinnaswamy Stadium", "Bengaluru", "38,000 Capacity", listOf("Cricket"), "MG Road, Bengaluru", true),
            Venue("ven_3", "Regional Indoor Sports Complex", "Kochi", "12,000 Capacity", listOf("Volleyball", "Badminton", "Basketball"), "Kadavanthra, Kochi", true),
            Venue("ven_4", "Hitex Esports Arena", "Hyderabad", "8,500 Capacity", listOf("Esports", "Chess", "Table Tennis"), "Madhapur, Hyderabad", true)
        )
    }
}
