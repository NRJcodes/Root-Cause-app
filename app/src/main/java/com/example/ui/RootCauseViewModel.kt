package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiService
import com.example.data.local.RootCauseDatabase
import com.example.data.model.ClarifyingQuestion
import com.example.data.model.ConfidenceLevel
import com.example.data.model.InvestigationSession
import com.example.data.model.ProblemCategory
import com.example.data.model.RootCauseTree
import com.example.data.model.SolutionComparison
import com.example.data.model.UserProfile
import com.example.data.repository.InvestigationRepository
import com.example.util.PdfExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.File

data class PresetScenario(
    val title: String,
    val category: ProblemCategory,
    val problemDescription: String,
    val documentSnippet: String,
    val fileName: String
)

data class UiState(
    val currentUser: UserProfile? = null,
    val isLoggedIn: Boolean = false,
    val sessions: List<InvestigationSession> = emptyList(),
    val currentSession: InvestigationSession? = null,
    val currentStage: Int = 1, // 1 to 6
    val isLoading: Boolean = false,
    val loadingMessage: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,

    // Stage 1 Fields
    val intakeCategory: ProblemCategory = ProblemCategory.PRODUCTION_OPERATIONS,
    val intakeTitle: String = "",
    val intakeProblem: String = "",
    val intakeDocText: String = "",
    val intakeFileName: String = "",

    // Stage 2 Fields
    val clarifyingQuestions: List<ClarifyingQuestion> = emptyList(),

    // Stage 3 Fields
    val rootCauseTree: RootCauseTree = RootCauseTree("", emptyList()),
    val confidenceFilter: ConfidenceLevel? = null,
    val categoryFilter: String? = null,

    // Stage 4 Fields
    val solutions: List<SolutionComparison> = emptyList(),

    // Stage 5 Fields
    val recommendation: String = "",

    // Stage 6 Fields
    val lastExportedFile: File? = null
)

class RootCauseViewModel(application: Application) : AndroidViewModel(application) {

    private val db = RootCauseDatabase.getDatabase(application)
    private val repository = InvestigationRepository(
        investigationDao = db.investigationDao(),
        userDao = db.userDao(),
        geminiService = GeminiService()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // Automatically authenticate with default enterprise lead analyst profile
        viewModelScope.launch {
            val user = repository.loginOrRegister(
                email = "lead.analyst@enterprise.com",
                name = "Eleanor Vance",
                organization = "Global Strategy & Operations"
            )
            _uiState.update { it.copy(currentUser = user, isLoggedIn = true) }
            observeSessions(user.email)
        }
    }

    private fun observeSessions(userId: String) {
        viewModelScope.launch {
            repository.getSessionsForUser(userId).collect { list ->
                _uiState.update { state ->
                    state.copy(sessions = list)
                }
            }
        }
    }

    fun login(email: String, name: String, organization: String) {
        viewModelScope.launch {
            try {
                val user = repository.loginOrRegister(email, name, organization)
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        isLoggedIn = true,
                        errorMessage = null
                    )
                }
                observeSessions(user.email)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Login failed: ${e.message}") }
            }
        }
    }

    fun logout() {
        _uiState.update {
            UiState(
                currentUser = null,
                isLoggedIn = false
            )
        }
    }

    fun startNewSession() {
        _uiState.update {
            it.copy(
                currentSession = null,
                currentStage = 1,
                intakeCategory = ProblemCategory.PRODUCTION_OPERATIONS,
                intakeTitle = "",
                intakeProblem = "",
                intakeDocText = "",
                intakeFileName = "",
                clarifyingQuestions = emptyList(),
                rootCauseTree = RootCauseTree("", emptyList()),
                solutions = emptyList(),
                recommendation = "",
                confidenceFilter = null,
                categoryFilter = null,
                errorMessage = null
            )
        }
    }

    fun selectSession(session: InvestigationSession) {
        val qList = mutableListOf<ClarifyingQuestion>()
        try {
            val qArr = JSONArray(if (session.clarifyingQuestionsJson.isNotBlank()) session.clarifyingQuestionsJson else "[]")
            for (i in 0 until qArr.length()) {
                qList.add(ClarifyingQuestion.fromJson(qArr.getJSONObject(i)))
            }
        } catch (_: Exception) {}

        val tree = RootCauseTree.fromJsonString(session.rootCauseTreeJson)

        val solList = mutableListOf<SolutionComparison>()
        try {
            val sArr = JSONArray(if (session.solutionsJson.isNotBlank()) session.solutionsJson else "[]")
            for (i in 0 until sArr.length()) {
                solList.add(SolutionComparison.fromJson(sArr.getJSONObject(i)))
            }
        } catch (_: Exception) {}

        _uiState.update {
            it.copy(
                currentSession = session,
                currentStage = session.currentStage.coerceIn(1, 6),
                intakeCategory = ProblemCategory.fromLabel(session.category),
                intakeTitle = session.title,
                intakeProblem = session.problemDescription,
                intakeDocText = session.uploadedDocumentText,
                intakeFileName = session.uploadedFileName,
                clarifyingQuestions = qList,
                rootCauseTree = tree,
                solutions = solList,
                recommendation = session.recommendation,
                errorMessage = null
            )
        }
    }

    fun setStage(stage: Int) {
        _uiState.update { it.copy(currentStage = stage.coerceIn(1, 6)) }
    }

    fun updateIntakeCategory(cat: ProblemCategory) {
        _uiState.update { it.copy(intakeCategory = cat) }
    }

    fun updateIntakeTitle(title: String) {
        _uiState.update { it.copy(intakeTitle = title) }
    }

    fun updateIntakeProblem(problem: String) {
        _uiState.update { it.copy(intakeProblem = problem) }
    }

    fun updateIntakeDocText(text: String, fileName: String = "") {
        _uiState.update {
            it.copy(
                intakeDocText = text,
                intakeFileName = if (fileName.isNotBlank()) fileName else if (text.isNotBlank()) "uploaded_notes.txt" else ""
            )
        }
    }

    fun loadPreset(preset: PresetScenario) {
        _uiState.update {
            it.copy(
                intakeCategory = preset.category,
                intakeTitle = preset.title,
                intakeProblem = preset.problemDescription,
                intakeDocText = preset.documentSnippet,
                intakeFileName = preset.fileName,
                errorMessage = null
            )
        }
    }

    /**
     * Stage 1 -> Stage 2:
     * Save Intake and call AI to generate clarifying questions
     */
    fun submitStage1Intake() {
        val state = _uiState.value
        if (state.intakeProblem.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a description of the problem or symptom.") }
            return
        }
        val user = state.currentUser ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    loadingMessage = "Analyzing intake evidence & generating targeted clarifying questions...",
                    errorMessage = null
                )
            }
            try {
                val current = state.currentSession
                val sessionToSave = (current ?: InvestigationSession(
                    userId = user.email,
                    category = state.intakeCategory.label,
                    title = state.intakeTitle.ifBlank { "${state.intakeCategory.label} Diagnostic" },
                    problemDescription = state.intakeProblem,
                    uploadedDocumentText = state.intakeDocText,
                    uploadedFileName = state.intakeFileName
                )).copy(
                    category = state.intakeCategory.label,
                    title = state.intakeTitle.ifBlank { "${state.intakeCategory.label} Diagnostic" },
                    problemDescription = state.intakeProblem,
                    uploadedDocumentText = state.intakeDocText,
                    uploadedFileName = state.intakeFileName
                )

                val savedId = repository.saveSession(sessionToSave)
                val persistedSession = sessionToSave.copy(id = savedId)

                val updatedSession = repository.generateStage2Questions(persistedSession)

                val qList = mutableListOf<ClarifyingQuestion>()
                val qArr = JSONArray(updatedSession.clarifyingQuestionsJson)
                for (i in 0 until qArr.length()) {
                    qList.add(ClarifyingQuestion.fromJson(qArr.getJSONObject(i)))
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentSession = updatedSession,
                        currentStage = 2,
                        clarifyingQuestions = qList
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Stage 1 submission error: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateQuestionAnswer(questionId: String, answer: String) {
        _uiState.update { state ->
            val updated = state.clarifyingQuestions.map {
                if (it.id == questionId) it.copy(answer = answer) else it
            }
            state.copy(clarifyingQuestions = updated)
        }
    }

    /**
     * Stage 2 -> Stage 3:
     * Save Clarifying Answers and call AI to generate Root Cause Tree
     */
    fun submitStage2Clarifying() {
        val state = _uiState.value
        val session = state.currentSession ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    loadingMessage = "Synthesizing evidence & constructing root-cause tree with confidence tags...",
                    errorMessage = null
                )
            }
            try {
                val updatedSession = repository.generateStage3Investigation(session, state.clarifyingQuestions)
                val tree = RootCauseTree.fromJsonString(updatedSession.rootCauseTreeJson)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentSession = updatedSession,
                        currentStage = 3,
                        rootCauseTree = tree
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Stage 2 analysis error: ${e.message}"
                    )
                }
            }
        }
    }

    fun setConfidenceFilter(level: ConfidenceLevel?) {
        _uiState.update { it.copy(confidenceFilter = level) }
    }

    fun setCategoryFilter(category: String?) {
        _uiState.update { it.copy(categoryFilter = category) }
    }

    /**
     * Stage 3 -> Stage 4:
     * Generate Solution Comparison Matrix for CONFIRMED & LIKELY root causes
     */
    fun proceedToStage4Solutions() {
        val state = _uiState.value
        val session = state.currentSession ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    loadingMessage = "Evaluating confirmed & likely root causes to formulate solution comparison matrix...",
                    errorMessage = null
                )
            }
            try {
                val updatedSession = repository.generateStage4Solutions(session)
                val solList = mutableListOf<SolutionComparison>()
                val sArr = JSONArray(updatedSession.solutionsJson)
                for (i in 0 until sArr.length()) {
                    solList.add(SolutionComparison.fromJson(sArr.getJSONObject(i)))
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentSession = updatedSession,
                        currentStage = 4,
                        solutions = solList
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Stage 3 -> 4 error: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Stage 4 -> Stage 5:
     * Generate Final Executive Recommendation
     */
    fun proceedToStage5Recommendation() {
        val state = _uiState.value
        val session = state.currentSession ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    loadingMessage = "Synthesizing strategic trade-offs & formulating governed final recommendation...",
                    errorMessage = null
                )
            }
            try {
                val updatedSession = repository.generateStage5Recommendation(session)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentSession = updatedSession,
                        currentStage = 5,
                        recommendation = updatedSession.recommendation
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Stage 4 -> 5 error: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Stage 5 -> Stage 6:
     * Move to Export & Report Review
     */
    fun proceedToStage6Export() {
        _uiState.update { it.copy(currentStage = 6) }
    }

    fun exportAndSharePdf(context: Context) {
        val state = _uiState.value
        val session = state.currentSession ?: return
        val user = state.currentUser

        try {
            val file = PdfExporter.generatePdfReport(context, session, user?.email ?: "analyst@enterprise.com")
            _uiState.update { it.copy(lastExportedFile = file, successMessage = "PDF generated successfully.") }
            PdfExporter.sharePdf(context, file)
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to export PDF: ${e.message}") }
        }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            if (_uiState.value.currentSession?.id == sessionId) {
                startNewSession()
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
