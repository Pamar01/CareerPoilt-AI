package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.remote.GeminiService
import com.example.data.repository.CareerRepository
import com.example.domain.JobMatchEngine
import com.example.domain.ResumeGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ScreenTab(val title: String) {
    DASHBOARD("Dashboard"),
    PROFILE("Career Profile"),
    JOB_MATCH("Job Match & Fit"),
    APPLICATIONS("App CRM"),
    INTERVIEW_PREP("Interview AI")
}

data class SampleJob(
    val title: String,
    val company: String,
    val location: String,
    val salary: String,
    val description: String
)

data class MainUiState(
    val profile: CareerProfile = CareerProfile(),
    val currentMatch: JobMatchRecord? = null,
    val matchesHistory: List<JobMatchRecord> = emptyList(),
    val applications: List<ApplicationRecord> = emptyList(),
    val currentTab: ScreenTab = ScreenTab.DASHBOARD,
    val isAnalyzing: Boolean = false,
    val activeJobTitle: String = "Backend Software Engineer",
    val activeCompanyName: String = "NexCorp FinTech",
    val activeLocation: String = "Hyderabad / Hybrid",
    val activeJdText: String = "",
    val interviewQuestions: List<InterviewQuestionItem> = emptyList(),
    val userNotification: String? = null
)

class CareerPilotViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CareerRepository

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    val sampleJobs = listOf(
        SampleJob(
            title = "Backend Software Engineer",
            company = "NexCorp FinTech",
            location = "Hyderabad / Hybrid",
            salary = "₹12 - ₹18 LPA",
            description = """
Role: Backend Software Engineer (Python & APIs)
About: We build low-latency payment processing pipelines.

Required Skills:
- Python 3.10+, FastAPI or Django
- Relational Databases: PostgreSQL, SQL schema design, query optimization
- REST API architecture, HTTP protocol, JWT authentication
- Docker containerization & microservices
- Version control with Git

Preferred Qualifications:
- Experience with Celery, Redis caching, or message brokers
- Basic cloud awareness with AWS (EC2, S3)
- Unit and integration testing with pytest
- CI/CD automated pipeline workflows

Responsibilities:
- Develop scalable asynchronous RESTful endpoints
- Write clean, maintainable unit tests
- Troubleshoot query latency bottlenecks
- Collaborate with frontend and DevOps engineers
            """.trimIndent()
        ),
        SampleJob(
            title = "Python / FastAPI Developer",
            company = "CloudScale Tech",
            location = "Bengaluru / Remote",
            salary = "₹10 - ₹15 LPA",
            description = """
We are looking for a Python Developer experienced in FastAPI, PostgreSQL, Docker, and REST APIs.
Key responsibilities:
- Build asynchronous backend endpoints and event triggers
- Architect robust database queries and migrations
- Implement automated test suites and uptime health probes
- Package services in Docker containers for cloud deployment
            """.trimIndent()
        ),
        SampleJob(
            title = "Cloud API Engineer",
            company = "Global Insights Corp",
            location = "Remote",
            salary = "₹14 - ₹20 LPA",
            description = """
Requirements:
- Strong programming background in Python, Java, or Node.js
- Experience designing REST APIs and working with SQL/NoSQL databases
- Docker, CI/CD pipeline automation, and cloud deployments on AWS or GCP
- Experience with performance monitoring and uptime telemetry
            """.trimIndent()
        )
    )

    init {
        val db = AppDatabase.getInstance(application)
        repository = CareerRepository(
            profileDao = db.careerProfileDao(),
            matchDao = db.jobMatchDao(),
            appDao = db.applicationDao()
        )

        // Seed initial sample JD
        _uiState.update { it.copy(activeJdText = sampleJobs[0].description) }

        viewModelScope.launch {
            repository.ensureInitialProfile()
        }

        // Collect profile
        viewModelScope.launch {
            repository.profile.collect { p ->
                if (p != null) {
                    _uiState.update { state ->
                        val initialQuestions = if (state.interviewQuestions.isEmpty()) {
                            ResumeGenerator.generateDefaultInterviewQuestions(p, state.activeJobTitle)
                        } else state.interviewQuestions
                        state.copy(profile = p, interviewQuestions = initialQuestions)
                    }
                }
            }
        }

        // Collect matches
        viewModelScope.launch {
            repository.allMatches.collect { matches ->
                _uiState.update { state ->
                    val cur = state.currentMatch ?: matches.firstOrNull()
                    state.copy(matchesHistory = matches, currentMatch = cur)
                }
            }
        }

        // Collect applications
        viewModelScope.launch {
            repository.allApplications.collect { apps ->
                _uiState.update { it.copy(applications = apps) }
                if (apps.isEmpty()) {
                    seedDefaultApplications()
                }
            }
        }
    }

    private suspend fun seedDefaultApplications() {
        val app1 = ApplicationRecord(
            company = "NexCorp FinTech",
            role = "Backend Software Engineer",
            location = "Hyderabad",
            salary = "₹12 - ₹16 LPA",
            status = ApplicationStatus.INTERVIEW,
            matchScore = 84,
            appliedDate = "3 days ago",
            notes = "Completed technical phone screen. Round 2 System & Architecture scheduled for Friday.",
            checklistItems = listOf("Resume customized for FastAPI/Postgres", "Cover letter sent", "Reviewed MetricsTrail architecture")
        )
        val app2 = ApplicationRecord(
            company = "CloudScale Tech",
            role = "Python / FastAPI Developer",
            location = "Bengaluru",
            salary = "₹10 - ₹15 LPA",
            status = ApplicationStatus.APPLIED,
            matchScore = 88,
            appliedDate = "Yesterday",
            notes = "Applied via CareerPilot tailored application flow.",
            checklistItems = listOf("Tailored resume generated", "Key skills validated")
        )
        val app3 = ApplicationRecord(
            company = "Global Insights Corp",
            role = "Cloud API Engineer",
            location = "Remote",
            salary = "₹14 - ₹20 LPA",
            status = ApplicationStatus.BOOKMARKED,
            matchScore = 79,
            appliedDate = "Today",
            notes = "High salary potential, check AWS requirements before submitting.",
            checklistItems = listOf("Evaluate AWS skill gap", "Complete Day 1-3 learning plan")
        )
        repository.addApplication(app1)
        repository.addApplication(app2)
        repository.addApplication(app3)
    }

    fun setScreenTab(tab: ScreenTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun loadSampleJob(sampleIndex: Int) {
        val sample = sampleJobs.getOrNull(sampleIndex) ?: return
        _uiState.update {
            it.copy(
                activeJobTitle = sample.title,
                activeCompanyName = sample.company,
                activeLocation = sample.location,
                activeJdText = sample.description
            )
        }
    }

    fun updateJdInput(title: String, company: String, location: String, jd: String) {
        _uiState.update {
            it.copy(
                activeJobTitle = title,
                activeCompanyName = company,
                activeLocation = location,
                activeJdText = jd
            )
        }
    }

    fun runJobMatchAnalysis() {
        val state = _uiState.value
        if (state.activeJdText.isBlank()) return

        _uiState.update { it.copy(isAnalyzing = true) }

        viewModelScope.launch {
            val match = JobMatchEngine.analyze(
                jobTitle = state.activeJobTitle,
                companyName = state.activeCompanyName,
                location = state.activeLocation,
                jobDescription = state.activeJdText,
                profile = state.profile
            )

            val matchId = repository.saveJobMatch(match)
            val savedMatch = match.copy(id = matchId)

            val updatedInterviewQuestions = ResumeGenerator.generateDefaultInterviewQuestions(state.profile, state.activeJobTitle)

            _uiState.update {
                it.copy(
                    currentMatch = savedMatch,
                    interviewQuestions = updatedInterviewQuestions,
                    isAnalyzing = false,
                    userNotification = "Job Match Engine complete: ResumeFit Score is ${savedMatch.fitScore}/100!"
                )
            }
        }
    }

    fun applyFixToResume(fixId: String) {
        val state = _uiState.value
        val match = state.currentMatch ?: return

        val updatedFixes = match.fixes.map { fix ->
            if (fix.id == fixId) fix.copy(isApplied = true) else fix
        }

        val updatedMatch = match.copy(fixes = updatedFixes)

        viewModelScope.launch {
            repository.saveJobMatch(updatedMatch)
            _uiState.update {
                it.copy(
                    currentMatch = updatedMatch,
                    userNotification = "Applied AI refinement into your tailored resume preview!"
                )
            }
        }
    }

    fun saveProfile(profile: CareerProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            _uiState.update {
                it.copy(
                    profile = profile,
                    userNotification = "Career Profile updated successfully."
                )
            }
        }
    }

    fun resetProfileToDefault() {
        viewModelScope.launch {
            repository.resetToDefaultProfile()
            _uiState.update {
                it.copy(userNotification = "Profile restored to comprehensive sample profile.")
            }
        }
    }

    fun addApplicationFromCurrentMatch() {
        val match = _uiState.value.currentMatch ?: return
        val app = ApplicationRecord(
            company = match.companyName,
            role = match.jobTitle,
            location = match.location,
            salary = "₹12 - ₹18 LPA",
            status = ApplicationStatus.APPLIED,
            matchScore = match.fitScore,
            appliedDate = "Today",
            jobMatchId = match.id,
            coverLetter = match.tailoredCoverLetter,
            tailoredResume = match.tailoredResumeMarkdown,
            notes = "Applied with ResumeFit ${match.fitScore}/100.",
            checklistItems = listOf("Customized resume generated", "Cover letter tailored", "Key skills verified")
        )

        viewModelScope.launch {
            repository.addApplication(app)
            _uiState.update {
                it.copy(userNotification = "Added ${match.companyName} to your Application CRM!")
            }
        }
    }

    fun updateApplicationStatus(appId: Long, newStatus: ApplicationStatus) {
        val currentApp = _uiState.value.applications.find { it.id == appId } ?: return
        val updated = currentApp.copy(status = newStatus)
        viewModelScope.launch {
            repository.updateApplication(updated)
            _uiState.update {
                it.copy(userNotification = "Updated status of ${currentApp.company} to ${newStatus.label}")
            }
        }
    }

    fun deleteApplication(appId: Long) {
        viewModelScope.launch {
            repository.deleteApplication(appId)
        }
    }

    fun toggleQuestionPracticed(questionId: String) {
        _uiState.update { state ->
            val updated = state.interviewQuestions.map {
                if (it.id == questionId) it.copy(isPracticed = !it.isPracticed) else it
            }
            state.copy(interviewQuestions = updated)
        }
    }

    fun clearNotification() {
        _uiState.update { it.copy(userNotification = null) }
    }
}
