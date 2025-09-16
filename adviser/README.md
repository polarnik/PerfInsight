# Performance Adviser Agent

## Overview

The Performance Adviser Agent is a tool that analyzes Java and Kotlin source code to identify performance issues and provide actionable suggestions for improvement. It uses a combination of static code analysis and AI-powered insights to detect common performance anti-patterns and recommend optimizations.

## Module Structure

The adviser module is organized as follows:

- `src/main/kotlin/com/jetbrains/` - Main source code
  - `Main.kt` - Entry point for the application
  - `PerformanceAdviserAgent.kt` - Core agent implementation for analyzing code
  - `utils/GrazieExecutor.kt` - Utility for interacting with JetBrains AI services

- `src/test/` - Test resources and test implementations
  - `kotlin/com/jetbrains/perfinsights/` - Test classes
  - `resources/examination/` - Sample code files for testing

## Execution

The Performance Adviser Agent can be executed as a standalone JAR file. The main function in `Main.kt` serves as the entry point.

### Command-line Arguments

The agent requires two command-line arguments:

1. `functionName` - The name of the function to analyze for performance issues
2. `sourceFolder` - The path to the folder containing source files to analyze

### Example Usage

```bash
java -jar adviser.jar processData /path/to/source/code
```

This will analyze the `processData` function in the source files located in `/path/to/source/code` and provide performance improvement suggestions.

## AI-Powered Analysis

The agent uses JetBrains AI services for enhanced analysis when available. To enable AI-powered analysis, you need to set up the GRAZIE token.

### Setting up the GRAZIE Token

The GRAZIE token is required for accessing JetBrains AI services. You need to set it as an environment variable:

```bash
export GRAZIE_TOKEN="your-grazie-token"
```

```run configuration in IDE: add environment variable
GRAZIE_TOKEN=e"your-grazie-token"
```

#### Obtaining a GRAZIE Token

To obtain a GRAZIE token:

1. Log in to your JetBrains account
2. Navigate to the JetBrains AI services page https://platform.jetbrains.ai/
3. Generate and copy new token for API access under profile icon
4. Copy the token and set it as the `GRAZIE_TOKEN` environment variable

The GRAZIE token is required for the agent to function. If the token is not set, the agent will throw an exception as AI analysis is required for performance suggestions.

## Features

- Detection of performance-critical patterns:
  - Nested loops
  - Object creation in loops
  - Method calls in loops
  - Conditionals in loops
  - String concatenation in loops
  - Recursion

- AI-powered analysis:
  - Context-aware suggestions
  - Detailed explanations of performance issues
  - Code examples for implementing improvements

## Example Output

When the agent completes its analysis, it will output performance improvement suggestions:

```
Performance Improvement Suggestions for function 'processData':
1. Avoid creating new objects inside loops. Consider moving object creation outside the loop or reusing existing objects to reduce memory allocation and garbage collection overhead.
2. Minimize method calls inside loops, especially if they perform expensive operations. Consider inlining small methods or caching results of method calls that don't change within the loop.
3. Replace string concatenation in loops with StringBuilder to avoid creating multiple intermediate String objects.
```
