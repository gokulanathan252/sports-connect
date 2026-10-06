package com.example.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.CelebrationOverlay
import com.example.ui.components.SportsConnectBackground
import com.example.ui.components.SportsConnectLoadingScreen
import com.example.ui.screens.AddPlayerDialog
import com.example.ui.screens.AddTeamDialog
import com.example.ui.screens.AddTournamentDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.BracketVisualizationView
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.LiveMatchCenterScreen
import com.example.ui.screens.NotificationsDialog
import com.example.ui.screens.PlayerProfileScreen
import com.example.ui.screens.PointsTableScreenView
import com.example.ui.screens.ScheduleMatchDialog
import com.example.ui.screens.SportExplorerScreen
import com.example.ui.screens.TeamProfileScreen
import com.example.ui.screens.TournamentDetailScreen
import com.example.ui.screens.VenuesOfficialsScreen
import com.example.ui.theme.SportsCyan
import com.example.ui.theme.SportsEmerald
import com.example.ui.theme.SportsGold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsConnectApp(
    viewModel: SportsConnectViewModel,
    modifier: Modifier = Modifier
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    if (isLoading) {
        SportsConnectLoadingScreen()
        return
    }

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    val sports by viewModel.sports.collectAsStateWithLifecycle()
    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val teams by viewModel.teams.collectAsStateWithLifecycle()
    val players by viewModel.players.collectAsStateWithLifecycle()
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val pointsTable by viewModel.pointsTable.collectAsStateWithLifecycle()
    val bracketMatches by viewModel.bracketMatches.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val officials by viewModel.officials.collectAsStateWithLifecycle()
    val venues by viewModel.venues.collectAsStateWithLifecycle()

    val selectedTournament by viewModel.selectedTournament.collectAsStateWithLifecycle()
    val selectedMatch by viewModel.selectedMatch.collectAsStateWithLifecycle()
    val selectedTeam by viewModel.selectedTeam.collectAsStateWithLifecycle()
    val selectedPlayer by viewModel.selectedPlayer.collectAsStateWithLifecycle()

    val showAddTournament by viewModel.showAddTournamentDialog.collectAsStateWithLifecycle()
    val showAddTeam by viewModel.showAddTeamDialog.collectAsStateWithLifecycle()
    val showAddPlayer by viewModel.showAddPlayerDialog.collectAsStateWithLifecycle()
    val showScheduleMatch by viewModel.showScheduleMatchDialog.collectAsStateWithLifecycle()
    val showNotifications by viewModel.showNotificationsDialog.collectAsStateWithLifecycle()

    val celebrationVisible by viewModel.celebrationVisible.collectAsStateWithLifecycle()
    val celebrationTitle by viewModel.celebrationTitle.collectAsStateWithLifecycle()
    val celebrationSubtitle by viewModel.celebrationSubtitle.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Android System Back Button Handler
    BackHandler(enabled = currentScreen != AppScreen.LANDING) {
        viewModel.navigateBack()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF0F1726),
                drawerContentColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.sports_connect_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SPORTSCONNECT",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Tournament Platform",
                                color = SportsCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF1E293B))

                Spacer(modifier = Modifier.height(10.dp))

                DrawerMenuItem(
                    icon = Icons.Default.Home,
                    label = "Home / Landing",
                    selected = currentScreen == AppScreen.LANDING,
                    onClick = {
                        viewModel.navigateTo(AppScreen.LANDING)
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.SportsScore,
                    label = "Dashboard",
                    selected = currentScreen == AppScreen.DASHBOARD,
                    onClick = {
                        viewModel.navigateTo(AppScreen.DASHBOARD)
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Sports,
                    label = "Sport Explorer (20+)",
                    selected = currentScreen == AppScreen.SPORTS,
                    onClick = {
                        viewModel.navigateTo(AppScreen.SPORTS)
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.EmojiEvents,
                    label = "Tournaments",
                    selected = currentScreen == AppScreen.TOURNAMENT_DETAIL,
                    onClick = {
                        viewModel.selectTournament(tournaments.first())
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.SportsScore,
                    label = "Live Match Center",
                    selected = currentScreen == AppScreen.LIVE_CENTER,
                    onClick = {
                        viewModel.navigateTo(AppScreen.LIVE_CENTER)
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.TableChart,
                    label = "Points Table & Standings",
                    selected = currentScreen == AppScreen.POINTS_TABLE,
                    onClick = {
                        viewModel.navigateTo(AppScreen.POINTS_TABLE)
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.People,
                    label = "Teams & Players",
                    selected = currentScreen == AppScreen.TEAM_PROFILE || currentScreen == AppScreen.PLAYER_PROFILE,
                    onClick = {
                        viewModel.selectTeam(teams.first())
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Analytics,
                    label = "Analytics & Reports",
                    selected = currentScreen == AppScreen.ANALYTICS,
                    onClick = {
                        viewModel.navigateTo(AppScreen.ANALYTICS)
                        coroutineScope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.LocationCity,
                    label = "Venues & Officials",
                    selected = currentScreen == AppScreen.VENUES_OFFICIALS,
                    onClick = {
                        viewModel.navigateTo(AppScreen.VENUES_OFFICIALS)
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        SportsConnectBackground {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.sports_connect_logo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SPORTSCONNECT",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                },
                                modifier = Modifier.testTag("app_menu_drawer_button")
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                            }
                        },
                        actions = {
                            // Notifications bell
                            Box {
                                IconButton(
                                    onClick = { viewModel.setShowNotificationsDialog(true) },
                                    modifier = Modifier.testTag("app_notifications_button")
                                ) {
                                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = SportsCyan)
                                }
                                if (notifications.any { !it.isRead }) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF3366))
                                    )
                                }
                            }

                            // Dark / Light Mode Toggle
                            IconButton(
                                onClick = { viewModel.toggleTheme() },
                                modifier = Modifier.testTag("theme_toggle_button")
                            ) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Theme Toggle",
                                    tint = SportsGold
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF0A0F1A).copy(alpha = 0.85f)
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF0C1322),
                        contentColor = SportsCyan
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.LANDING,
                            onClick = { viewModel.navigateTo(AppScreen.LANDING) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = SportsCyan,
                                indicatorColor = SportsCyan,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_home")
                        )

                        NavigationBarItem(
                            selected = currentScreen == AppScreen.DASHBOARD,
                            onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                            icon = { Icon(Icons.Default.SportsScore, contentDescription = "Dashboard") },
                            label = { Text("Dashboard", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = SportsCyan,
                                indicatorColor = SportsCyan,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_dashboard")
                        )

                        NavigationBarItem(
                            selected = currentScreen == AppScreen.LIVE_CENTER,
                            onClick = { viewModel.navigateTo(AppScreen.LIVE_CENTER) },
                            icon = { Icon(Icons.Default.SportsScore, contentDescription = "Live") },
                            label = { Text("Live Now", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = Color(0xFFFF3366),
                                indicatorColor = Color(0xFFFF3366),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_live")
                        )

                        NavigationBarItem(
                            selected = currentScreen == AppScreen.SPORTS,
                            onClick = { viewModel.navigateTo(AppScreen.SPORTS) },
                            icon = { Icon(Icons.Default.Sports, contentDescription = "Sports") },
                            label = { Text("Sports", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = SportsCyan,
                                indicatorColor = SportsCyan,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_sports")
                        )

                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") },
                            label = { Text("Menu", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_menu")
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_switch"
                    ) { target ->
                        when (target) {
                            AppScreen.LANDING -> LandingScreen(
                                tournaments = tournaments,
                                liveMatches = matches.filter { it.status == com.example.data.MatchStatus.LIVE },
                                sports = sports,
                                onExploreSportsClick = { viewModel.navigateTo(AppScreen.SPORTS) },
                                onStartTournamentClick = { viewModel.setShowAddTournamentDialog(true) },
                                onViewTournament = { viewModel.selectTournament(it) },
                                onViewMatch = { viewModel.selectMatch(it) },
                                onOpenDashboard = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                            )

                            AppScreen.DASHBOARD -> DashboardScreen(
                                liveMatches = matches.filter { it.status == com.example.data.MatchStatus.LIVE },
                                tournaments = tournaments,
                                onAddTournamentClick = { viewModel.setShowAddTournamentDialog(true) },
                                onAddMatchClick = { viewModel.setShowScheduleMatchDialog(true) },
                                onAddTeamClick = { viewModel.setShowAddTeamDialog(true) },
                                onAddPlayerClick = { viewModel.setShowAddPlayerDialog(true) },
                                onViewMatch = { viewModel.selectMatch(it) },
                                onViewTournament = { viewModel.selectTournament(it) },
                                onNavigateToAnalytics = { viewModel.navigateTo(AppScreen.ANALYTICS) },
                                onNavigateToPointsTable = { viewModel.navigateTo(AppScreen.POINTS_TABLE) }
                            )

                            AppScreen.SPORTS -> SportExplorerScreen(
                                sports = sports,
                                onSportSelect = { sport ->
                                    val tour = tournaments.find { it.sport.equals(sport.name, ignoreCase = true) }
                                    if (tour != null) viewModel.selectTournament(tour)
                                    else viewModel.setShowAddTournamentDialog(true)
                                }
                            )

                            AppScreen.LIVE_CENTER -> LiveMatchCenterScreen(
                                matches = matches,
                                selectedMatch = selectedMatch,
                                onSelectMatch = { viewModel.selectMatch(it) },
                                onUpdateScore = { id, isA, d -> viewModel.updateScore(id, isA, d) },
                                onCompleteMatch = { id, wId -> viewModel.completeMatch(id, wId) },
                                onBack = { viewModel.navigateBack() }
                            )

                            AppScreen.TOURNAMENT_DETAIL -> {
                                val tour = selectedTournament ?: tournaments.first()
                                TournamentDetailScreen(
                                    tournament = tour,
                                    matches = matches.filter { it.tournamentId == tour.id || it.sport == tour.sport },
                                    teams = teams.filter { it.sport == tour.sport },
                                    players = players.filter { it.sport == tour.sport || it.teamId.isNotEmpty() },
                                    pointsTable = pointsTable,
                                    bracketMatches = bracketMatches,
                                    onBack = { viewModel.navigateBack() },
                                    onViewMatch = { viewModel.selectMatch(it) },
                                    onViewTeam = { viewModel.selectTeam(it) },
                                    onViewPlayer = { viewModel.selectPlayer(it) },
                                    onAdvanceBracket = { bId, wTeam -> viewModel.advanceBracket(bId, wTeam) }
                                )
                            }

                            AppScreen.POINTS_TABLE -> {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "CHAMPIONSHIP STANDINGS",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    PointsTableScreenView(pointsTable = pointsTable)
                                }
                            }

                            AppScreen.PLAYER_PROFILE -> {
                                val player = selectedPlayer ?: players.first()
                                PlayerProfileScreen(
                                    player = player,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }

                            AppScreen.TEAM_PROFILE -> {
                                val team = selectedTeam ?: teams.first()
                                TeamProfileScreen(
                                    team = team,
                                    players = players,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }

                            AppScreen.ANALYTICS -> AnalyticsScreen()

                            AppScreen.VENUES_OFFICIALS -> VenuesOfficialsScreen(
                                venues = venues,
                                officials = officials
                            )
                        }
                    }

                    // Celebration Overlay (Confetti + Champion Modal)
                    CelebrationOverlay(
                        visible = celebrationVisible,
                        title = celebrationTitle,
                        subtitle = celebrationSubtitle,
                        onDismiss = { viewModel.dismissCelebration() }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showAddTournament) {
        AddTournamentDialog(
            onDismiss = { viewModel.setShowAddTournamentDialog(false) },
            onConfirm = { viewModel.addTournament(it) }
        )
    }

    if (showAddTeam) {
        AddTeamDialog(
            onDismiss = { viewModel.setShowAddTeamDialog(false) },
            onConfirm = { viewModel.addTeam(it) }
        )
    }

    if (showAddPlayer) {
        AddPlayerDialog(
            teams = teams,
            onDismiss = { viewModel.setShowAddPlayerDialog(false) },
            onConfirm = { viewModel.addPlayer(it) }
        )
    }

    if (showScheduleMatch) {
        ScheduleMatchDialog(
            tournaments = tournaments,
            teams = teams,
            onDismiss = { viewModel.setShowScheduleMatchDialog(false) },
            onConfirm = { viewModel.scheduleMatch(it) }
        )
    }

    if (showNotifications) {
        NotificationsDialog(
            notifications = notifications,
            onDismiss = { viewModel.setShowNotificationsDialog(false) },
            onMarkAllRead = { viewModel.markNotificationsRead() }
        )
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, tint = if (selected) SportsCyan else Color(0xFF94A3B8)) },
        label = {
            Text(
                text = label,
                color = if (selected) Color.White else Color(0xFFCBD5E1),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFF1E2C44),
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}
