package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConfidenceLevel
import com.example.data.model.RootCauseNode
import com.example.data.model.RootCauseTree
import com.example.ui.components.ConfidenceBadge
import com.example.ui.theme.ConfirmedGreen
import com.example.ui.theme.EnterpriseAccent
import com.example.ui.theme.EnterpriseAccentSoft
import com.example.ui.theme.EnterpriseBackground
import com.example.ui.theme.EnterpriseBorder
import com.example.ui.theme.EnterpriseNavy
import com.example.ui.theme.EnterpriseNavyDark
import com.example.ui.theme.EnterpriseSlateBlue
import com.example.ui.theme.EnterpriseSurface
import com.example.ui.theme.EnterpriseTextMuted
import com.example.ui.theme.EnterpriseTextSecondary
import com.example.ui.theme.LikelyAmber
import com.example.ui.theme.SpeculativePurple

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Stage3InvestigationScreen(
    tree: RootCauseTree,
    confidenceFilter: ConfidenceLevel?,
    categoryFilter: String?,
    isLoading: Boolean,
    onConfidenceFilterChange: (ConfidenceLevel?) -> Unit,
    onCategoryFilterChange: (String?) -> Unit,
    onBack: () -> Unit,
    onProceedToSolutions: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allBranches = tree.branches
    val filteredBranches = allBranches.filter { branch ->
        val matchesConfidence = confidenceFilter == null || branch.confidence == confidenceFilter
        val matchesCategory = categoryFilter == null || branch.category.equals(categoryFilter, ignoreCase = true)
        matchesConfidence && matchesCategory
    }

    val confirmedCount = allBranches.count { it.confidence == ConfidenceLevel.CONFIRMED }
    val likelyCount = allBranches.count { it.confidence == ConfidenceLevel.LIKELY }
    val speculativeCount = allBranches.count { it.confidence == ConfidenceLevel.SPECULATIVE }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EnterpriseBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Stage Header
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EnterpriseBorder, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(EnterpriseAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "3",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Stage 3: Root-Cause Investigation Tree",
                            color = EnterpriseNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Evidence-weighted decomposition. Stated symptom down to contributing causal branches.",
                            color = EnterpriseTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Confidence Summary Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryMetricPill(
                        label = "CONFIRMED",
                        count = confirmedCount,
                        color = ConfirmedGreen,
                        isSelected = confidenceFilter == ConfidenceLevel.CONFIRMED,
                        onClick = {
                            onConfidenceFilterChange(
                                if (confidenceFilter == ConfidenceLevel.CONFIRMED) null else ConfidenceLevel.CONFIRMED
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricPill(
                        label = "LIKELY",
                        count = likelyCount,
                        color = LikelyAmber,
                        isSelected = confidenceFilter == ConfidenceLevel.LIKELY,
                        onClick = {
                            onConfidenceFilterChange(
                                if (confidenceFilter == ConfidenceLevel.LIKELY) null else ConfidenceLevel.LIKELY
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricPill(
                        label = "SPECULATIVE",
                        count = speculativeCount,
                        color = SpeculativePurple,
                        isSelected = confidenceFilter == ConfidenceLevel.SPECULATIVE,
                        onClick = {
                            onConfidenceFilterChange(
                                if (confidenceFilter == ConfidenceLevel.SPECULATIVE) null else ConfidenceLevel.SPECULATIVE
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Tree Top Node: Stated Symptom
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = EnterpriseNavyDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("root_symptom_node")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = EnterpriseAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "STATED SYMPTOM (OBSERVED ANOMALY)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EnterpriseAccent
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ROOT NODE",
                                color = Color(0xFF94A3B8),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tree.statedSymptom.ifBlank { "Operational symptom baseline" },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )
                }
            }

            // Visual Connecting Stem
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(24.dp)
                    .background(EnterpriseAccent)
            )

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(EnterpriseAccent)
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(14.dp)
                    .background(EnterpriseAccent)
            )
        }

        // Filter Bar (Category Chips)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = EnterpriseTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Category:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EnterpriseNavy
            )
            Spacer(modifier = Modifier.width(6.dp))

            val categories = listOf("All", "People", "Process", "Equipment", "Materials", "Environment", "Management")
            categories.forEach { cat ->
                val isSelected = (cat == "All" && categoryFilter == null) || (categoryFilter.equals(cat, ignoreCase = true))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) EnterpriseNavy else EnterpriseSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) EnterpriseNavy else EnterpriseBorder),
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clickable { onCategoryFilterChange(if (cat == "All") null else cat) }
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else EnterpriseTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual Branch Nodes
        if (filteredBranches.isEmpty()) {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, EnterpriseBorder, RoundedCornerShape(10.dp))
                    .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = EnterpriseTextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No branches match active filter.",
                        color = EnterpriseNavy,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Clear filters above to view all root-cause branches.",
                        color = EnterpriseTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        } else {
            filteredBranches.forEachIndexed { index, branch ->
                BranchNodeCard(
                    branch = branch,
                    index = index + 1,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.35f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = EnterpriseNavy
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Clarify", color = EnterpriseNavy, fontSize = 13.sp)
            }

            Button(
                onClick = onProceedToSolutions,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnterpriseNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.65f)
                    .height(48.dp)
                    .testTag("proceed_to_stage_4_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Formulating Solutions...", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("Evaluate Solutions (Stage 4)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SummaryMetricPill(
    label: String,
    count: Int,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) color else Color(0xFFE2E8F0)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = EnterpriseNavy
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BranchNodeCard(
    branch: RootCauseNode,
    index: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, EnterpriseBorder, RoundedCornerShape(10.dp))
            .testTag("branch_node_${branch.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Badge + Confidence Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(EnterpriseSlateBlue)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = branch.category.uppercase(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BRANCH #$index",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseTextMuted
                    )
                }

                ConfidenceBadge(confidence = branch.confidence)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Branch Title
            Text(
                text = branch.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EnterpriseNavy,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Evidence Citation Container
            val isUnverified = branch.evidenceReason.contains("no supporting data provided", ignoreCase = true)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isUnverified) Color(0xFFFDF4FF) else Color(0xFFF0FDF4))
                    .border(
                        1.dp,
                        if (isUnverified) Color(0xFFF5D0FE) else Color(0xFFBBF7D0),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Column {
                    Text(
                        text = if (isUnverified) "EVIDENCE STATUS: INFERENCE ONLY" else "EVIDENCE CITATION (DATA-BACKED):",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnverified) SpeculativePurple else ConfirmedGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = branch.evidenceReason,
                        fontSize = 11.sp,
                        color = EnterpriseNavyDark,
                        lineHeight = 15.sp
                    )
                }
            }

            // Sub factors if available
            if (branch.subFactors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Contributing Sub-elements:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    branch.subFactors.forEach { sub ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "• $sub",
                                fontSize = 10.sp,
                                color = EnterpriseNavy,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
