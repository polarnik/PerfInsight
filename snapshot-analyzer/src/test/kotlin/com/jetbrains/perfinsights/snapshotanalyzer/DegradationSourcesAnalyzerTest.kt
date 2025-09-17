package com.jetbrains.perfinsights.snapshotanalyzer

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import java.io.File

class DegradationSourcesAnalyzerTest {

    @Test
    fun `test analyze identifies source files with highest degradation`() {
        // Arrange
        val analyzer = DegradationSourcesAnalyzer()
        val baselineFile = File("src/test/resources/Method-list-CPU_baseline.csv")
        val degradedFile = File("src/test/resources/Method-list-CPU_degradation.csv")

        // Act
        val methodToSourceFile = analyzer.analyze(
            baselineFile.inputStream(),
            degradedFile.inputStream()
        )

        // Assert
        // Verify we have results
        assertTrue(methodToSourceFile.isNotEmpty(), "Should return method to source file mappings")

        // Print the method to source file mappings for debugging
        println("Method to source file mappings:")
        methodToSourceFile.entries.forEachIndexed { index, entry ->
            println("${index + 1}. ${entry.key} -> ${entry.value}")
        }

        // Verify we have exactly 3 or fewer entries
        assertTrue(methodToSourceFile.size <= 3, "Should return at most 3 method to source file mappings")

        // Verify the results contain Kotlin/Java files
        val kotlinFiles = methodToSourceFile.values.filter { it.endsWith(".kt") }

        assertTrue(kotlinFiles.isNotEmpty(), "Should contain Kotlin files")

        // Verify some specific files we expect to see in the results
        // These are examples from the top degraded methods in the CSV files
        val expectedFiles = listOf(
            "SearchTreeUtil.kt",
            "ReadOnlyDelegates.kt",
            "DocumentFeaturesProviders.kt"
        )

        expectedFiles.forEach { expectedFile ->
            assertTrue(
                methodToSourceFile.values.any { it.endsWith(expectedFile) },
                "Results should contain $expectedFile"
            )
        }
    }

    @Test
    fun `test analyze filters out library methods`() {
        // Arrange
        val analyzer = DegradationSourcesAnalyzer()
        val baselineFile = File("src/test/resources/Method-list-CPU_baseline.csv")
        val degradedFile = File("src/test/resources/Method-list-CPU_degradation.csv")

        // Act
        val methodToSourceFile = analyzer.analyze(
            baselineFile.inputStream(),
            degradedFile.inputStream()
        )

        // Assert
        // Verify we don't have java.* or org.* source files (library methods)
        val libraryFiles = methodToSourceFile.values.filter {
            it.startsWith("java.") || it.startsWith("org.") || it.startsWith("com.fasterxml")
        }

        assertEquals(0, libraryFiles.size, "Should not contain library source files")
    }

    @Test
    fun `test analyze limits results to top 3 degraded methods`() {
        // Arrange
        val analyzer = DegradationSourcesAnalyzer()
        val baselineFile = File("src/test/resources/Method-list-CPU_baseline.csv")
        val degradedFile = File("src/test/resources/Method-list-CPU_degradation.csv")

        // Act
        val methodToSourceFile = analyzer.analyze(
            baselineFile.inputStream(),
            degradedFile.inputStream()
        )

        // Assert
        println("[DEBUG_LOG] Total number of method to source file mappings: ${methodToSourceFile.size}")

        // Verify we have exactly 3 or fewer entries
        assertTrue(methodToSourceFile.size <= 3,
            "Should return at most 3 method to source file mappings (got ${methodToSourceFile.size})")

        // Also verify that we have at least some results
        assertTrue(methodToSourceFile.isNotEmpty(), "Should return at least some method to source file mappings")
    }

    @Test
    fun `test analyze excludes methods with own time 0 in both baseline and degraded`() {
        // Arrange
        val analyzer = DegradationSourcesAnalyzer()
        val baselineFile = File("src/test/resources/Method-list-CPU_baseline.csv")
        val degradedFile = File("src/test/resources/Method-list-CPU_degradation.csv")

        // Act
        val methodToSourceFile = analyzer.analyze(
            baselineFile.inputStream(),
            degradedFile.inputStream()
        )

        // Assert
        // These files are from methods with own time 0 in both baseline and degraded snapshots
        val excludedFiles = listOf(
            "CloudForwardFilter.kt",
            "CloudRedirectRootRequestFilter.kt",
            "CorsRequestFilter.kt",
            "SitemapFilter.kt"
        )

        excludedFiles.forEach { excludedFile ->
            assertFalse(
                methodToSourceFile.values.any { it.endsWith(excludedFile) },
                "Results should not contain $excludedFile (method with own time 0 in both baseline and degraded)"
            )
        }
    }

    @Test
    fun `test analyze excludes methods with own time degradation less than 1 percent of baseline own time`() {
        // Arrange
        val analyzer = DegradationSourcesAnalyzer()
        val baselineFile = File("src/test/resources/Method-list-CPU_baseline.csv")
        val degradedFile = File("src/test/resources/Method-list-CPU_degradation.csv")

        // Act
        val methodToSourceFile = analyzer.analyze(
            baselineFile.inputStream(),
            degradedFile.inputStream()
        )

        // Assert
        // These files are from methods with own time degradation less than 1% of baseline own time
        val excludedFiles = listOf(
            "MdcProviderServletFilter.kt", // Own time: 24ms in baseline, 0ms in degraded, degradation: -24ms (< 1% of 24ms)
            "BaseApplicationFilter.kt"     // Own time: 8ms in baseline, 0ms in degraded, degradation: -8ms (< 1% of 8ms)
            // HttpContextAccessorFilter.kt is not excluded because baseline own time is 0, so 1% threshold doesn't apply
            // FilterAdapter.kt is not excluded because degradation (4ms) > 1% of baseline (0.12ms)
        )

        excludedFiles.forEach { excludedFile ->
            assertFalse(
                methodToSourceFile.values.any { it.endsWith(excludedFile) },
                "Results should not contain $excludedFile (method with own time degradation less than 1% of baseline own time)"
            )
        }
    }
}
