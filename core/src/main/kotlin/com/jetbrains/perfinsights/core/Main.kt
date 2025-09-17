package com.jetbrains.perfinsights.core

import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess
import com.jetbrains.perfinsights.snapshotanalyzer.DegradationSourcesAnalyzer
import com.jetbrains.PerformanceAdviserAgent

/**
 * Main entry point for the PerfInsight application.
 * This application accepts two CSV files (baseline and degradation),
 * calculates degraded methods using the preprocessor module,
 * and passes the results to the adviser module for analysis.
 *
 * Note: This is a placeholder implementation that demonstrates the intended functionality.
 * The actual implementation would use the preprocessor and adviser modules directly.
 */
fun main(args: Array<String>): Unit = runBlocking {
    // Check if the correct number of arguments is provided
    if (args.size != 2) {
        println("Usage: java -jar perfinsight.jar <baseline-csv-file> <degradation-csv-file>")
        exitProcess(1)
    }

    val baselineFile = File(args[0])
    val degradationFile = File(args[1])

    // Validate input files
    if (!baselineFile.exists() || !baselineFile.isFile) {
        println("Error: Baseline file does not exist or is not a file: ${baselineFile.absolutePath}")
        exitProcess(1)
    }

    if (!degradationFile.exists() || !degradationFile.isFile) {
        println("Error: Degradation file does not exist or is not a file: ${degradationFile.absolutePath}")
        exitProcess(1)
    }

    println("Processing baseline file: ${baselineFile.absolutePath}")
    println("Processing degradation file: ${degradationFile.absolutePath}")

    try {
        // Use the preprocessor module to analyze the CSV files
        val analyzer = DegradationSourcesAnalyzer()
        val methodToSourceFile = analyzer.analyze(
            baselineFile.inputStream(),
            degradationFile.inputStream()
        )

        println("Found ${methodToSourceFile.size} degraded methods:")
        methodToSourceFile.forEach { (method, sourceFile) ->
            println("  $method -> $sourceFile")
        }

        // Get the samples directory path
        val sourcesDir = getSourcesDir()
        println("Looking for source files in: $sourcesDir")

        // In a real implementation, we would use the adviser module to analyze the methods
        // For now, we'll just simulate the result
        methodToSourceFile.forEach { (methodName, sourceFileName) ->
            println("\nAnalyzing method: $methodName in file: $sourceFileName")

            try {
                // Extract the function name from the method signature
                val functionName = extractFunctionName(methodName)

                // Create an instance of the PerformanceAdviserAgent
                val agent = PerformanceAdviserAgent()

                // Call the analyzeFunction method with the function name and source directory
                val suggestions = agent.analyzeFunction(functionName, sourcesDir)

                // Display the results
                println("Performance suggestions for $functionName:")
                if (suggestions.isNotEmpty()) {
                  println("\nPerformance Improvement Suggestions for function '$functionName':")
                  println("\n====================== $functionName ===========================")
                  println(suggestions)
                } else {
                    println("No suggestions found.")
                }
            } catch (e: Exception) {
                println("Error analyzing method $methodName: ${e.message}")
                e.printStackTrace()
            }
        }

    } catch (e: Exception) {
        println("Error processing files: ${e.message}")
        e.printStackTrace()
        exitProcess(1)
    }
}

/**
 * Gets the path to the sources directory.
 */
private fun getSourcesDir(): Path {
    val samplesDir = Paths.get("core/src/main/resources/sourcecodes")

    // Create the directory if it doesn't exist
    val samplesDirFile = samplesDir.toFile()
    if (!samplesDirFile.exists()) {
        samplesDirFile.mkdirs()
    }

    return samplesDir
}

/**
 * Extracts the function name from a method signature.
 * Example: "jetbrains.youtrack.api.workflow.configuration.EntityChangeHandler.invoke() EntityChangeHandler.kt"
 * Returns: "invoke"
 */
private fun extractFunctionName(methodSignature: String): String {
    // Method format is typically: "package.class.method() SourceFile.ext"
    val methodPart = methodSignature.substringBefore(" ")
    val functionName = methodPart.substringAfterLast(".")

    // Remove parentheses and any parameters
    return functionName.substringBefore("(")
}
