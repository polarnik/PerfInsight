package com.jetbrains.perfinsight.yk.printer;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.StackTrace;

public class StackTraceToCode {
    public String toCode(StackTrace stackTrace) {
        StringBuffer code = new StringBuffer();
        code.append("```java\n");
        code.append("Performance Problem with ");
        code.append(stackTrace.total_self_samples);
        code.append(" samples and ");
        code.append(stackTrace.total_self_time_ms);
        code.append(" ms:\n ");

        for(Node node : stackTrace.nodes) {
            code.append("\tat ");
            code.append(node.javaMethod);
            code.append("(");
            code.append(node.javaFile);
            code.append(":");
            code.append(node.javaFile_line_number);
            code.append(")\t");
            if(node.self_time_percent != null && node.self_time_percent > 1.0) {
                code.append("time: ");
                code.append(node.self_time_percent);
                code.append("%");
            }
            code.append("\t");
            if(node.count_multiplicator != null && node.count_multiplicator > 1.5) {
                code.append("loop: ");
                code.append(node.count_multiplicator);
            }
            code.append("\n");

        }
        code.append("```\n");
        return code.toString();
    }
}
