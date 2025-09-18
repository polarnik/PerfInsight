# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog, and this project adheres to Semantic Versioning.

## [0.4.0] - 2025-09-17
- Planned improvements and features.
- yourkit-convertor: MergeSampleWithCalls matching greatly improved for completeness and efficiency:
  - normalize call_tree (strip line numbers) for primary matching; scope-aware hierarchical merge maintained; best match picked by highest non-null count.
  - add fuzzy keys: (javaFile + javaMethod) and (javaClass + javaMethod) when call_tree differs; search recursively within the current scope subtree if needed; skip-level behavior kept with reduced log noise (FINE).
  - propagate javaFile, javaFile_line_number, javaMethod, javaClass from sampling nodes in the result.
- yourkit-convertor: Golden-file regression in place for exact output verification:
  - GoldenMergeVerificationTest reads filtered inputs (1758146295-filtered-sampling.xml, 1758146295-filtered-counting.xml) from resources, merges with improved algorithm, applies calculators, and asserts exact XML equality with expected-1758146295-merged.xml.
  - Helper generator tests to produce the expected file deterministically were added.
- yourkit-convertor: Made XML parsing more robust by introducing adapters for Double fields (handles values like "< 0.1") and switched time_ms/avg_time_ms to Double to support fractional milliseconds.
- yourkit-convertor: Integration test now prints absolute paths to the generated temp files for filtered sampling and merged outputs.
- yourkit-convertor: Filters (FilterSampingBySamplesPercent, FilterSamplingByTimePercent) now propagate javaFile, javaFile_line_number, javaMethod, and javaClass to filtered nodes.
- yourkit-convertor: Integration test now also serializes the final merged View to JSON and verifies JSON deserialization.
- yourkit-convertor: Implemented SelfSamplesCalculator to compute self_samples and self_time_ms for each Node.
- yourkit-convertor: Filters and merge now propagate self_time_ms, self_time_percent, self_samples, and self_samples_percent attributes in output Views.
- yourkit-convertor: Implemented StackTraceSplitter to extract parent chains for each list-node (node with children) from a View.

## [0.3.2] - 2025-09-17
### Added
- yourkit-convertor: Implemented CountingFieldsCalculator to compute count_multiplicator for each Node based on parent/child count ratio.
- Unit test CountingFieldsCalculatorTest using count.xml verifying root multiplicators and that the node "WebComponent.java:1000 org.glassfish.jersey.servlet.WebComponent.serviceImpl(URI)" has count_multiplicator = 10.
- yourkit-convertor: Implemented SamplingFieldsCalculator to compute samples_percent for each Node as 100 * child.samples / parent.samples; root set to 100.
- Unit test SamplingFieldsCalculatorTest using sampling/Call-tree-all-threads-merged-WEB.xml verifying the specified node has samples_percent = 100.0.
- yourkit-convertor: Implemented MergeSampleWithCalls to merge sampling tree with calls tree by (call_tree, level) and enrich sampling nodes with count from matching calls nodes.

## [0.3.1] - 2025-09-17
### Added
- yourkit-convertor: XML deserialization for YourKit call tree views via JAXB (Jakarta XML Bind).
- Data model enhancements: View with a list of Node, Node supports recursive 0..n children. Attributes mapped: call_tree, time_ms, samples, avg_time_ms, count.
- Unit tests that deserialize count.xml and sample.xml and validate structure.
- Implemented FilterSampingBySamplesPercent to prune nodes by percentage of root samples; added unit test using sample.2.xml verifying nodes below 2% are excluded while top nodes are retained.

## [0.3.0] - 2025-09-17
- Implemented PerformanceAdviserAgent with advanced code pattern analysis for more targeted performance suggestions.
- Added detection of performance-critical patterns like nested loops, object creation in loops, and recursion.
- Implemented intelligent suggestion generation based on detected code patterns.
- Integrated PerformanceAdviserAgent with koog framework.
- Added AI-powered performance analysis.
- Created GrazieExecutor utility for AI model integration.
- Added unit tests for PerformanceAdviserAgent to verify suggestion generation.
- Added README.md to adviser module with documentation on structure, execution, and GRAZIE token setup.

## [0.2.0] - 2025-09-16
- Added Gradle Wrapper
- Initialized Gradle submodules: core, yourkit-convertor, cli, demo-benchmark, demo-service.
- Added demo-benchmark for getting Profiling data from YouTrack server.


## [0.1.0] - 2025-09-16
### Added
- Initial project documentation: README and docs (inspiration, plan, requirements, tasks, research).
- Initialized this CHANGELOG.

