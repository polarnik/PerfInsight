package com.jetbrains.perfinsight.yk.filter;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

import java.util.ArrayList;

public class FilterSamplingByTimePercent implements Filter {
    @Override
    public View doFilter(View view, Double minPercent) {
        if (view == null) return null;
        if (view.nodes == null || view.nodes.isEmpty()) return view;
        if (minPercent == null) return view;

        View out = new View();
        out.description = view.description;
        out.nodes = new ArrayList<>();
        for (Node root : view.nodes) {
            double root_time_ms = root.time_ms == null ? 0L : root.time_ms;
            Node filteredRoot = filterNode(root, root_time_ms, minPercent);
            if (filteredRoot != null) {
                out.nodes.add(filteredRoot);
            }
        }
        return out;
    }

    private Node filterNode(Node node, double root_time_ms, double minPercent) {
        if (node == null) return null;
        double node_time_ms = node.time_ms == null ? 0L : node.time_ms;
        double percent = root_time_ms > 0 ? (node_time_ms * 100.0) / root_time_ms : 0.0;

        // Always keep the very root node (percent will be 100 when comparing to itself)
        boolean keep = percent >= minPercent || node == null; // node==null never happens here, but keep root via percent==100
        if (!keep) return null;

        Node copy = new Node();
        copy.call_tree = node.call_tree;
        // copy parsed java location/method info
        copy.javaFile = node.javaFile;
        copy.javaFile_line_number = node.javaFile_line_number;
        copy.javaMethod = node.javaMethod;
        copy.javaClass = node.javaClass;
        copy.time_ms = node.time_ms;
        copy.samples = node.samples;
        copy.avg_time_ms = node.avg_time_ms;
        copy.count = node.count;
        copy.count_multiplicator = node.count_multiplicator;
        copy.children = new ArrayList<>();

        if (node.children != null) {
            for (Node ch : node.children) {
                Node filteredChild = filterNode(ch, root_time_ms, minPercent);
                if (filteredChild != null) {
                    copy.children.add(filteredChild);
                }
            }
        }
        return copy;
    }
}
