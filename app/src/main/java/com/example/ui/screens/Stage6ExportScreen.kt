package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClarifyingQuestion
import com.example.data.model.InvestigationSession
import com.example.data.model.RootCauseTree
import com.example.data.model.SolutionComparison
import com.example.ui.components.ConfidenceBadge
import com.example.ui.theme.ConfirmedGreen
import com.example.ui.theme.EnterpriseAccent
import com.example.ui.theme.EnterpriseBackground
import com.example.ui.theme.EnterpriseBorder
import com.example.ui.theme.EnterpriseNavy
import com.example.ui.theme.EnterpriseNavyDark
import com.example.ui.theme.EnterpriseSurface
import com.example.ui.theme.EnterpriseTextMuted
import com.example.ui.theme.EnterpriseTextSecondary
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Stage6ExportScreen(
    session: InvestigationSession?,
    clarifyingQuestions: List<ClarifyingQuestion>,
    tree: RootCauseTree,
    solutions: List<SolutionComparison>,
    recommendation: String,
    lastExportedFile: File?,
    onExportPdf: () -> Unit,
    onStartNew: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                            text = "6",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Stage 6: Investigation Export & Formal Report",
                            color = EnterpriseNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Generate and share an executive-grade PDF diagnostic dossier.",
                            color = EnterpriseTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PDF Generation Call-to-Action Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseNavyDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Executive Diagnostic PDF Report",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Standard A4 layout with evidence audit, confidence tree & governance notice",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onExportPdf,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EnterpriseAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("export_pdf_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Export & Share PDF",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val summary = buildSummaryText(session, clarifyingQuestions, tree, solutions, recommendation)
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("RootCause Summary", summary))
                            Toast.makeText(context, "Investigation copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569)),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Text", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Document Report Preview
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EnterpriseBorder, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DOSSIER PREVIEW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseTextMuted
                    )
                    Text(
                        text = "STATUS: COMPLETE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ConfirmedGreen
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = session?.title?.ifBlank { "Operational Diagnostic Investigation" } ?: "Operational Diagnostic Investigation",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )

                val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(session?.updatedAt ?: System.currentTimeMillis()))
                Text(
                    text = "Category: ${session?.category ?: "Operations"}  •  Date: $dateStr  •  ID: #${session?.id ?: 1}",
                    fontSize = 11.sp,
                    color = EnterpriseTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = EnterpriseBorder)
                Spacer(modifier = Modifier.height(14.dp))

                // Section 1
                Text(
                    text = "1. INTAKE PROBLEM STATEMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session?.problemDescription ?: "None",
                    fontSize = 12.sp,
                    color = EnterpriseNavyDark,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = EnterpriseBorder)
                Spacer(modifier = Modifier.height(14.dp))

                // Section 2
                Text(
                    text = "2. ROOT-CAUSE DECOMPOSITION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                tree.branches.forEach { b ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "• ${b.title}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = EnterpriseNavy,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        ConfidenceBadge(confidence = b.confidence)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = EnterpriseBorder)
                Spacer(modifier = Modifier.height(14.dp))

                // Section 3
                Text(
                    text = "3. EVALUATED SOLUTIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                solutions.forEach { s ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = "• ${s.solutionTitle}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EnterpriseNavy
                        )
                        Text(
                            text = "Cost: ${s.estimatedCost} | Timeframe: ${s.estimatedTimeframe} | Risk: ${s.riskLevel} | Impact: ${s.expectedImpact}",
                            fontSize = 11.sp,
                            color = EnterpriseAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = EnterpriseBorder)
                Spacer(modifier = Modifier.height(14.dp))

                // Section 4
                Text(
                    text = "4. EXECUTIVE DIRECTIVE & GOVERNANCE CLAUSE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = recommendation.ifBlank { "Directive ready for operational review." },
                    fontSize = 12.sp,
                    color = EnterpriseNavyDark,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.4f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = EnterpriseNavy
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Recommendation", color = EnterpriseNavy, fontSize = 12.sp)
            }

            Button(
                onClick = onStartNew,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnterpriseNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.6f)
                    .height(48.dp)
                    .testTag("start_new_investigation_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("New Diagnostic", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun buildSummaryText(
    session: InvestigationSession?,
    questions: List<ClarifyingQuestion>,
    tree: RootCauseTree,
    solutions: List<SolutionComparison>,
    recommendation: String
): String {
    val sb = StringBuilder()
    sb.appendLine("==================================================")
    sb.appendLine("ROOTCAUSE AI — EXECUTIVE BUSINESS DIAGNOSTIC REPORT")
    sb.appendLine("==================================================")
    sb.appendLine("Title: ${session?.title}")
    sb.appendLine("Category: ${session?.category}")
    sb.appendLine("Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}")
    sb.appendLine()
    sb.appendLine("1. STATED SYMPTOM:")
    sb.appendLine(session?.problemDescription)
    sb.appendLine()
    sb.appendLine("2. CLARIFYING EVIDENCE:")
    questions.forEach { q ->
        sb.appendLine("Q: ${q.question}")
        sb.appendLine("A: ${q.answer.ifBlank { "Not provided" }}")
    }
    sb.appendLine()
    sb.appendLine("3. ROOT CAUSE BREAKDOWN:")
    tree.branches.forEach { b ->
        sb.appendLine("[${b.confidence.name}] (${b.category}) ${b.title}")
        sb.appendLine("Evidence: ${b.evidenceReason}")
    }
    sb.appendLine()
    sb.appendLine("4. SOLUTIONS COMPARISON:")
    solutions.forEach { s ->
        sb.appendLine("• ${s.solutionTitle} (Target: ${s.rootCauseAddressed})")
        sb.appendLine("  Cost: ${s.estimatedCost} | Time: ${s.estimatedTimeframe} | Risk: ${s.riskLevel} | Impact: ${s.expectedImpact}")
    }
    sb.appendLine()
    sb.appendLine("5. RECOMMENDATION & GOVERNANCE:")
    sb.appendLine(recommendation)
    sb.appendLine()
    sb.appendLine("NOTICE: This recommendation is based on the information provided and requires validation by someone with direct operational knowledge before action is taken.")
    return sb.toString()
}
