package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProblemCategory
import com.example.ui.PresetScenario
import com.example.ui.theme.EnterpriseAccent
import com.example.ui.theme.EnterpriseAccentSoft
import com.example.ui.theme.EnterpriseBackground
import com.example.ui.theme.EnterpriseBorder
import com.example.ui.theme.EnterpriseNavy
import com.example.ui.theme.EnterpriseNavyDark
import com.example.ui.theme.EnterpriseSurface
import com.example.ui.theme.EnterpriseTextMuted
import com.example.ui.theme.EnterpriseTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Stage1IntakeScreen(
    category: ProblemCategory,
    title: String,
    problemDescription: String,
    docText: String,
    docFileName: String,
    isLoading: Boolean,
    onCategoryChange: (ProblemCategory) -> Unit,
    onTitleChange: (String) -> Unit,
    onProblemChange: (String) -> Unit,
    onDocTextChange: (String, String) -> Unit,
    onLoadPreset: (PresetScenario) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    val presets = listOf(
        PresetScenario(
            title = "Assembly Line 3 Yield Collapse",
            category = ProblemCategory.PRODUCTION_OPERATIONS,
            problemDescription = "Assembly Line 3 first-pass yield plummeted from 94.2% to 76.5% over the past 14 days, primarily isolated to the night shift operations. Scrap rates for micro-actuator units increased by $42,000 weekly.",
            documentSnippet = "[SHIFT LOG 09/24]\n- 02:15 AM: Thermal sensor error on pick-and-place robot #4.\n- Maintenance swapped nozzle tip with vendor B batch #882.\n- Torque calibration logged at 4.2Nm (standard is 3.8Nm +/- 0.1).\n- 2 new contract technicians deployed without standard 12-hour shadowing certification.",
            fileName = "line3_shift_telemetry_report.txt"
        ),
        PresetScenario(
            title = "28% Surge in Mid-Market SaaS Churn",
            category = ProblemCategory.FINANCIAL,
            problemDescription = "Net revenue retention dropped 9 points as 28% of mid-market tier enterprise accounts failed to renew at annual term. Customer success exit surveys cite delayed reporting and invoice reconciliation errors.",
            documentSnippet = "[FINANCIAL AUDIT Q3]\n- Billing engine migration executed July 15th.\n- Average invoice delivery latency: 19 days post-close (was 2 days).\n- Unresolved disputed chargebacks: 142 tickets pending in finance queue.\n- Account management headcount cut by 15% during Q2 reorg.",
            fileName = "q3_churn_and_billing_audit.txt"
        ),
        PresetScenario(
            title = "Cold-Chain Inventory Spoilage & Fulfillment Lag",
            category = ProblemCategory.SUPPLY_CHAIN,
            problemDescription = "Regional distribution center experienced a 22% delay in cold-chain pharmaceutical fulfillment, resulting in $185,000 in spoiled biologic inventory due to temperature breach during dock staging.",
            documentSnippet = "[SUPPLY CHAIN INCIDENT LOG]\n- Temperature logger #7 exceeded 8°C threshold for 3.4 consecutive hours.\n- Dock dwell time increased from 35 mins to 195 mins due to carrier scheduling mismatch.\n- Cross-dock tracking barcodes failed scanner read rate (paper thermal fade).",
            fileName = "coldchain_incident_telemetry.txt"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EnterpriseBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Card with generated visual banner
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EnterpriseBorder, RoundedCornerShape(12.dp))
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_rootcause_analytic_1791004103649),
                        contentDescription = "Enterprise Analytics Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, EnterpriseNavyDark.copy(alpha = 0.85f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Stage 1: Diagnostic Intake & Symptom Scoping",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Separate observed symptoms from unverified assumptions.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preset Scenarios Quick Loader
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EnterpriseBorder, RoundedCornerShape(10.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = EnterpriseAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sample Operational Scenarios (Quick Test)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseNavy
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EnterpriseAccentSoft,
                            border = androidx.compose.foundation.BorderStroke(1.dp, EnterpriseAccent.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable { onLoadPreset(preset) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = preset.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EnterpriseNavy
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Intake Form Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EnterpriseSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EnterpriseBorder, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Problem Category *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { categoryDropdownExpanded = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("category_dropdown_button")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.label,
                                color = EnterpriseNavy,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = EnterpriseTextSecondary
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        ProblemCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.label, fontSize = 14.sp) },
                                onClick = {
                                    onCategoryChange(cat)
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "2. Diagnostic Session Title",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    placeholder = { Text("e.g. Assembly Line 3 Yield Collapse", color = EnterpriseTextMuted, fontSize = 14.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("intake_title_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "3. Problem Description (Stated Symptom) *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnterpriseNavy
                )
                Text(
                    text = "Describe the observed business impact, frequency, affected units, or cost implications.",
                    fontSize = 11.sp,
                    color = EnterpriseTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = problemDescription,
                    onValueChange = onProblemChange,
                    placeholder = {
                        Text(
                            "Detail the symptom here... What occurred? Which operational KPI broke down? What is the measurable effect?",
                            color = EnterpriseTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    minLines = 4,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("intake_problem_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Document / Supporting Evidence Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = null,
                        tint = EnterpriseAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "4. Supporting Documents & Telemetry Evidence",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseNavy
                    )
                }
                Text(
                    text = "Paste text extracts from shift logs, audit reports, spreadsheets, error logs, or notes.",
                    fontSize = 11.sp,
                    color = EnterpriseTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = docText,
                    onValueChange = { onDocTextChange(it, docFileName) },
                    placeholder = {
                        Text(
                            "Paste raw operational logs, maintenance timestamps, sensor metrics, or vendor spec data...",
                            color = EnterpriseTextMuted,
                            fontSize = 12.sp
                        )
                    },
                    minLines = 4,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("intake_documents_input")
                )

                if (docFileName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = EnterpriseTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Attached: $docFileName",
                            fontSize = 11.sp,
                            color = EnterpriseNavy,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = onSubmit,
            enabled = !isLoading && problemDescription.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = EnterpriseNavy,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_stage_1_button")
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Analyzing Evidence...", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            } else {
                Text("Proceed to Stage 2: Clarifying Questions", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
