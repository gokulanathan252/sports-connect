package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BracketMatch
import com.example.data.Match
import com.example.data.NotificationItem
import com.example.data.Official
import com.example.data.Player
import com.example.data.PointsTableRow
import com.example.data.Sport
import com.example.data.SportsConnectRepository
import com.example.data.Team
import com.example.data.Tournament
import com.example.data.Venue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    LANDING,
    DASHBOARD,
    SPORTS,
    LIVE_CENTER,
    TOURNAMENT_DETAIL,
    POINTS_TABLE,
    PLAYER_PROFILE,
    TEAM_PROFILE,
    ANALYTICS,
    VENUES_OFFICIALS
}

class SportsConnectViewModel(
    private val repository: SportsConnectRepository = SportsConnectRepository()
) : ViewModel() {

    val sports: StateFlow<List<Sport>> = repository.sports
    val tournaments: StateFlow<List<Tournament>> = repository.tournaments
    val teams: StateFlow<List<Team>> = repository.teams
    val players: StateFlow<List<Player>> = repository.players
    val matches: StateFlow<List<Match>> = repository.matches
    val pointsTable: StateFlow<List<PointsTableRow>> = repository.pointsTable
    val bracketMatches: StateFlow<List<BracketMatch>> = repository.bracketMatches
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val officials: StateFlow<List<Official>> = repository.officials
    val venues: StateFlow<List<Venue>> = repository.venues

    private val _currentScreen = MutableStateFlow(AppScreen.LANDING)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<AppScreen>()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Selection states
    private val _selectedTournament = MutableStateFlow<Tournament?>(null)
    val selectedTournament: StateFlow<Tournament?> = _selectedTournament.asStateFlow()

    private val _selectedMatch = MutableStateFlow<Match?>(null)
    val selectedMatch: StateFlow<Match?> = _selectedMatch.asStateFlow()

    private val _selectedTeam = MutableStateFlow<Team?>(null)
    val selectedTeam: StateFlow<Team?> = _selectedTeam.asStateFlow()

    private val _selectedPlayer = MutableStateFlow<Player?>(null)
    val selectedPlayer: StateFlow<Player?> = _selectedPlayer.asStateFlow()

    // Dialog & overlay states
    private val _showAddTournamentDialog = MutableStateFlow(false)
    val showAddTournamentDialog: StateFlow<Boolean> = _showAddTournamentDialog.asStateFlow()

    private val _showAddTeamDialog = MutableStateFlow(false)
    val showAddTeamDialog: StateFlow<Boolean> = _showAddTeamDialog.asStateFlow()

    private val _showAddPlayerDialog = MutableStateFlow(false)
    val showAddPlayerDialog: StateFlow<Boolean> = _showAddPlayerDialog.asStateFlow()

    private val _showScheduleMatchDialog = MutableStateFlow(false)
    val showScheduleMatchDialog: StateFlow<Boolean> = _showScheduleMatchDialog.asStateFlow()

    private val _showNotificationsDialog = MutableStateFlow(false)
    val showNotificationsDialog: StateFlow<Boolean> = _showNotificationsDialog.asStateFlow()

    // Celebration state
    private val _celebrationVisible = MutableStateFlow(false)
    val celebrationVisible: StateFlow<Boolean> = _celebrationVisible.asStateFlow()

    private val _celebrationTitle = MutableStateFlow("CHAMPION")
    val celebrationTitle: StateFlow<String> = _celebrationTitle.asStateFlow()

    private val _celebrationSubtitle = MutableStateFlow("Winner Crowned!")
    val celebrationSubtitle: StateFlow<String> = _celebrationSubtitle.asStateFlow()

    init {
        viewModelScope.launch {
            // Simulated initial universe loading screen
            delay(1600)
            _isLoading.value = false
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            return true
        } else if (_currentScreen.value != AppScreen.LANDING) {
            _currentScreen.value = AppScreen.LANDING
            return true
        }
        return false
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun selectTournament(tournament: Tournament) {
        _selectedTournament.value = tournament
        navigateTo(AppScreen.TOURNAMENT_DETAIL)
    }

    fun selectMatch(match: Match) {
        _selectedMatch.value = match
        navigateTo(AppScreen.LIVE_CENTER)
    }

    fun selectTeam(team: Team) {
        _selectedTeam.value = team
        navigateTo(AppScreen.TEAM_PROFILE)
    }

    fun selectPlayer(player: Player) {
        _selectedPlayer.value = player
        navigateTo(AppScreen.PLAYER_PROFILE)
    }

    fun updateScore(matchId: String, isTeamA: Boolean, delta: Int) {
        repository.updateScore(matchId, isTeamA, delta)
    }

    fun completeMatch(matchId: String, winnerTeamId: String) {
        repository.completeMatch(matchId, winnerTeamId)
        val match = matches.value.find { it.id == matchId }
        val winnerName = if (match?.teamA?.teamId == winnerTeamId) match.teamA.name else match?.teamB?.name ?: "Champions"

        triggerCelebration(
            title = "$winnerName 🏆",
            subtitle = "Won the match in ${match?.tournamentName ?: "Championship"}!"
        )
    }

    fun advanceBracket(bracketId: String, winnerTeam: String) {
        repository.advanceBracket(bracketId, winnerTeam)
        val bracket = bracketMatches.value.find { it.id == bracketId }
        if (bracket?.stage == "FINAL") {
            triggerCelebration(
                title = "$winnerTeam 👑",
                subtitle = "Grand Champions of the Tournament!"
            )
        }
    }

    fun triggerCelebration(title: String, subtitle: String) {
        _celebrationTitle.value = title
        _celebrationSubtitle.value = subtitle
        _celebrationVisible.value = true
    }

    fun dismissCelebration() {
        _celebrationVisible.value = false
    }

    fun addTournament(tournament: Tournament) {
        repository.addTournament(tournament)
    }

    fun addTeam(team: Team) {
        repository.addTeam(team)
    }

    fun addPlayer(player: Player) {
        repository.addPlayer(player)
    }

    fun scheduleMatch(match: Match) {
        repository.scheduleMatch(match)
    }

    fun markNotificationsRead() {
        repository.markAllNotificationsRead()
    }

    // Dialog toggles
    fun setShowAddTournamentDialog(show: Boolean) { _showAddTournamentDialog.value = show }
    fun setShowAddTeamDialog(show: Boolean) { _showAddTeamDialog.value = show }
    fun setShowAddPlayerDialog(show: Boolean) { _showAddPlayerDialog.value = show }
    fun setShowScheduleMatchDialog(show: Boolean) { _showScheduleMatchDialog.value = show }
    fun setShowNotificationsDialog(show: Boolean) { _showNotificationsDialog.value = show }
}
