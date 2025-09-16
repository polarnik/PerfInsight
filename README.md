# PerfInsight
HKTN25-30 🔬 PerfInsight tool for JVM Profiles

# Problem

AI Agent Junie can work as a static code analyzer:
- summarize a source code
- find patterns and anti-patterns

but it cannot work with profiling data like:
- JFR files
- async-profiler files
- YourKit profiler files

because the files are too large

# Solution

We can create a pipeline and a set of tools for preparation and extraction performance context about the source code:

0. ⚙️ Start benchmarks
1. ⚙️ Collect performance profiles
2. 🔬 Analyze Profiles
    * Extract thread pools from profile results
    * Extract slow functions from profile results
    * Get system version info
    * Get source code for the slow classes and methods
3. 🔮 Find performance antipatterns in the selected source code
4. 🔮 Generate suggestions for improving performance
5. 🔁 Revalidate

# Examples

It can use:

- youtrack performance tests as a becnhmark component
- async profiler as a profile component
- some cusom tools for getting key performance metrics from the JFR files and extracting the **performance context**
- Junie as an AI component for generating performance insides
- TeamCity as a pipeline system

