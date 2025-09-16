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
        assertEquals(3, suggestions.size, "Should return exactly 3 suggestions")

        // Verify that each suggestion is not empty
        suggestions.forEach { suggestion ->
            assertFalse(suggestion.isBlank(), "Suggestion should not be blank")
        }

        // Print the suggestions for debugging
        println("Performance suggestions:")
        suggestions.forEachIndexed { index, suggestion ->
            println("${index + 1}. $suggestion")
        }
    }

    @Test
    fun `test analyzeFunction returns empty list when function is not found`() {
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
        assertTrue(suggestions.isEmpty(), "Should return empty list for non-existent function")
    }
}
