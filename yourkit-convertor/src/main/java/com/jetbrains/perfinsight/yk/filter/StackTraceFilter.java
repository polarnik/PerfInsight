package com.jetbrains.perfinsight.yk.filter;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.StackTrace;

import java.util.List;

public class StackTraceFilter {
    public List<StackTrace> doFilter(List<StackTrace> stackTraces, Double min_self_time_percent, Double count_multiplicator ) {
        List<StackTrace> result = new java.util.ArrayList<>();
        for(StackTrace stackTrace: stackTraces) {
            stackTrace = doFilter(stackTrace, min_self_time_percent, count_multiplicator);
            result.add(stackTrace);
        }
        return result;
    }

    public StackTrace doFilter(StackTrace stackTrace, Double min_self_time_percent, Double count_multiplicator ) {
        StackTrace newStackTrace = new StackTrace();
        newStackTrace.nodes = new java.util.ArrayList<>();
        boolean skipMode = true;
        for(int i = 0; i < stackTrace.nodes.size(); i++) {
            Node node = stackTrace.nodes.get(i);
            if(skipMode) {
                if(node.self_time_percent != null && node.self_time_percent >= min_self_time_percent) {
                    skipMode = false;
                }
                if(node.count_multiplicator != null && node.count_multiplicator >= count_multiplicator) {
                    skipMode = false;
                }
                if(i + 1 < stackTrace.nodes.size()) {
                    Node nextNode = stackTrace.nodes.get(i + 1);
                    if(nextNode.self_time_percent != null && nextNode.self_time_percent >= min_self_time_percent) {
                        skipMode = false;
                    }
                    if(nextNode.count_multiplicator != null && nextNode.count_multiplicator >= count_multiplicator) {
                        skipMode = false;
                    }
                }
                if(!skipMode) {
                    newStackTrace.nodes.add(node);
                }
            } else {
                newStackTrace.nodes.add(node);
            }
        }

        newStackTrace.total_self_samples = newStackTrace.nodes.stream().mapToLong(node -> node.self_samples).sum();
        newStackTrace.total_samples = newStackTrace.nodes.stream().mapToLong(node -> node.samples).sum();
        newStackTrace.total_self_time_ms = newStackTrace.nodes.stream().mapToDouble(node -> node.self_time_ms).sum();
        newStackTrace.total_time_ms = newStackTrace.nodes.stream().mapToDouble(node -> node.time_ms).sum();

        return newStackTrace;
    }
}
