package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.data.model.ClarifyingQuestion
import com.example.data.model.InvestigationSession
import com.example.data.model.RootCauseTree
import com.example.data.model.SolutionComparison
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    fun generatePdfReport(context: Context, session: InvestigationSession, userEmail: String): File {
        val reportDir = File(context.cacheDir, "reports")
        if (!reportDir.exists()) reportDir.mkdirs()

        val fileName = "RootCause_Investigation_${session.id}_${System.currentTimeMillis()}.pdf"
        val pdfFile = File(reportDir, fileName)

        val pdfDoc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 standard size (595 x 842 pt)
        val page = pdfDoc.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.parseColor("#0F2537") // Navy
            textSize = 18f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
        }

        val subtitlePaint = Paint().apply {
            color = Color.parseColor("#0284C7") // Accent Cyan/Sky
            textSize = 10f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
        }

        val sectionHeaderPaint = Paint().apply {
            color = Color.parseColor("#0F172A") // Charcoal
            textSize = 12f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
        }

        val bodyBoldPaint = Paint().apply {
            color = Color.parseColor("#1E293B")
            textSize = 9f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = Color.parseColor("#334155")
            textSize = 9f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
        }

        val metaPaint = Paint().apply {
            color = Color.parseColor("#64748B")
            textSize = 8f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.parseColor("#CBD5E1")
            strokeWidth = 1f
        }

        var y = 40f
        val left = 40f
        val right = 555f

        // Header Banner Bar
        val bannerPaint = Paint().apply { color = Color.parseColor("#0F2537") }
        canvas.drawRect(left, y, right, y + 4f, bannerPaint)
        y += 24f

        canvas.drawText("ROOTCAUSE AI", left, y, titlePaint)
        canvas.drawText("EXECUTIVE BUSINESS DIAGNOSTIC REPORT", left + 170f, y - 2f, subtitlePaint)
        y += 16f

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm z", Locale.getDefault())
        val dateStr = dateFormat.format(Date(session.updatedAt))
        canvas.drawText("Category: ${session.category}  |  Session ID: #${session.id}  |  Date: $dateStr  |  Analyst: $userEmail", left, y, metaPaint)
        y += 12f
        canvas.drawLine(left, y, right, y, linePaint)
        y += 18f

        // Section 1: Problem Intake
        canvas.drawText("1. INTAKE SUMMARY & STATED SYMPTOM", left, y, sectionHeaderPaint)
        y += 14f

        canvas.drawText("Title: ${session.title.ifBlank { "Untitled Diagnostic" }}", left, y, bodyBoldPaint)
        y += 12f

        val descLines = wrapText(session.problemDescription, bodyPaint, (right - left))
        for (line in descLines.take(3)) {
            canvas.drawText(line, left, y, bodyPaint)
            y += 11f
        }
        if (session.uploadedDocumentText.isNotBlank()) {
            val docSummary = "Supporting Evidence: ${session.uploadedFileName.ifBlank { "Document Attached" }} (${session.uploadedDocumentText.length} chars analyzed)"
            canvas.drawText(docSummary, left, y, metaPaint)
            y += 12f
        }
        y += 6f
        canvas.drawLine(left, y, right, y, linePaint)
        y += 16f

        // Section 2: Clarifying Questions & Findings
        val qList = mutableListOf<ClarifyingQuestion>()
        try {
            val qArr = JSONArray(if (session.clarifyingQuestionsJson.isNotBlank()) session.clarifyingQuestionsJson else "[]")
            for (i in 0 until qArr.length()) {
                qList.add(ClarifyingQuestion.fromJson(qArr.getJSONObject(i)))
            }
        } catch (_: Exception) {}

        if (qList.isNotEmpty()) {
            canvas.drawText("2. CLARIFYING EVIDENCE & OPERATIONAL ANSWERS", left, y, sectionHeaderPaint)
            y += 14f
            for (q in qList.take(3)) {
                val qLine = wrapText("• ${q.question}", bodyBoldPaint, (right - left))
                for (ql in qLine.take(1)) {
                    canvas.drawText(ql, left, y, bodyBoldPaint)
                    y += 10f
                }
                val ans = if (q.answer.isNotBlank()) q.answer else "No specific operational constraint provided."
                val aLine = wrapText("  Response: $ans", bodyPaint, (right - left))
                for (al in aLine.take(2)) {
                    canvas.drawText(al, left, y, bodyPaint)
                    y += 10f
                }
                y += 2f
            }
            y += 4f
            canvas.drawLine(left, y, right, y, linePaint)
            y += 16f
        }

        // Section 3: Root Cause Tree
        val tree = RootCauseTree.fromJsonString(session.rootCauseTreeJson)
        if (tree.branches.isNotEmpty()) {
            canvas.drawText("3. ROOT-CAUSE INVESTIGATION BREAKDOWN", left, y, sectionHeaderPaint)
            y += 14f

            for (branch in tree.branches.take(4)) {
                val confTag = "[${branch.confidence.name}]"
                val tagColor = when (branch.confidence.name) {
                    "CONFIRMED" -> Color.parseColor("#059669")
                    "LIKELY" -> Color.parseColor("#D97706")
                    else -> Color.parseColor("#7C3AED")
                }
                val confPaint = Paint().apply {
                    color = tagColor
                    textSize = 8.5f
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                }

                canvas.drawText("$confTag (${branch.category})", left, y, confPaint)
                canvas.drawText(" ${branch.title}", left + 90f, y, bodyBoldPaint)
                y += 10f

                val evText = "Evidence: ${branch.evidenceReason}"
                val evLines = wrapText(evText, bodyPaint, (right - left - 15f))
                for (el in evLines.take(1)) {
                    canvas.drawText("  $el", left, y, bodyPaint)
                    y += 10f
                }
                y += 3f
            }
            y += 4f
            canvas.drawLine(left, y, right, y, linePaint)
            y += 16f
        }

        // Section 4: Solutions Comparison
        val solutions = mutableListOf<SolutionComparison>()
        try {
            val sArr = JSONArray(if (session.solutionsJson.isNotBlank()) session.solutionsJson else "[]")
            for (i in 0 until sArr.length()) {
                solutions.add(SolutionComparison.fromJson(sArr.getJSONObject(i)))
            }
        } catch (_: Exception) {}

        if (solutions.isNotEmpty()) {
            canvas.drawText("4. SOLUTIONS COMPARISON MATRIX", left, y, sectionHeaderPaint)
            y += 14f
            for (sol in solutions.take(3)) {
                canvas.drawText("• ${sol.solutionTitle}", left, y, bodyBoldPaint)
                y += 10f
                val metaText = "Cost: ${sol.estimatedCost}  |  Timeframe: ${sol.estimatedTimeframe}  |  Risk: ${sol.riskLevel}  |  Impact: ${sol.expectedImpact}"
                canvas.drawText("  $metaText", left, y, subtitlePaint)
                y += 11f
            }
            y += 4f
            canvas.drawLine(left, y, right, y, linePaint)
            y += 16f
        }

        // Section 5: Executive Recommendation
        if (session.recommendation.isNotBlank()) {
            canvas.drawText("5. FINAL EXECUTIVE RECOMMENDATION", left, y, sectionHeaderPaint)
            y += 14f
            val recLines = wrapText(session.recommendation, bodyPaint, (right - left))
            for (rl in recLines.take(6)) {
                canvas.drawText(rl, left, y, bodyPaint)
                y += 10f
            }
            y += 8f
        }

        // Mandatory Disclaimer Box
        val boxPaint = Paint().apply {
            color = Color.parseColor("#F1F5F9")
            style = Paint.Style.FILL
        }
        val boxBorderPaint = Paint().apply {
            color = Color.parseColor("#94A3B8")
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        canvas.drawRect(left, y, right, y + 36f, boxPaint)
        canvas.drawRect(left, y, right, y + 36f, boxBorderPaint)

        val disclaimerBold = Paint().apply {
            color = Color.parseColor("#0F172A")
            textSize = 7.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        val disclaimerText = Paint().apply {
            color = Color.parseColor("#334155")
            textSize = 7.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        canvas.drawText("GOVERNANCE & VALIDATION NOTICE:", left + 8f, y + 14f, disclaimerBold)
        canvas.drawText("This recommendation is based on the information provided and requires validation by someone with direct operational knowledge before action is taken.", left + 8f, y + 26f, disclaimerText)

        // Footer
        val footY = 820f
        canvas.drawLine(left, footY - 10f, right, footY - 10f, linePaint)
        canvas.drawText("CONFIDENTIAL — GENERATED BY ROOTCAUSE AI DECISION SUPPORT PLATFORM", left, footY, metaPaint)
        canvas.drawText("PAGE 1 OF 1", right - 50f, footY, metaPaint)

        pdfDoc.finishPage(page)

        FileOutputStream(pdfFile).use { out ->
            pdfDoc.writeTo(out)
        }
        pdfDoc.close()

        return pdfFile
    }

    fun sharePdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "RootCause AI Diagnostic Report")
            putExtra(Intent.EXTRA_TEXT, "Attached is the executive root-cause investigation report.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "Share RootCause AI Diagnostic Report")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val clean = text.replace("\n", " ").replace("###", "").replace("**", "")
        val words = clean.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = paint.measureText(testLine)
            if (width > maxWidth) {
                if (currentLine.isNotEmpty()) lines.add(currentLine.toString())
                currentLine = StringBuilder(word)
            } else {
                currentLine = StringBuilder(testLine)
            }
        }
        if (currentLine.isNotEmpty()) lines.add(currentLine.toString())
        return lines
    }
}
