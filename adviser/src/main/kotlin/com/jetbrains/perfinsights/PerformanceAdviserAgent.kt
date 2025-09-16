package com.jetbrains

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import ai.jetbrains.code.prompt.executor.clients.grazie.koog.model.JetBrainsAIModels
import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.agent.entity.AIAgentStrategy
import ai.koog.agents.core.agent.entity.ToolSelectionStrategy
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.ext.agent.ProvideSubgraphResult
import ai.koog.agents.ext.agent.StringSubgraphResult
import ai.koog.agents.ext.agent.subgraphWithTask
import ai.koog.prompt.dsl.prompt
import com.jetbrains.utils.simpleGrazieExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.streams.asSequence


/**
 * Performance Adviser Agent
 *
 * This agent analyzes source code files and suggests ways to improve performance
 * of a specified function using the koog framework.
 */
class PerformanceAdviserAgent {
    // System prompt for the AI agent
    private val systemPrompt = """
        You are a performance optimization expert. Your task is to analyze Java and Kotlin code
        and provide specific, actionable suggestions to improve performance.
        Focus on identifying common performance issues such as:
        1. Inefficient algorithms and data structures
        2. Unnecessary object creation
        3. Suboptimal loop implementations
        4. Inefficient I/O operations
        5. Thread synchronization issues

        For each issue you identify, provide a clear explanation of:
        - What the problem is
        - Why it impacts performance
        - How to fix it with a specific code example

        Your suggestions should be practical, specific to the code being analyzed,
        and provide measurable performance improvements.
        """

    /**
     * Analyzes source code files and suggests performance improvements for a specified function.
     *
     * @param functionName The name of the function to analyze
     * @param sourceFolder The folder containing source files to analyze
     * @return A list of performance improvement suggestions
     */
    suspend fun analyzeFunction(functionName: String, sourceFolder: Path): List<String> {
        println("Analyzing function: $functionName in folder: $sourceFolder")

        // Get the Grazie token from environment variable
        val grazieToken = System.getenv("GRAZIE_TOKEN")
        if (grazieToken.isNullOrBlank()) {
            throw IllegalStateException("GRAZIE_TOKEN environment variable is not set. AI analysis is required.")
        }

        try {
            // Create the prompt executor
            val promptExecutor = simpleGrazieExecutor(token = grazieToken)

            // Create tool registry
            val toolRegistry = ToolRegistry {
                tool(ProvidePerformanceSuggestions)
            }

            // Create the AI agent
            val agent = AIAgent(
                promptExecutor = promptExecutor,
                strategy = generatePerformanceStrategy(),
                agentConfig = AIAgentConfig(
                    prompt = prompt(id = "performance-adviser") {
                        system { +systemPrompt }
                    },
                    model = JetBrainsAIModels.OpenAI.GPT4o,
                    maxAgentIterations = 100,
                ),
                toolRegistry = toolRegistry
            )

            // Find and analyze the function
            val sourceFiles = findSourceFiles(sourceFolder)
            println("Found ${sourceFiles.size} source files for analysis")

            val targetFunction = findFunction(functionName, sourceFiles)
            if (targetFunction == null) {
                println("Function '$functionName' not found in the provided source files")
                return emptyList()
            }

            println("Found function '$functionName'. Analyzing for performance improvements...")

            // Extract function code and context
            val functionCode = targetFunction.toString()
            val analysisContext = buildAnalysisContext(functionName, functionCode)

            // Run the agent with the function code and context
            val result = agent.runAndGetResult(analysisContext)

            // Parse the result into a list of suggestions
            return result?.split("\n")?.filter { it.isNotBlank() }?.take(3) ?: emptyList()
        } catch (e: Exception) {
            throw IllegalStateException("Error using AI for analysis: ${e.message}. AI analysis is required.", e)
        }
    }


    /**
     * Builds a context string for the AI analysis.
     */
    private fun buildAnalysisContext(functionName: String, functionCode: String): String {
        return """
            Function name: $functionName

            Function code:
            ```
            $functionCode
            ```

            Please analyze this function and provide 3 specific, actionable suggestions to improve its performance.
        """.trimIndent()
    }

    /**
     * Finds all source files in the specified folder.
     *
     * @param sourceFolder The folder to search for source files
     * @return A list of source files
     */
    private fun findSourceFiles(sourceFolder: Path): List<Path> = runBlocking {
        withContext(Dispatchers.IO) {
            val absolutePath = sourceFolder.toAbsolutePath().normalize()
            println("Looking for source files in: $absolutePath")

            if (!Files.exists(absolutePath)) {
                println("Source folder does not exist: $absolutePath")
                // Try to create the directory if it doesn't exist
                try {
                    Files.createDirectories(absolutePath)
                    println("Created directory: $absolutePath")
                } catch (e: Exception) {
                    println("Failed to create directory: ${e.message}")
                    return@withContext emptyList()
                }
            }

            val files = Files.walk(absolutePath)
                .asSequence()
                .filter { it.isRegularFile() }
                .filter { it.name.endsWith(".java") || it.name.endsWith(".kt") }
                .toList()

            println("Found ${files.size} source files:")
            files.forEach { println("  - ${it.fileName}") }

            return@withContext files
        }
    }

    /**
     * Finds the specified function in the source files.
     *
     * @param functionName The name of the function to find
     * @param sourceFiles The source files to search
     * @return The function declaration if found, null otherwise
     */
    private fun findFunction(functionName: String, sourceFiles: List<Path>): MethodDeclaration? {
        for (sourceFile in sourceFiles) {
            try {
                if (sourceFile.name.endsWith(".java")) {
                    val compilationUnit = StaticJavaParser.parse(sourceFile.toFile())
                    val methods = compilationUnit.findAll(MethodDeclaration::class.java)

                    val method = methods.find { it.nameAsString == functionName }
                    if (method != null) {
                        return method
                    }
                } else if (sourceFile.name.endsWith(".kt")) {
                    // For Kotlin files, we'll use a simpler approach for now
                    // In a real implementation, we would use the Kotlin compiler API
                    val content = Files.readString(sourceFile)
                    if (content.contains("fun $functionName") || content.contains("fun `$functionName`")) {
                        // Create a simple method declaration to represent the Kotlin function
                        val method = MethodDeclaration()
                        method.setName(functionName)
                        method.setBody(StaticJavaParser.parseBlock("{ /* Kotlin function body */ }"))
                        return method
                    }
                }
            } catch (e: Exception) {
                println("Error parsing file ${sourceFile.fileName}: ${e.message}")
            }
        }

        return null
    }



    /**
     * Generates the strategy for the performance analysis agent.
     */
    private fun generatePerformanceStrategy(): AIAgentStrategy = strategy("performance_analysis") {
        val nodeAnalyzeAndSuggest by subgraphWithTask<String>(
            toolSelectionStrategy = ToolSelectionStrategy.ALL,
        ) { input ->
            """
               Function to analyze: $input

               This input contains the function code and detected code patterns.
               Analyze the function for performance issues and provide 3 specific, actionable suggestions to improve its performance.
               Focus on the detected patterns and identify opportunities for optimization.
               For each suggestion, explain what the issue is, why it impacts performance, and how to fix it.

               Call the finish_task_execution tool when you have completed your analysis and have 3 suggestions.
            """.trimIndent()
        }

        edge(nodeStart forwardTo nodeAnalyzeAndSuggest)
        edge(nodeAnalyzeAndSuggest forwardTo nodeFinish transformed { it.result })
    }
}

/**
 * Tool for providing performance suggestions.
 */
object ProvidePerformanceSuggestions : ProvideSubgraphResult<StringSubgraphResult>() {
    override val argsSerializer: KSerializer<StringSubgraphResult> = StringSubgraphResult.serializer()

    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "finish_task_execution",
        description = "Please call this tool after you have analyzed the function and have 3 specific, actionable suggestions to improve its performance. Each suggestion should explain what the issue is, why it impacts performance, and how to fix it.",
        requiredParameters = listOf(
            ToolParameterDescriptor(
                name = "result",
                description = "The 3 performance improvement suggestions, separated by newlines",
                type = ToolParameterType.String
            )
        )
    )

    override suspend fun execute(args: StringSubgraphResult): StringSubgraphResult {
        return args
    }
}
