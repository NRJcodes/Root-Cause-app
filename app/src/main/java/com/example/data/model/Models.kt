package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

enum class ProblemCategory(val label: String) {
    PRODUCTION_OPERATIONS("Production/Operations"),
    FINANCIAL("Financial"),
    SUPPLY_CHAIN("Supply Chain"),
    QUALITY("Quality"),
    STAFFING_HR("Staffing/HR"),
    OTHER("Other");

    companion object {
        fun fromLabel(label: String): ProblemCategory =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: OTHER
    }
}

enum class ConfidenceLevel(val label: String) {
    CONFIRMED("CONFIRMED"),
    LIKELY("LIKELY"),
    SPECULATIVE("SPECULATIVE");

    companion object {
        fun fromString(str: String): ConfidenceLevel =
            when (str.trim().uppercase()) {
                "CONFIRMED" -> CONFIRMED
                "LIKELY" -> LIKELY
                else -> SPECULATIVE
            }
    }
}

data class ClarifyingQuestion(
    val id: String,
    val question: String,
    val guidance: String = "",
    var answer: String = ""
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("question", question)
        put("guidance", guidance)
        put("answer", answer)
    }

    companion object {
        fun fromJson(obj: JSONObject): ClarifyingQuestion = ClarifyingQuestion(
            id = obj.optString("id", System.currentTimeMillis().toString()),
            question = obj.optString("question", ""),
            guidance = obj.optString("guidance", ""),
            answer = obj.optString("answer", "")
        )
    }
}

data class RootCauseNode(
    val id: String,
    val title: String,
    val category: String, // People, Process, Equipment, Materials, Environment, Management
    val confidence: ConfidenceLevel,
    val evidenceReason: String,
    val subFactors: List<String> = emptyList()
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("category", category)
        put("confidence", confidence.name)
        put("evidenceReason", evidenceReason)
        val arr = JSONArray()
        subFactors.forEach { arr.put(it) }
        put("subFactors", arr)
    }

    companion object {
        fun fromJson(obj: JSONObject): RootCauseNode {
            val subList = mutableListOf<String>()
            val arr = obj.optJSONArray("subFactors")
            if (arr != null) {
                for (i in 0 until arr.length()) {
                    subList.add(arr.getString(i))
                }
            }
            return RootCauseNode(
                id = obj.optString("id", System.currentTimeMillis().toString()),
                title = obj.optString("title", ""),
                category = obj.optString("category", "Process"),
                confidence = ConfidenceLevel.fromString(obj.optString("confidence", "SPECULATIVE")),
                evidenceReason = obj.optString("evidenceReason", "no supporting data provided, inference only."),
                subFactors = subList
            )
        }
    }
}

data class RootCauseTree(
    val statedSymptom: String,
    val branches: List<RootCauseNode>
) {
    fun toJsonString(): String {
        val root = JSONObject()
        root.put("statedSymptom", statedSymptom)
        val arr = JSONArray()
        branches.forEach { arr.put(it.toJson()) }
        root.put("branches", arr)
        return root.toString()
    }

    companion object {
        fun fromJsonString(jsonStr: String): RootCauseTree {
            if (jsonStr.isBlank()) return RootCauseTree("", emptyList())
            return try {
                val root = JSONObject(jsonStr)
                val symptom = root.optString("statedSymptom", "")
                val list = mutableListOf<RootCauseNode>()
                val arr = root.optJSONArray("branches")
                if (arr != null) {
                    for (i in 0 until arr.length()) {
                        list.add(RootCauseNode.fromJson(arr.getJSONObject(i)))
                    }
                }
                RootCauseTree(symptom, list)
            } catch (e: Exception) {
                RootCauseTree("", emptyList())
            }
        }
    }
}

data class SolutionComparison(
    val id: String,
    val rootCauseAddressed: String,
    val solutionTitle: String,
    val description: String,
    val estimatedCost: String, // Low, Medium, High or numeric
    val estimatedTimeframe: String, // e.g. "2-4 Weeks", "Immediate"
    val riskLevel: String, // Low, Medium, High
    val expectedImpact: String // High, Medium, Low
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("rootCauseAddressed", rootCauseAddressed)
        put("solutionTitle", solutionTitle)
        put("description", description)
        put("estimatedCost", estimatedCost)
        put("estimatedTimeframe", estimatedTimeframe)
        put("riskLevel", riskLevel)
        put("expectedImpact", expectedImpact)
    }

    companion object {
        fun fromJson(obj: JSONObject): SolutionComparison = SolutionComparison(
            id = obj.optString("id", System.currentTimeMillis().toString()),
            rootCauseAddressed = obj.optString("rootCauseAddressed", ""),
            solutionTitle = obj.optString("solutionTitle", ""),
            description = obj.optString("description", ""),
            estimatedCost = obj.optString("estimatedCost", "Medium"),
            estimatedTimeframe = obj.optString("estimatedTimeframe", "1-3 Months"),
            riskLevel = obj.optString("riskLevel", "Medium"),
            expectedImpact = obj.optString("expectedImpact", "High")
        )
    }
}

@Entity(tableName = "investigation_sessions")
data class InvestigationSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val category: String,
    val title: String,
    val problemDescription: String,
    val uploadedDocumentText: String = "",
    val uploadedFileName: String = "",
    val clarifyingQuestionsJson: String = "[]",
    val rootCauseTreeJson: String = "{}",
    val solutionsJson: String = "[]",
    val recommendation: String = "",
    val currentStage: Int = 1, // 1 to 6
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    val email: String,
    val fullName: String,
    val passwordHash: String,
    val organization: String = "Enterprise Operations",
    val roleTitle: String = "Director of Strategy & Ops",
    val createdAt: Long = System.currentTimeMillis()
)
