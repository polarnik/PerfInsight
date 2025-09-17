package com.jetbrains.perfinsight.yk.filter;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The class gets the samples from the root node, and calculate the percentage of samples for descendant nodes.
 * If the percentage of samples is greater than or equal to minPercent, the node is included in the result.
 * Children of excluded nodes are not traversed further (they are pruned).
 */
public class FilterSampingBySamplesPercent implements Filter {
    @Override
    public View doFilter(View view, Double minPercent) {
        if (view == null) return null;
        if (view.nodes == null || view.nodes.isEmpty()) return view;
        if (minPercent == null) return view;

        View out = new View();
        out.description = view.description;
        out.nodes = new ArrayList<>();
        for (Node root : view.nodes) {
            long rootSamples = root.samples == null ? 0L : root.samples;
            Node filteredRoot = filterNode(root, rootSamples, minPercent);
            if (filteredRoot != null) {
                out.nodes.add(filteredRoot);
            }
        }
        return out;
    }

    private Node filterNode(Node node, long rootSamples, double minPercent) {
        if (node == null) return null;
        long nodeSamples = node.samples == null ? 0L : node.samples;
        double percent = rootSamples > 0 ? (nodeSamples * 100.0) / rootSamples : 0.0;

        // Always keep the very root node (percent will be 100 when comparing to itself)
        boolean keep = percent >= minPercent || node == null; // node==null never happens here, but keep root via percent==100
        if (!keep) return null;

        Node copy = new Node();
        copy.call_tree = node.call_tree;
        copy.time_ms = node.time_ms;
        copy.samples = node.samples;
        copy.avg_time_ms = node.avg_time_ms;
        copy.count = node.count;
        copy.children = new ArrayList<>();

        if (node.children != null) {
            for (Node ch : node.children) {
                Node filteredChild = filterNode(ch, rootSamples, minPercent);
                if (filteredChild != null) {
                    copy.children.add(filteredChild);
                }
            }
        }
        return copy;
    }
}
