package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AuthDialog
import com.example.ui.components.SessionsBottomSheet
import com.example.ui.components.StageProgressBar
import com.example.ui.components.TopEnterpriseBar
import com.example.ui.screens.Stage1IntakeScreen
import com.example.ui.screens.Stage2ClarifyingScreen
import com.example.ui.screens.Stage3InvestigationScreen
import com.example.ui.screens.Stage4SolutionsScreen
import com.example.ui.screens.Stage5RecommendationScreen
import com.example.ui.screens.Stage6ExportScreen
import com.example.ui.theme.EnterpriseBackground

@Composable
fun RootCauseApp(
    viewModel: RootCauseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showHistorySheet by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    // Handle back button smoothly
    BackHandler(enabled = uiState.currentStage > 1 || showHistorySheet || showAuthDialog) {
        when {
            showHistorySheet -> showHistorySheet = false
            showAuthDialog -> showAuthDialog = false
            uiState.currentStage > 1 -> viewModel.setStage(uiState.currentStage - 1)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopEnterpriseBar(
                    currentUser = uiState.currentUser,
                    onNewDiagnosticClick = { viewModel.startNewSession() },
                    onHistoryClick = { showHistorySheet = true },
                    onProfileClick = { showAuthDialog = true }
                )

                StageProgressBar(
                    currentStage = uiState.currentStage,
                    maxCompletedStage = uiState.currentSession?.currentStage ?: 1,
                    onStageClick = { viewModel.setStage(it) }
                )
            }
        },
        containerColor = EnterpriseBackground,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentStage) {
                1 -> Stage1IntakeScreen(
                    category = uiState.intakeCategory,
                    title = uiState.intakeTitle,
                    problemDescription = uiState.intakeProblem,
                    docText = uiState.intakeDocText,
                    docFileName = uiState.intakeFileName,
                    isLoading = uiState.isLoading,
                    onCategoryChange = { viewModel.updateIntakeCategory(it) },
                    onTitleChange = { viewModel.updateIntakeTitle(it) },
                    onProblemChange = { viewModel.updateIntakeProblem(it) },
                    onDocTextChange = { text, file -> viewModel.updateIntakeDocText(text, file) },
                    onLoadPreset = { viewModel.loadPreset(it) },
                    onSubmit = { viewModel.submitStage1Intake() }
                )

                2 -> Stage2ClarifyingScreen(
                    symptomSummary = uiState.intakeProblem,
                    questions = uiState.clarifyingQuestions,
                    isLoading = uiState.isLoading,
                    onAnswerChange = { id, ans -> viewModel.updateQuestionAnswer(id, ans) },
                    onBack = { viewModel.setStage(1) },
                    onSubmit = { viewModel.submitStage2Clarifying() }
                )

                3 -> Stage3InvestigationScreen(
                    tree = uiState.rootCauseTree,
                    confidenceFilter = uiState.confidenceFilter,
                    categoryFilter = uiState.categoryFilter,
                    isLoading = uiState.isLoading,
                    onConfidenceFilterChange = { viewModel.setConfidenceFilter(it) },
                    onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
                    onBack = { viewModel.setStage(2) },
                    onProceedToSolutions = { viewModel.proceedToStage4Solutions() }
                )

                4 -> Stage4SolutionsScreen(
                    solutions = uiState.solutions,
                    isLoading = uiState.isLoading,
                    onBack = { viewModel.setStage(3) },
                    onProceedToRecommendation = { viewModel.proceedToStage5Recommendation() }
                )

                5 -> Stage5RecommendationScreen(
                    recommendationText = uiState.recommendation,
                    onBack = { viewModel.setStage(4) },
                    onProceedToExport = { viewModel.proceedToStage6Export() }
                )

                6 -> Stage6ExportScreen(
                    session = uiState.currentSession,
                    clarifyingQuestions = uiState.clarifyingQuestions,
                    tree = uiState.rootCauseTree,
                    solutions = uiState.solutions,
                    recommendation = uiState.recommendation,
                    lastExportedFile = uiState.lastExportedFile,
                    onExportPdf = { viewModel.exportAndSharePdf(context) },
                    onStartNew = { viewModel.startNewSession() },
                    onBack = { viewModel.setStage(5) }
                )
            }
        }
    }

    if (showHistorySheet) {
        SessionsBottomSheet(
            sessions = uiState.sessions,
            activeSessionId = uiState.currentSession?.id,
            onSelectSession = { viewModel.selectSession(it) },
            onDeleteSession = { viewModel.deleteSession(it) },
            onDismiss = { showHistorySheet = false }
        )
    }

    if (showAuthDialog) {
        AuthDialog(
            currentUser = uiState.currentUser,
            onLogin = { email, name, org -> viewModel.login(email, name, org) },
            onLogout = { viewModel.logout() },
            onDismiss = { showAuthDialog = false }
        )
    }
}
