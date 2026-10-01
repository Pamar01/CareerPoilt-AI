package com.example.data.repository

import com.example.data.local.ApplicationDao
import com.example.data.local.CareerProfileDao
import com.example.data.local.JobMatchDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.*

class CareerRepository(
    private val profileDao: CareerProfileDao,
    private val matchDao: JobMatchDao,
    private val appDao: ApplicationDao
) {
    val profile: Flow<CareerProfile?> = profileDao.getProfile()
    val allMatches: Flow<List<JobMatchRecord>> = matchDao.getAllMatches()
    val allApplications: Flow<List<ApplicationRecord>> = appDao.getAllApplications()

    suspend fun ensureInitialProfile() {
        val existing = profile.firstOrNull()
        if (existing == null) {
            profileDao.insertOrUpdateProfile(createDefaultProfile())
        }
    }

    suspend fun saveProfile(profile: CareerProfile) {
        profileDao.insertOrUpdateProfile(profile)
    }

    suspend fun resetToDefaultProfile() {
        profileDao.insertOrUpdateProfile(createDefaultProfile())
    }

    suspend fun saveJobMatch(match: JobMatchRecord): Long {
        return matchDao.insertMatch(match)
    }

    suspend fun deleteJobMatch(id: Long) {
        matchDao.deleteMatchById(id)
    }

    suspend fun addApplication(application: ApplicationRecord): Long {
        return appDao.insertApplication(application)
    }

    suspend fun updateApplication(application: ApplicationRecord) {
        appDao.updateApplication(application)
    }

    suspend fun deleteApplication(id: Long) {
        appDao.deleteApplicationById(id)
    }

    fun createDefaultProfile(): CareerProfile {
        return CareerProfile(
            id = 1,
            fullName = "Amar Reddy",
            email = "amarreddy1143@gmail.com",
            phone = "+91 98765 43210",
            location = "Hyderabad, India",
            linkedin = "linkedin.com/in/amarreddy",
            github = "github.com/amarreddy",
            portfolio = "amarreddy.dev",
            summary = "Backend Software Engineer specializing in Python, FastAPI, PostgreSQL, and microservices architecture. Experienced in designing low-latency REST APIs, caching with Redis, containerization with Docker, and building resilient data pipelines.",
            targetRoles = listOf("Software Engineer", "Backend Developer", "Python Developer", "Cloud API Engineer"),
            preferredLocations = listOf("Hyderabad", "Bengaluru", "Remote"),
            targetSalary = "₹8 - ₹14 LPA",
            educations = listOf(
                Education(
                    id = "edu-1",
                    degree = "B.Tech in Electrical & Electronics Engineering",
                    institution = "JNTUH College of Engineering",
                    yearRange = "2020 - 2024",
                    gpaOrScore = "8.4 CGPA"
                )
            ),
            experiences = listOf(
                WorkExperience(
                    id = "exp-1",
                    title = "Backend Developer Intern",
                    company = "NexStream Software",
                    location = "Hyderabad, India",
                    duration = "Jan 2024 - Jun 2024",
                    bullets = listOf(
                        "Developed asynchronous RESTful endpoints using Python, FastAPI, and Pydantic, improving average response latency by 32%.",
                        "Architected database schemas and optimized SQL queries on PostgreSQL with SQLAlchemy ORM, handling over 150k daily active requests.",
                        "Implemented Redis caching layer for frequent catalog queries, reducing primary database load by 40%."
                    )
                )
            ),
            projects = listOf(
                ProjectItem(
                    id = "proj-1",
                    name = "MetricsTrail",
                    role = "Lead Backend Architect",
                    description = "Asynchronous distributed website uptime & API monitoring service with automated alerts and live telemetry dashboard.",
                    techStack = listOf("Python", "FastAPI", "PostgreSQL", "Docker", "Redis", "Celery", "REST API"),
                    highlights = listOf(
                        "Designed an asynchronous FastAPI-based website monitoring service using PostgreSQL and Redis, implementing REST APIs and automated health checks.",
                        "Integrated Dockerized background workers using Celery for distributed health probe pings across 500+ endpoints.",
                        "Engineered role-based JWT authentication and real-time webhook dispatching on incident detection."
                    )
                ),
                ProjectItem(
                    id = "proj-2",
                    name = "Fraud Detection System",
                    role = "Backend Developer",
                    description = "Real-time payment transaction anomaly detector identifying suspicious checkout patterns.",
                    techStack = listOf("Python", "PostgreSQL", "REST API", "Docker", "Git"),
                    highlights = listOf(
                        "Built a rule-based transaction scoring engine evaluating transactions under 50ms latency threshold.",
                        "Configured PostgreSQL indexed audit tables for fraud classification logs and automated chargeback flagging.",
                        "Containerized service using Docker and implemented automated unit test suite with 90% code coverage."
                    )
                ),
                ProjectItem(
                    id = "proj-3",
                    name = "Job Application Automation",
                    role = "Developer",
                    description = "Intelligent application workflow engine that synchronizes candidate profiles with target job requirements.",
                    techStack = listOf("Python", "FastAPI", "SQL", "Git", "REST API"),
                    highlights = listOf(
                        "Automated application pipeline tracking and resume keyword alignment parsing.",
                        "Implemented structured JSON output generation for rapid ATS template generation."
                    )
                )
            ),
            skills = listOf(
                SkillItem(name = "Python", category = "Programming", level = "Expert"),
                SkillItem(name = "SQL", category = "Programming", level = "Expert"),
                SkillItem(name = "Java", category = "Programming", level = "Familiar"),
                SkillItem(name = "FastAPI", category = "Backend & APIs", level = "Expert"),
                SkillItem(name = "Django", category = "Backend & APIs", level = "Proficient"),
                SkillItem(name = "REST API", category = "Backend & APIs", level = "Expert"),
                SkillItem(name = "Pydantic", category = "Backend & APIs", level = "Expert"),
                SkillItem(name = "PostgreSQL", category = "Databases", level = "Expert"),
                SkillItem(name = "MySQL", category = "Databases", level = "Proficient"),
                SkillItem(name = "Redis", category = "Databases", level = "Proficient"),
                SkillItem(name = "Docker", category = "Cloud & DevOps", level = "Proficient"),
                SkillItem(name = "Git", category = "Tools", level = "Expert"),
                SkillItem(name = "Celery", category = "Backend & APIs", level = "Proficient"),
                SkillItem(name = "Linux", category = "Tools", level = "Proficient")
            ),
            certifications = listOf(
                CertificationItem(
                    id = "cert-1",
                    name = "Meta Backend Developer Professional Certificate",
                    issuer = "Coursera / Meta",
                    year = "2024"
                ),
                CertificationItem(
                    id = "cert-2",
                    name = "PostgreSQL High Performance Query Tuning",
                    issuer = "Udemy",
                    year = "2024"
                )
            )
        )
    }
}
