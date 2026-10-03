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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.ConfirmedGreen
import com.example.ui.theme.EnterpriseAccent
import com.example.ui.theme.EnterpriseBackground
import com.example.ui.theme.EnterpriseBorder
import com.example.ui.theme.EnterpriseNavy
import com.example.ui.theme.EnterpriseNavyDark
import com.example.ui.theme.EnterpriseSurface
import com.example.ui.theme.EnterpriseTextSecondary

@Composable
fun Stage5RecommendationScreen(
    recommendationText: String,
    onBack: () -> Unit,
    onProceedToExport: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                            text = "5",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Stage 5: Final Executive Recommendation",
                            color = EnterpriseNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Singular directive path forward with operational justification and governance safeguards.",
                            color = EnterpriseTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Executive Recommendation Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EnterpriseBorder, RoundedCornerShape(12.dp))
                .testTag("recommendation_content_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        tint = ConfirmedGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EXECUTIVE DIRECTIVE & RATIONALE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseNavy
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = recommendationText.ifBlank {
                        "Recommendation synthesis in progress. Please review previous stages."
                    },
                    fontSize = 13.sp,
                    color = EnterpriseNavyDark,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mandatory Human Validation Callout Box
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                .testTag("mandatory_governance_clause_box")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = EnterpriseNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MANDATORY OPERATIONAL GOVERNANCE CLAUSE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseNavy
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "This recommendation is based on the information provided and requires validation by someone with direct operational knowledge before action is taken.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EnterpriseNavyDark,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Automated analytical models cannot replace physical shop-floor verification, real-time safety certifications, or contextual domain expertise.",
                    fontSize = 10.sp,
                    color = EnterpriseTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                Text("Solutions", color = EnterpriseNavy, fontSize = 13.sp)
            }

            Button(
                onClick = onProceedToExport,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnterpriseNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.65f)
                    .height(48.dp)
                    .testTag("proceed_to_stage_6_button")
            ) {
                Text("Review & Export Report (Stage 6)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
