#!/bin/zsh

# Export all available views, the filters will not be applied:

snapshot_calls="/Users/Viacheslav.Smirnov/IdeaProjects/github.com/polarnik/PerfInsight/demo-yourkit-convertor/youtrack-2025-09-17-get-issues-sampling.snapshot"
output_dir_calls="/Users/Viacheslav.Smirnov/IdeaProjects/github.com/polarnik/PerfInsight/demo-yourkit-convertor/build/sampling"

cd "/Applications/YourKit Java Profiler.app/Contents/Resources/lib"

java -jar yourkit.jar  -export "$snapshot_calls" "$output_dir_calls"

cd "$OLDPWD"
