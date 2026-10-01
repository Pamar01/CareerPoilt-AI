package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResumeFitBreakdown
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary

@Composable
fun FitScoreCard(
    score: Int,
    breakdown: ResumeFitBreakdown,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val scoreColor = when {
        score >= 80 -> EmeraldSuccess
        score >= 60 -> AmberWarning
        else -> CrimsonError
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("resume_fit_score_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ResumeFit™ Score",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Transparent Score",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Deterministic transparent scoring — not fake ATS hype",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Score Badge Circle
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(scoreColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreColor
                        )
                        Text(
                            text = "/100",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Score Category Bars
            ScoreBarRow(
                title = "Keyword Coverage",
                value = breakdown.keywordCoverage,
                max = breakdown.keywordCoverageMax,
                color = IndigoPrimary
            )
            ScoreBarRow(
                title = "Skills Alignment",
                value = breakdown.skillsAlignment,
                max = breakdown.skillsAlignmentMax,
                color = EmeraldSuccess
            )
            ScoreBarRow(
                title = "Experience Relevance",
                value = breakdown.experienceRelevance,
                max = breakdown.experienceRelevanceMax,
                color = MaterialTheme.colorScheme.secondary
            )
            ScoreBarRow(
                title = "Achievement Quality",
                value = breakdown.achievementQuality,
                max = breakdown.achievementQualityMax,
                color = AmberWarning
            )
            ScoreBarRow(
                title = "ATS Formatting",
                value = breakdown.atsFormatting,
                max = breakdown.atsFormattingMax,
                color = EmeraldSuccess
            )
            ScoreBarRow(
                title = "Clarity & Readability",
                value = breakdown.clarity,
                max = breakdown.clarityMax,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle Expand Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { expanded = !expanded }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (expanded) "Hide Detailed Score Breakdown" else "View Detailed Score Breakdown & Audit",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp)
                ) {
                    ScoreAuditItem("Keyword Coverage (${breakdown.keywordCoverage}/${breakdown.keywordCoverageMax})", breakdown.keywordCoverageDetails)
                    ScoreAuditItem("Skills Alignment (${breakdown.skillsAlignment}/${breakdown.skillsAlignmentMax})", breakdown.skillsAlignmentDetails)
                    ScoreAuditItem("Experience Relevance (${breakdown.experienceRelevance}/${breakdown.experienceRelevanceMax})", breakdown.experienceRelevanceDetails)
                    ScoreAuditItem("Achievement Quality (${breakdown.achievementQuality}/${breakdown.achievementQualityMax})", breakdown.achievementQualityDetails)
                    ScoreAuditItem("ATS Formatting (${breakdown.atsFormatting}/${breakdown.atsFormattingMax})", breakdown.atsFormattingDetails)
                    ScoreAuditItem("Clarity (${breakdown.clarity}/${breakdown.clarityMax})", breakdown.clarityDetails)
                }
            }
        }
    }
}

@Composable
private fun ScoreBarRow(
    title: String,
    value: Int,
    max: Int,
    color: Color
) {
    val progress = (value.toFloat() / max).coerceIn(0f, 1f)

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$value/$max",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun ScoreAuditItem(title: String, detail: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = detail,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
