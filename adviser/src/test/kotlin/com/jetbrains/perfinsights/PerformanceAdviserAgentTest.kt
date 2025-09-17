package com.jetbrains.perfinsights

import com.jetbrains.PerformanceAdviserAgent
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.fail
import java.nio.file.Paths

class PerformanceAdviserAgentTest {

    @Test
    fun `test analyzeFunction returns suggestions when function is found`() {
        // Fail test if GRAZIE_TOKEN is not set
        val grazieToken = System.getenv("GRAZIE_TOKEN")
        if (grazieToken.isNullOrBlank()) {
            fail("GRAZIE_TOKEN environment variable is not set. This test requires AI analysis.")
        }

        // Arrange
        val agent = PerformanceAdviserAgent()
        val functionName = "processData"
        val sourceFolder = Paths.get("src/test/resources/examination")

        // Act
        val suggestions = runBlocking {
            agent.analyzeFunction(functionName, sourceFolder)
        }

        // Assert
        assertNotNull(suggestions, "Suggestions should not be null")
        assertFalse(suggestions.isBlank(), "Suggestions should not be blank")

        // Print the suggestions for debugging
        println("Performance suggestions:")
        println(suggestions)
    }

    @Test
    fun `test analyzeFunction returns empty string when function is not found`() {
        // Fail test if GRAZIE_TOKEN is not set
        val grazieToken = System.getenv("GRAZIE_TOKEN")
        if (grazieToken.isNullOrBlank()) {
            fail("GRAZIE_TOKEN environment variable is not set. This test requires AI analysis.")
        }

        // Arrange
        val agent = PerformanceAdviserAgent()
        val functionName = "nonExistentFunction"
        val sourceFolder = Paths.get("src/test/resources/examination")

        // Act
        val suggestions = runBlocking {
            agent.analyzeFunction(functionName, sourceFolder)
        }

        // Assert
        assertTrue(suggestions.isEmpty(), "Should return empty string for non-existent function")
    }
}
