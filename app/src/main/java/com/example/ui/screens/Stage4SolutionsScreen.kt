package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.TableChart
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
import com.example.data.model.SolutionComparison
import com.example.ui.theme.ConfirmedGreen
import com.example.ui.theme.ConfirmedGreenBg
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedBg
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
import com.example.ui.theme.LikelyAmberBg

@Composable
fun Stage4SolutionsScreen(
    solutions: List<SolutionComparison>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onProceedToRecommendation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedByCause = solutions.groupBy { it.rootCauseAddressed }

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
                            text = "4",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Stage 4: Solutions Comparison Matrix",
                            color = EnterpriseNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Countermeasures formulated exclusively for CONFIRMED and LIKELY root causes.",
                            color = EnterpriseTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Solutions List Grouped by Root Cause
        if (solutions.isEmpty()) {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, EnterpriseBorder, RoundedCornerShape(10.dp))
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No solutions generated yet.",
                        color = EnterpriseNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Complete Stage 3 to evaluate interventions.",
                        color = EnterpriseTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            groupedByCause.forEach { (causeTitle, solList) ->
                // Cause Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(EnterpriseNavy)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = EnterpriseAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ROOT CAUSE: $causeTitle",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                solList.forEach { sol ->
                    SolutionCard(solution = sol, modifier = Modifier.padding(bottom = 12.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
                Text("Tree", color = EnterpriseNavy, fontSize = 13.sp)
            }

            Button(
                onClick = onProceedToRecommendation,
                enabled = !isLoading && solutions.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnterpriseNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.65f)
                    .height(48.dp)
                    .testTag("proceed_to_stage_5_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Formulating Directive...", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("Formulate Recommendation (Stage 5)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
fun SolutionCard(solution: SolutionComparison, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
        colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, EnterpriseBorder, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .testTag("solution_card_${solution.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = solution.solutionTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EnterpriseNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = solution.description,
                fontSize = 12.sp,
                color = EnterpriseNavyDark,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Multi-column parameters row (Cost, Timeframe, Risk, Impact)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, EnterpriseBorder, RoundedCornerShape(6.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricColumn(title = "EST. COST", value = solution.estimatedCost, color = EnterpriseNavy)
                MetricColumn(title = "TIMEFRAME", value = solution.estimatedTimeframe, color = EnterpriseNavy)
                MetricColumn(
                    title = "RISK LEVEL",
                    value = solution.riskLevel,
                    color = when (solution.riskLevel.lowercase()) {
                        "low" -> ConfirmedGreen
                        "medium" -> LikelyAmber
                        else -> DangerRed
                    }
                )
                MetricColumn(
                    title = "IMPACT",
                    value = solution.expectedImpact,
                    color = when (solution.expectedImpact.lowercase()) {
                        "high" -> ConfirmedGreen
                        "medium" -> EnterpriseAccent
                        else -> EnterpriseTextSecondary
                    }
                )
            }
        }
    }
}

@Composable
fun MetricColumn(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = EnterpriseTextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
