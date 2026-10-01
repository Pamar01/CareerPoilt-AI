package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.ResumeFixSuggestion
import com.example.ui.components.FitScoreCard
import com.example.ui.components.FixResumeCard
import com.example.ui.components.RequirementEvidenceRow
import com.example.ui.components.ResumePreviewCard
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.MainUiState

@Composable
fun JobMatchScreen(
    uiState: MainUiState,
    onUpdateJdInput: (String, String, String, String) -> Unit,
    onLoadSample: (Int) -> Unit,
    onRunAnalysis: () -> Unit,
    onApplyFix: (String) -> Unit,
    onSaveToCrm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCoverLetter by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("job_match_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // JD Input Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Job Match Engine",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Paste a target Job Description to compare required skills, responsibilities, and experience evidence.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sample selector chips
                    Text(
                        text = "Quick Sample Jobs:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = { onLoadSample(0) },
                            label = { Text("NexCorp (FastAPI/Postgres)") },
                            modifier = Modifier.testTag("sample_chip_0")
                        )
                        SuggestionChip(
                            onClick = { onLoadSample(1) },
                            label = { Text("CloudScale (Python Dev)") },
                            modifier = Modifier.testTag("sample_chip_1")
                        )
                        SuggestionChip(
                            onClick = { onLoadSample(2) },
                            label = { Text("Global Insights (Cloud API)") },
                            modifier = Modifier.testTag("sample_chip_2")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.activeJobTitle,
                            onValueChange = {
                                onUpdateJdInput(it, uiState.activeCompanyName, uiState.activeLocation, uiState.activeJdText)
                            },
                            label = { Text("Job Title") },
                            modifier = Modifier.weight(1f).testTag("job_title_input"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = uiState.activeCompanyName,
                            onValueChange = {
                                onUpdateJdInput(uiState.activeJobTitle, it, uiState.activeLocation, uiState.activeJdText)
                            },
                            label = { Text("Company") },
                            modifier = Modifier.weight(1f).testTag("company_name_input"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.activeJdText,
                        onValueChange = {
                            onUpdateJdInput(uiState.activeJobTitle, uiState.activeCompanyName, uiState.activeLocation, it)
                        },
                        label = { Text("Paste Job Description (JD)") },
                        modifier = Modifier.fillMaxWidth().testTag("jd_textarea"),
                        minLines = 4,
                        maxLines = 8
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRunAnalysis,
                        enabled = !uiState.isAnalyzing && uiState.activeJdText.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().testTag("run_analysis_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        if (uiState.isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing JD & Matching Profile...")
                        } else {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Run Job Match & ResumeFit Analysis")
                        }
                    }
                }
            }
        }

        // Active Analysis Results
        val match = uiState.currentMatch
        if (match != null) {
            // 1. ResumeFit Score Component
            item {
                FitScoreCard(
                    score = match.fitScore,
                    breakdown = match.fitBreakdown
                )
            }

            // Quick Add to CRM Action Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = IndigoPrimary.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Save to Application CRM",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = IndigoPrimary
                            )
                            Text(
                                text = "Track pipeline, custom resume, and interview prep checklist.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = {
                                onSaveToCrm()
                                Toast.makeText(context, "Saved application to CRM!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier.testTag("save_to_crm_button")
                        ) {
                            Text("Track")
                        }
                    }
                }
            }

            // 2. Requirement vs Candidate Evidence Table
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Match Breakdown & Evidence (${match.criteria.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Exact Profile Proof",
                        style = MaterialTheme.typography.labelSmall,
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(match.criteria) { criterion ->
                RequirementEvidenceRow(criterion = criterion)
            }

            // 3. "Fix My Resume" Actionable Suggestions
            if (match.fixes.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IndigoPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Fix My Resume (Truthful AI Refinements)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Rule: Never invent experience. Only optimize phrasing, verify unearned skills, and elevate metrics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(match.fixes) { fix ->
                    FixResumeCard(
                        fix = fix,
                        onApply = { onApplyFix(it.id) }
                    )
                }
            }

            // 4. 14-Day Skill Gap Learning Plan
            if (match.learningPlan.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Skill Gap → 14-Day Career Acceleration Plan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Focus on closing critical gaps (AWS, CI/CD) most requested by target employers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(match.learningPlan) { planItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = IndigoPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = planItem.dayRange,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = planItem.topic,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = planItem.focusArea,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Action: ${planItem.actionItem}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldSuccess,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // 5. Tailored ATS Resume Generator
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ResumePreviewCard(
                    profile = uiState.profile,
                    targetRole = match.jobTitle,
                    targetCompany = match.companyName
                )
            }

            // 6. Tailored Cover Letter Accordion
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Tailored Cover Letter",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Highlighting MetricsTrail, FastAPI & self-learning roadmap",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { showCoverLetter = !showCoverLetter }) {
                                Icon(
                                    imageVector = if (showCoverLetter) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null
                                )
                            }
                        }

                        AnimatedVisibility(visible = showCoverLetter) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = match.tailoredCoverLetter,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
