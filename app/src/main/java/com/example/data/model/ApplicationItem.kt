package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ApplicationStatus(val label: String) {
    BOOKMARKED("Bookmarked"),
    APPLIED("Applied"),
    SCREENING("Screening"),
    INTERVIEW("Interviewing"),
    OFFER("Offer Received"),
    REJECTED("Archived / Rejected")
}

data class InterviewQuestionItem(
    val id: String,
    val category: String, // "Technical", "Project Architecture", "Coding Challenge", "System Design"
    val question: String,
    val contextReason: String,
    val keyPointsToCover: List<String>,
    val codeSnippet: String = "",
    val isPracticed: Boolean = false
)

@Entity(tableName = "applications")
data class ApplicationRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val company: String,
    val role: String,
    val location: String = "Hyderabad / Remote",
    val salary: String = "₹12 - ₹16 LPA",
    val status: ApplicationStatus = ApplicationStatus.APPLIED,
    val matchScore: Int = 84,
    val appliedDate: String = "Today",
    val jobMatchId: Long? = null,
    val coverLetter: String = "",
    val tailoredResume: String = "",
    val notes: String = "",
    val checklistItems: List<String> = listOf("Customized resume generated", "Cover letter tailored", "Tech keywords verified"),
    val interviewQuestions: List<InterviewQuestionItem> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
