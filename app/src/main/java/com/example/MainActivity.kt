package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CareerPilotViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CareerPilotApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerPilotApp(
    viewModel: CareerPilotViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle BackHandler for sub-screens
    if (uiState.currentTab != ScreenTab.DASHBOARD) {
        BackHandler {
            viewModel.setScreenTab(ScreenTab.DASHBOARD)
        }
    }

    // Show notifications in snackbar
    LaunchedEffect(uiState.userNotification) {
        uiState.userNotification?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(IndigoPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CareerPilot",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = IndigoPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "AI",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = IndigoPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "One Profile • Tailored Applications",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = EmeraldSuccess.copy(alpha = 0.12f),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ResumeFit ${uiState.currentMatch?.fitScore ?: 84}/100",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.DASHBOARD,
                    onClick = { viewModel.setScreenTab(ScreenTab.DASHBOARD) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home") },
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.PROFILE,
                    onClick = { viewModel.setScreenTab(ScreenTab.PROFILE) },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Career Profile") },
                    label = { Text("Profile") },
                    modifier = Modifier.testTag("nav_tab_profile")
                )
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.JOB_MATCH,
                    onClick = { viewModel.setScreenTab(ScreenTab.JOB_MATCH) },
                    icon = { Icon(Icons.Default.Bolt, contentDescription = "Job Match Engine") },
                    label = { Text("Match") },
                    modifier = Modifier.testTag("nav_tab_match")
                )
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.APPLICATIONS,
                    onClick = { viewModel.setScreenTab(ScreenTab.APPLICATIONS) },
                    icon = { Icon(Icons.Default.ViewKanban, contentDescription = "Applications CRM") },
                    label = { Text("CRM") },
                    modifier = Modifier.testTag("nav_tab_crm")
                )
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.INTERVIEW_PREP,
                    onClick = { viewModel.setScreenTab(ScreenTab.INTERVIEW_PREP) },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "Interview Coach") },
                    label = { Text("Interview") },
                    modifier = Modifier.testTag("nav_tab_interview")
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    ScreenTab.DASHBOARD -> DashboardScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.setScreenTab(it) },
                        onQuickAnalyze = { viewModel.setScreenTab(ScreenTab.JOB_MATCH) }
                    )
                    ScreenTab.PROFILE -> ProfileScreen(
                        profile = uiState.profile,
                        onSaveProfile = { viewModel.saveProfile(it) },
                        onResetDefault = { viewModel.resetProfileToDefault() }
                    )
                    ScreenTab.JOB_MATCH -> JobMatchScreen(
                        uiState = uiState,
                        onUpdateJdInput = { t, c, l, jd -> viewModel.updateJdInput(t, c, l, jd) },
                        onLoadSample = { viewModel.loadSampleJob(it) },
                        onRunAnalysis = { viewModel.runJobMatchAnalysis() },
                        onApplyFix = { viewModel.applyFixToResume(it) },
                        onSaveToCrm = {
                            viewModel.addApplicationFromCurrentMatch()
                        }
                    )
                    ScreenTab.APPLICATIONS -> ApplicationCrmScreen(
                        uiState = uiState,
                        onUpdateStatus = { id, status -> viewModel.updateApplicationStatus(id, status) },
                        onDeleteApplication = { viewModel.deleteApplication(it) }
                    )
                    ScreenTab.INTERVIEW_PREP -> InterviewCoachScreen(
                        uiState = uiState,
                        onTogglePracticed = { viewModel.toggleQuestionPracticed(it) }
                    )
                }
            }
        }
    }
}
