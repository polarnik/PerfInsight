package com.jetbrains.perfinsights.snapshotanalyzer

import java.io.InputStream

/**
 * Analyzes performance degradation between baseline and degraded CSV snapshots.
 * Identifies product methods with the highest degradation and extracts their source files.
 */
class DegradationSourcesAnalyzer {

    /**
     * Analyzes baseline and degraded CSV snapshots to identify source files with the highest degradation.
     *
     * @param baselineInputStream The input stream of the baseline CSV snapshot
     * @param degradedInputStream The input stream of the degraded CSV snapshot
     * @return A map of method names to source file names for the top 3 methods with highest degradation
     */
    fun analyze(baselineInputStream: InputStream, degradedInputStream: InputStream): Map<String, String> {
        val baselineMethods = parseCSV(baselineInputStream)
        val degradedMethods = parseCSV(degradedInputStream)

        // Create maps of method name to time and own time for baseline methods
        val baselineMethodsMap = baselineMethods
            .filter { isProductMethod(it.name) }
            .associateBy({ it.name }, { it })

        // Calculate degradation for each method in degraded snapshot
        val methodDegradations = degradedMethods
            .filter { isProductMethod(it.name) }
            .mapNotNull { degradedMethod ->
                val baselineMethod = baselineMethodsMap[degradedMethod.name]

                // Skip methods where both baseline and degraded own times are 0
                if (baselineMethod?.ownTime == 0.0 && degradedMethod.ownTime == 0.0) {
                    return@mapNotNull null
                }

                val baselineTime = baselineMethod?.time ?: 0.0
                val baselineOwnTime = baselineMethod?.ownTime ?: 0.0
                val timeDegradation = degradedMethod.time - baselineTime
                val ownTimeDegradation = degradedMethod.ownTime - baselineOwnTime

                // Skip methods where own time degradation is less than 1% of baseline own time or less than 100 ms
                if (ownTimeDegradation < 100.0 || ownTimeDegradation < baselineOwnTime * 0.01) {
                    return@mapNotNull null
                }

                MethodDegradation(degradedMethod.name, timeDegradation, ownTimeDegradation, extractSourceFile(degradedMethod.name))
            }
            .sortedByDescending { it.degradation }

        // Create a map of method names to source file names for the top 3 methods with highest degradation
        return methodDegradations
            .take(3) // Only consider top 3 degraded methods
            .mapNotNull { methodDegradation ->
                methodDegradation.sourceFile?.let { sourceFile ->
                    methodDegradation.name to sourceFile
                }
            }
            .toMap()
    }

    /**
     * Parses a CSV file containing method profiling data.
     *
     * @param inputStream The input stream of the CSV file
     * @return A list of Method objects
     */
    private fun parseCSV(inputStream: InputStream): List<Method> {
        val methods = mutableListOf<Method>()

        inputStream.bufferedReader().useLines { lines ->
            // Skip header line
            lines.drop(1).forEach { line ->
                // Parse CSV line, handling quoted values
                val parts = parseCSVLine(line)
                if (parts.size >= 3) {
                    val methodName = parts[0].trim()
                    val timeMs = parts[1].trim().toDoubleOrNull() ?: 0.0
                    val ownTimeStr = parts[2].trim()
                    // Handle special cases like "< 0.1"
                    val ownTimeMs = when {
                        ownTimeStr.startsWith("<") -> 0.1 // Treat "< 0.1" as 0.1
                        else -> ownTimeStr.toDoubleOrNull() ?: 0.0
                    }
                    methods.add(Method(methodName, timeMs, ownTimeMs))
                }
            }
        }

        return methods
    }

    /**
     * Parses a CSV line, handling quoted values correctly.
     *
     * @param line The CSV line to parse
     * @return A list of values from the CSV line
     */
    private fun parseCSVLine(line: String): List<String> {
        val values = mutableListOf<String>()
        var currentValue = StringBuilder()
        var inQuotes = false

        for (char in line) {
            when {
                char == '"' -> inQuotes = !inQuotes
                char == ',' && !inQuotes -> {
                    values.add(currentValue.toString())
                    currentValue = StringBuilder()
                }
                else -> currentValue.append(char)
            }
        }

        // Add the last value
        values.add(currentValue.toString())

        return values
    }

    /**
     * Determines if a method is a product method (not a library method).
     * Product methods typically start with company-specific package names.
     *
     * @param methodName The full method name
     * @return true if it's a product method, false otherwise
     */
    private fun isProductMethod(methodName: String): Boolean {
        // Consider methods starting with "jetbrains." as product methods
        return methodName.startsWith("jetbrains.") && !methodName.startsWith("jetbrains.exodus")
    }

    /**
     * Extracts the source file name from a method signature.
     *
     * @param methodName The full method name including the source file
     * @return The source file name or null if not found
     */
    private fun extractSourceFile(methodName: String): String? {
        // Method format is typically: "package.class.method() SourceFile.ext"
        val parts = methodName.split(" ")
        return if (parts.size > 1) parts.last() else null
    }

    /**
     * Represents a method from the profiling data.
     *
     * @property name The full method name
     * @property time The execution time in milliseconds
     */
    private data class Method(val name: String, val time: Double, val ownTime: Double)

    /**
     * Represents a method with its degradation information.
     *
     * @property name The full method name
     * @property degradation The degradation in milliseconds (degraded - baseline)
     * @property ownTimeDegradation The own time degradation in milliseconds (degraded - baseline)
     * @property sourceFile The source file name
     */
    private data class MethodDegradation(val name: String, val degradation: Double, val ownTimeDegradation: Double, val sourceFile: String?)
}
