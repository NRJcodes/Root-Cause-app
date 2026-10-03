package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConfidenceLevel
import com.example.data.model.UserProfile
import com.example.ui.theme.ConfirmedGreen
import com.example.ui.theme.ConfirmedGreenBg
import com.example.ui.theme.ConfirmedGreenBorder
import com.example.ui.theme.EnterpriseAccent
import com.example.ui.theme.EnterpriseBorder
import com.example.ui.theme.EnterpriseNavy
import com.example.ui.theme.EnterpriseNavyDark
import com.example.ui.theme.EnterpriseTextMuted
import com.example.ui.theme.EnterpriseTextSecondary
import com.example.ui.theme.LikelyAmber
import com.example.ui.theme.LikelyAmberBg
import com.example.ui.theme.LikelyAmberBorder
import com.example.ui.theme.SpeculativePurple
import com.example.ui.theme.SpeculativePurpleBg
import com.example.ui.theme.SpeculativePurpleBorder

@Composable
fun TopEnterpriseBar(
    currentUser: UserProfile?,
    onNewDiagnosticClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = EnterpriseNavyDark,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(EnterpriseAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RC",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "RootCause AI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ENTERPRISE",
                                color = EnterpriseAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Business Diagnostic Support Tool",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.testTag("history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Investigation History",
                        tint = Color(0xFFCBD5E1)
                    )
                }

                Button(
                    onClick = onNewDiagnosticClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EnterpriseAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("new_diagnostic_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "New", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.testTag("profile_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile",
                        tint = Color(0xFFCBD5E1)
                    )
                }
            }
        }
    }
}

@Composable
fun StageProgressBar(
    currentStage: Int,
    maxCompletedStage: Int,
    onStageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        1 to "Intake",
        2 to "Clarify",
        3 to "Investigation",
        4 to "Solutions",
        5 to "Recommendation",
        6 to "Export"
    )

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, EnterpriseBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            stages.forEachIndexed { index, (stageNum, title) ->
                val isActive = currentStage == stageNum
                val isCompleted = currentStage > stageNum || maxCompletedStage >= stageNum
                val isClickable = stageNum <= maxCompletedStage + 1

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = isClickable) { onStageClick(stageNum) }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("stage_tab_$stageNum")
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isActive -> EnterpriseAccent
                                    isCompleted -> EnterpriseNavy
                                    else -> Color(0xFFE2E8F0)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted && !isActive) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "$stageNum",
                                color = if (isActive || isCompleted) Color.White else Color(0xFF64748B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isActive -> EnterpriseAccent
                            isCompleted -> EnterpriseNavy
                            else -> Color(0xFF94A3B8)
                        }
                    )
                }

                if (index < stages.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(2.dp)
                            .background(
                                if (stageNum < currentStage) EnterpriseNavy else Color(0xFFE2E8F0)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ConfidenceBadge(confidence: ConfidenceLevel, modifier: Modifier = Modifier) {
    val (bgColor, textColor, borderColor) = when (confidence) {
        ConfidenceLevel.CONFIRMED -> Triple(ConfirmedGreenBg, ConfirmedGreen, ConfirmedGreenBorder)
        ConfidenceLevel.LIKELY -> Triple(LikelyAmberBg, LikelyAmber, LikelyAmberBorder)
        ConfidenceLevel.SPECULATIVE -> Triple(SpeculativePurpleBg, SpeculativePurple, SpeculativePurpleBorder)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = confidence.name,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
