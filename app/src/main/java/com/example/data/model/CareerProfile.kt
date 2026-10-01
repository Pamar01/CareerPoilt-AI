package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "career_profile")
data class CareerProfile(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "Amar Reddy",
    val email: String = "amarreddy1143@gmail.com",
    val phone: String = "+91 98765 43210",
    val location: String = "Hyderabad, India",
    val linkedin: String = "linkedin.com/in/amarreddy",
    val github: String = "github.com/amarreddy",
    val portfolio: String = "amarreddy.dev",
    val summary: String = "Backend-focused Software Engineer skilled in Python, FastAPI, PostgreSQL, and scalable microservices. Passionate about designing resilient REST APIs and high-performance data systems.",
    val targetRoles: List<String> = listOf("Software Engineer", "Backend Developer", "Python Developer", "Cloud API Engineer"),
    val preferredLocations: List<String> = listOf("Hyderabad", "Bengaluru", "Remote"),
    val targetSalary: String = "₹12 - ₹18 LPA",
    val educations: List<Education> = emptyList(),
    val experiences: List<WorkExperience> = emptyList(),
    val projects: List<ProjectItem> = emptyList(),
    val skills: List<SkillItem> = emptyList(),
    val certifications: List<CertificationItem> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class Education(
    val id: String,
    val degree: String,
    val institution: String,
    val yearRange: String,
    val gpaOrScore: String
)

data class WorkExperience(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val duration: String,
    val bullets: List<String>
)

data class ProjectItem(
    val id: String,
    val name: String,
    val role: String,
    val description: String,
    val techStack: List<String>,
    val highlights: List<String>
)

data class SkillItem(
    val name: String,
    val category: String, // "Programming", "Backend & APIs", "Databases", "Cloud & DevOps", "Tools"
    val level: String = "Proficient" // "Expert", "Proficient", "Familiar"
)

data class CertificationItem(
    val id: String,
    val name: String,
    val issuer: String,
    val year: String
)
