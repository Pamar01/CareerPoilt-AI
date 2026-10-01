package com.example.domain

import com.example.data.model.CareerProfile
import com.example.data.model.InterviewQuestionItem

enum class ResumeTemplate(val title: String, val subtitle: String) {
    ATS_CLASSIC("Template A: Classic ATS", "Maximum ATS readability, standard single-column layout"),
    MODERN_TECH("Template B: Modern Tech", "Emphasizes core backend stack, Docker & cloud metrics"),
    FRESH_GRADUATE("Template C: Fresh Graduate", "Highlights engineering degree, key projects & foundational skills"),
    EXECUTIVE_LEAD("Template D: Senior / Architect", "Focuses on systems architecture, optimization & business outcomes")
}

object ResumeGenerator {

    fun generateAtsMarkdown(
        profile: CareerProfile,
        targetRole: String = "Software Engineer",
        targetCompany: String = "",
        template: ResumeTemplate = ResumeTemplate.MODERN_TECH
    ): String {
        val sb = StringBuilder()

        // Header
        sb.append("# ${profile.fullName.uppercase()}\n")
        sb.append("${targetRole} | ${profile.location}\n")
        sb.append("${profile.email} • ${profile.phone} • ${profile.linkedin} • ${profile.github}\n\n")
        sb.append("---\n\n")

        // Summary
        sb.append("## PROFESSIONAL SUMMARY\n")
        sb.append("${profile.summary}\n\n")

        // Technical Skills
        sb.append("## TECHNICAL SKILLS\n")
        val skillsByCategory = profile.skills.groupBy { it.category }
        skillsByCategory.forEach { (cat, list) ->
            sb.append("• **$cat:** ${list.joinToString(", ") { it.name }}\n")
        }
        sb.append("\n")

        when (template) {
            ResumeTemplate.FRESH_GRADUATE -> {
                // Education First
                appendEducation(sb, profile)
                appendProjects(sb, profile)
                appendExperience(sb, profile)
                appendCertifications(sb, profile)
            }
            ResumeTemplate.ATS_CLASSIC, ResumeTemplate.MODERN_TECH, ResumeTemplate.EXECUTIVE_LEAD -> {
                // Experience First, then Projects, then Education
                appendExperience(sb, profile)
                appendProjects(sb, profile)
                appendEducation(sb, profile)
                appendCertifications(sb, profile)
            }
        }

        return sb.toString()
    }

    private fun appendExperience(sb: StringBuilder, profile: CareerProfile) {
        if (profile.experiences.isNotEmpty()) {
            sb.append("## WORK EXPERIENCE\n")
            profile.experiences.forEach { exp ->
                sb.append("### ${exp.title} — ${exp.company}\n")
                sb.append("*${exp.location} | ${exp.duration}*\n")
                exp.bullets.forEach { b ->
                    sb.append("• $b\n")
                }
                sb.append("\n")
            }
        }
    }

    private fun appendProjects(sb: StringBuilder, profile: CareerProfile) {
        if (profile.projects.isNotEmpty()) {
            sb.append("## FEATURED TECHNICAL PROJECTS\n")
            profile.projects.forEach { proj ->
                sb.append("### ${proj.name} | ${proj.role}\n")
                sb.append("**Tech Stack:** ${proj.techStack.joinToString(", ")}\n")
                sb.append("*${proj.description}*\n")
                proj.highlights.forEach { h ->
                    sb.append("• $h\n")
                }
                sb.append("\n")
            }
        }
    }

    private fun appendEducation(sb: StringBuilder, profile: CareerProfile) {
        if (profile.educations.isNotEmpty()) {
            sb.append("## EDUCATION\n")
            profile.educations.forEach { edu ->
                sb.append("• **${edu.degree}** — ${edu.institution} (${edu.yearRange})")
                if (edu.gpaOrScore.isNotEmpty()) {
                    sb.append(" | Grade: ${edu.gpaOrScore}")
                }
                sb.append("\n")
            }
            sb.append("\n")
        }
    }

    private fun appendCertifications(sb: StringBuilder, profile: CareerProfile) {
        if (profile.certifications.isNotEmpty()) {
            sb.append("## CERTIFICATIONS & TRAINING\n")
            profile.certifications.forEach { cert ->
                sb.append("• **${cert.name}** — ${cert.issuer} (${cert.year})\n")
            }
            sb.append("\n")
        }
    }

    fun generateCoverLetter(
        profile: CareerProfile,
        targetRole: String,
        targetCompany: String,
        missingSkills: List<String>
    ): String {
        val company = if (targetCompany.isNotBlank()) targetCompany else "Hiring Team"
        return """
Dear Hiring Manager at $company,

I am writing to express my strong enthusiasm for the $targetRole position at $company. With a proven foundation in backend systems architecture, asynchronous API development using Python & FastAPI, and relational database tuning with PostgreSQL, I am confident in my ability to immediately contribute to your engineering initiatives.

In my recent project, MetricsTrail, I spearheaded the backend architecture of a distributed API uptime monitoring system. By implementing FastAPI's async paradigms alongside PostgreSQL connection pooling and Redis caching, I designed a resilient service capable of monitoring hundreds of live endpoints with minimal latency. Similarly, my practical experience at NexStream Software focused on building scalable RESTful microservices and writing optimized SQL schemas.

What particularly attracts me to $company is your commitment to high-reliability engineering. While I have broad experience in Python, REST APIs, and Docker containerization, I am actively expanding my capabilities with hands-on practice in ${if (missingSkills.isNotEmpty()) missingSkills.joinToString(", ") else "cloud infrastructure (AWS) and automated CI/CD workflows"}.

I welcome the opportunity to discuss how my technical skills, proactive problem-solving mindset, and dedication to high-quality code can support $company's engineering goals.

Thank you for your time and consideration.

Warm regards,
${profile.fullName}
${profile.email} | ${profile.phone}
${profile.linkedin}
        """.trimIndent()
    }

    fun generateDefaultInterviewQuestions(profile: CareerProfile, targetRole: String): List<InterviewQuestionItem> {
        return listOf(
            InterviewQuestionItem(
                id = "iq-1",
                category = "Project Architecture",
                question = "Walk me through the architecture of your MetricsTrail project. Why did you choose FastAPI over Flask or Django?",
                contextReason = "Directly based on your featured project 'MetricsTrail' listed on your resume.",
                keyPointsToCover = listOf(
                    "Native async/await support with Starlette & Uvicorn for handling concurrent I/O-bound probes.",
                    "Pydantic data validation with automatic OpenAPI/Swagger documentation generation.",
                    "Fast execution benchmarks compared to standard synchronous WSGI frameworks."
                )
            ),
            InterviewQuestionItem(
                id = "iq-2",
                category = "Technical Deep Dive",
                question = "How do you handle database connection pooling and prevent bottlenecks in PostgreSQL under high traffic?",
                contextReason = "Matches target role's core requirement: PostgreSQL & Database optimization.",
                keyPointsToCover = listOf(
                    "Using asyncpg with connection pool limits (min/max size) tailored to server core counts.",
                    "Utilizing EXPLAIN ANALYZE on query execution plans to identify sequential table scans.",
                    "Applying composite indexes and caching frequent read-only lookups in Redis."
                )
            ),
            InterviewQuestionItem(
                id = "iq-3",
                category = "Technical Deep Dive",
                question = "Explain how Docker containerization was configured for your Celery background workers.",
                contextReason = "Matches required skills: Docker & Background asynchronous processing.",
                keyPointsToCover = listOf(
                    "Multi-stage Docker build to keep production image size under 150MB.",
                    "Docker Compose orchestration separating web API, Redis broker, and Celery workers.",
                    "Handling graceful shutdown signals (SIGTERM) to allow in-flight tasks to finish."
                )
            ),
            InterviewQuestionItem(
                id = "iq-4",
                category = "Coding Challenge",
                question = "Two Sum / Valid Parentheses in Python",
                contextReason = "Top coding assessment question asked in 80%+ of backend software interviews.",
                keyPointsToCover = listOf(
                    "Two Sum: Use a Hash Map (dictionary) to store complements in O(n) time and O(n) space.",
                    "Valid Parentheses: Use a Stack LIFO data structure in O(n) time to match open/closing brackets."
                ),
                codeSnippet = """
def two_sum(nums: list[int], target: int) -> list[int]:
    lookup = {}
    for i, num in enumerate(nums):
        diff = target - num
        if diff in lookup:
            return [lookup[diff], i]
        lookup[num] = i
    return []
                """.trimIndent()
            ),
            InterviewQuestionItem(
                id = "iq-5",
                category = "System Design",
                question = "Design an idempotent API endpoint for payment processing or critical webhook callbacks.",
                contextReason = "Evaluates production engineering maturity and reliability.",
                keyPointsToCover = listOf(
                    "Accepting client-generated Idempotency-Key in HTTP headers.",
                    "Storing request status and cached response in Redis with TTL.",
                    "Using database transactions (SERIALIZABLE or row locking) to prevent double charge."
                )
            )
        )
    }
}
