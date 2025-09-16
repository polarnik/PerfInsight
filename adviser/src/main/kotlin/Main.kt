package com.jetbrains

import kotlinx.coroutines.runBlocking
import java.nio.file.Paths

/**
 * Main entry point for the Performance Adviser Agent.
 *
 * Usage: java -jar adviser.jar <functionName> <sourceFolder>
 */
fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: java -jar adviser.jar <functionName> <sourceFolder>")
        return
    }

    val functionName = args[0]
    val sourceFolder = Paths.get(args[1])

    println("Starting Performance Adviser Agent...")
    println("This agent will analyze the function '$functionName' in '$sourceFolder' and provide performance improvement suggestions.")

    // Use the new PerformanceAdviserAgent implementation
    val adviser = PerformanceAdviserAgent()
    val suggestions = runBlocking {
        adviser.analyzeFunction(functionName, sourceFolder)
    }

    if (suggestions.isEmpty()) {
        println("No performance improvement suggestions found for function '$functionName'")
    } else {
        println("\nPerformance Improvement Suggestions for function '$functionName':")
        suggestions.forEachIndexed { index, suggestion ->
            println("${index + 1}. $suggestion")
        }
    }
}
