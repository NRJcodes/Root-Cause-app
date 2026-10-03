package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ConfidenceLevel
import com.example.data.model.ProblemCategory
import com.example.data.model.RootCauseNode
import com.example.data.model.RootCauseTree
import com.example.data.model.SolutionComparison
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RootCause AI", appName)
    }

    @Test
    fun `root cause tree serialization and parsing`() {
        val node1 = RootCauseNode(
            id = "rc_1",
            title = "Thermal calibration drift on robot 4",
            category = "Equipment",
            confidence = ConfidenceLevel.CONFIRMED,
            evidenceReason = "Cited in telemetry logs showing 4.2Nm torque deviation",
            subFactors = listOf("Nozzle tip wear", "Sensor drift")
        )
        val tree = RootCauseTree(
            statedSymptom = "Assembly Line 3 Yield Collapse",
            branches = listOf(node1)
        )

        val jsonStr = tree.toJsonString()
        val parsed = RootCauseTree.fromJsonString(jsonStr)

        assertEquals("Assembly Line 3 Yield Collapse", parsed.statedSymptom)
        assertEquals(1, parsed.branches.size)
        assertEquals(ConfidenceLevel.CONFIRMED, parsed.branches[0].confidence)
        assertEquals("Equipment", parsed.branches[0].category)
        assertEquals(2, parsed.branches[0].subFactors.size)
    }

    @Test
    fun `solution comparison model mapping`() {
        val sol = SolutionComparison(
            id = "sol_1",
            rootCauseAddressed = "Thermal calibration drift",
            solutionTitle = "Automated Real-time Sensor Interlock",
            description = "Halt cycling if torque exceeds 3.9Nm.",
            estimatedCost = "Low",
            estimatedTimeframe = "1-2 Weeks",
            riskLevel = "Low",
            expectedImpact = "High"
        )
        val json = sol.toJson()
        val restored = SolutionComparison.fromJson(json)

        assertEquals("sol_1", restored.id)
        assertEquals("Low", restored.estimatedCost)
        assertEquals("High", restored.expectedImpact)
    }

    @Test
    fun `problem category mapping helper`() {
        assertEquals(ProblemCategory.PRODUCTION_OPERATIONS, ProblemCategory.fromLabel("Production/Operations"))
        assertEquals(ProblemCategory.FINANCIAL, ProblemCategory.fromLabel("Financial"))
        assertEquals(ProblemCategory.SUPPLY_CHAIN, ProblemCategory.fromLabel("Supply Chain"))
        assertEquals(ProblemCategory.QUALITY, ProblemCategory.fromLabel("Quality"))
        assertEquals(ProblemCategory.STAFFING_HR, ProblemCategory.fromLabel("Staffing/HR"))
        assertEquals(ProblemCategory.OTHER, ProblemCategory.fromLabel("Unknown"))
    }
}
