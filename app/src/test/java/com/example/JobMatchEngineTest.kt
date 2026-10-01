package com.example

import com.example.data.model.CareerProfile
import com.example.data.model.MatchStatus
import com.example.data.model.TrustLevel
import com.example.domain.JobMatchEngine
import com.example.domain.ResumeGenerator
import org.junit.Assert.*
import org.junit.Test

class JobMatchEngineTest {

    @Test
    fun testJobMatchEngineCalculatesFitScoreAndExtractsCriteria() {
        val profile = CareerProfile()
        val jd = """
            Backend Engineer needed with Python, FastAPI, PostgreSQL, Docker, and REST APIs.
            Responsibilities: Develop scalable APIs, write automated tests, and optimize database queries.
        """.trimIndent()

        val match = JobMatchEngine.analyze(
            jobTitle = "Backend Engineer",
            companyName = "NexCorp",
            location = "Hyderabad",
            jobDescription = jd,
            profile = profile
        )

        // Verify score is reasonable and between 0 and 100
        assertTrue("Score should be > 50", match.fitScore > 50)
        assertTrue("Score should be <= 100", match.fitScore <= 100)

        // Verify all 6 category components exist
        val breakdown = match.fitBreakdown
        assertTrue(breakdown.keywordCoverage in 0..30)
        assertTrue(breakdown.skillsAlignment in 0..20)
        assertTrue(breakdown.experienceRelevance in 0..20)
        assertTrue(breakdown.achievementQuality in 0..15)
        assertEquals(10, breakdown.atsFormatting)
        assertEquals(5, breakdown.clarity)

        // Verify criteria extraction
        assertTrue(match.criteria.isNotEmpty())
        val pythonCriteria = match.criteria.find { it.title.equals("Python", ignoreCase = true) }
        assertNotNull("Python requirement should be detected", pythonCriteria)

        // Verify Fix My Resume suggestions
        assertTrue("Fixes should be generated", match.fixes.isNotEmpty())
        assertTrue("Known trust tag should exist for verifiable project", match.fixes.any { it.trustTag == TrustLevel.KNOWN })
    }

    @Test
    fun testResumeGeneratorMarkdownOutput() {
        val profile = CareerProfile()
        val markdown = ResumeGenerator.generateAtsMarkdown(profile, "Backend Developer", "Stripe")

        assertTrue(markdown.contains("AMAR REDDY"))
        assertTrue(markdown.contains("TECHNICAL SKILLS"))
        assertTrue(markdown.contains("PROFESSIONAL SUMMARY"))
    }
}
