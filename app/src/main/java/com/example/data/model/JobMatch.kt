package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MatchStatus {
    MATCHED,
    PARTIAL,
    MISSING
}

enum class TrustLevel {
    KNOWN,       // 🟢 User explicitly provided it in projects/experience
    INFERRED,    // 🟡 Plausible extension based on provided tech stack
    MISSING      // 🔴 Not in profile; requires candidate confirmation
}

data class RequirementCriterion(
    val title: String,
    val category: String, // "Required Skill", "Preferred Skill", "Core Responsibility", "Experience Signal"
    val status: MatchStatus,
    val candidateEvidence: String,
    val actionableNote: String
)

data class ResumeFitBreakdown(
    val keywordCoverage: Int = 24,
    val keywordCoverageMax: Int = 30,
    val keywordCoverageDetails: String = "Matches 80% of core JD keywords.",
    
    val skillsAlignment: Int = 18,
    val skillsAlignmentMax: Int = 20,
    val skillsAlignmentDetails: String = "Strong Python, FastAPI, and SQL alignment.",
    
    val experienceRelevance: Int = 15,
    val experienceRelevanceMax: Int = 20,
    val experienceRelevanceDetails: String = "High backend relevance; cloud signals need AWS metrics.",
    
    val achievementQuality: Int = 12,
    val achievementQualityMax: Int = 15,
    val achievementQualityDetails: String = "Strong action verbs with quantifiable project outcomes.",
    
    val atsFormatting: Int = 10,
    val atsFormattingMax: Int = 10,
    val atsFormattingDetails: String = "Clean single-column standard typography, parsed without errors.",
    
    val clarity: Int = 5,
    val clarityMax: Int = 5,
    val clarityDetails: String = "Concise bullet points under 2 lines with clear technical depth."
) {
    val totalScore: Int
        get() = keywordCoverage + skillsAlignment + experienceRelevance + achievementQuality + atsFormatting + clarity
}

data class ResumeFixSuggestion(
    val id: String,
    val section: String,
    val currentText: String,
    val suggestedText: String,
    val rationale: List<String>,
    val trustTag: TrustLevel,
    val isApplied: Boolean = false
)

data class LearningPlanDay(
    val dayRange: String,
    val topic: String,
    val focusArea: String,
    val actionItem: String
)

@Entity(tableName = "job_matches")
data class JobMatchRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobTitle: String,
    val companyName: String,
    val location: String,
    val rawJobDescription: String,
    val fitScore: Int,
    val fitBreakdown: ResumeFitBreakdown,
    val criteria: List<RequirementCriterion>,
    val fixes: List<ResumeFixSuggestion>,
    val missingCriticalSkills: List<String>,
    val learningPlan: List<LearningPlanDay>,
    val tailoredResumeMarkdown: String = "",
    val tailoredCoverLetter: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
