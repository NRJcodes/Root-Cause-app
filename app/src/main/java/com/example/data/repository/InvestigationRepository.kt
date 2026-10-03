package com.example.data.repository

import com.example.data.ai.GeminiService
import com.example.data.local.InvestigationDao
import com.example.data.local.UserDao
import com.example.data.model.ClarifyingQuestion
import com.example.data.model.InvestigationSession
import com.example.data.model.RootCauseTree
import com.example.data.model.SolutionComparison
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

class InvestigationRepository(
    private val investigationDao: InvestigationDao,
    private val userDao: UserDao,
    private val geminiService: GeminiService
) {

    fun getSessionsForUser(userId: String): Flow<List<InvestigationSession>> =
        investigationDao.getSessionsForUser(userId)

    fun getSessionById(id: Long): Flow<InvestigationSession?> =
        investigationDao.getSessionById(id)

    suspend fun getSessionByIdSync(id: Long): InvestigationSession? =
        investigationDao.getSessionByIdSync(id)

    suspend fun saveSession(session: InvestigationSession): Long {
        return if (session.id == 0L) {
            investigationDao.insertSession(session)
        } else {
            investigationDao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
            session.id
        }
    }

    suspend fun deleteSession(id: Long) {
        investigationDao.deleteSession(id)
    }

    // User authentication (simple single-workspace / local persistence)
    suspend fun loginOrRegister(email: String, name: String, organization: String): UserProfile {
        val existing = userDao.getUserSync(email)
        if (existing != null) {
            return existing
        }
        val newUser = UserProfile(
            email = email,
            fullName = name.ifBlank {
                email.substringBefore("@").replace(".", " ")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
            },
            passwordHash = "local_hash",
            organization = organization.ifBlank { "Enterprise Global Operations" }
        )
        userDao.insertUser(newUser)
        return newUser
    }

    // Stage 2 generation
    suspend fun generateStage2Questions(session: InvestigationSession): InvestigationSession {
        val questions = geminiService.generateClarifyingQuestions(
            category = session.category,
            problemDescription = session.problemDescription,
            documentText = session.uploadedDocumentText
        )
        val arr = JSONArray()
        questions.forEach { arr.put(it.toJson()) }
        val updated = session.copy(
            clarifyingQuestionsJson = arr.toString(),
            currentStage = 2,
            updatedAt = System.currentTimeMillis()
        )
        investigationDao.updateSession(updated)
        return updated
    }

    // Stage 3 generation
    suspend fun generateStage3Investigation(session: InvestigationSession, questions: List<ClarifyingQuestion>): InvestigationSession {
        val tree = geminiService.generateRootCauseTree(
            category = session.category,
            problemDescription = session.problemDescription,
            documentText = session.uploadedDocumentText,
            clarifyingQA = questions
        )
        // Also update questions json in case answers changed
        val qArr = JSONArray()
        questions.forEach { qArr.put(it.toJson()) }

        val updated = session.copy(
            clarifyingQuestionsJson = qArr.toString(),
            rootCauseTreeJson = tree.toJsonString(),
            currentStage = 3,
            updatedAt = System.currentTimeMillis()
        )
        investigationDao.updateSession(updated)
        return updated
    }

    // Stage 4 generation
    suspend fun generateStage4Solutions(session: InvestigationSession): InvestigationSession {
        val tree = RootCauseTree.fromJsonString(session.rootCauseTreeJson)
        val solutions = geminiService.generateSolutions(
            symptom = tree.statedSymptom.ifBlank { session.problemDescription },
            rootCauses = tree.branches
        )
        val arr = JSONArray()
        solutions.forEach { arr.put(it.toJson()) }

        val updated = session.copy(
            solutionsJson = arr.toString(),
            currentStage = 4,
            updatedAt = System.currentTimeMillis()
        )
        investigationDao.updateSession(updated)
        return updated
    }

    // Stage 5 generation
    suspend fun generateStage5Recommendation(session: InvestigationSession): InvestigationSession {
        val tree = RootCauseTree.fromJsonString(session.rootCauseTreeJson)
        val solList = mutableListOf<SolutionComparison>()
        val arr = JSONArray(if (session.solutionsJson.isNotBlank()) session.solutionsJson else "[]")
        for (i in 0 until arr.length()) {
            solList.add(SolutionComparison.fromJson(arr.getJSONObject(i)))
        }

        val recommendation = geminiService.generateRecommendation(
            symptom = tree.statedSymptom.ifBlank { session.problemDescription },
            rootCauses = tree.branches,
            solutions = solList
        )

        val updated = session.copy(
            recommendation = recommendation,
            currentStage = 5,
            updatedAt = System.currentTimeMillis()
        )
        investigationDao.updateSession(updated)
        return updated
    }
}
