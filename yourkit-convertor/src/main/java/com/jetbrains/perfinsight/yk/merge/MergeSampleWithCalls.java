package com.jetbrains.perfinsight.yk.merge;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

import java.util.*;

/**
 * The class iterates by sampling Nodes and merges them with corresponding calls Nodes.
 * The sampling nodes have fields:
 *  - time_ms
 *  - samples
 *  - samples_percent
 *  The key is:
 *   - call_tree
 *   - level
 *
 *  The count nodes have fields:
 *   - time_ms
 *   - avg_time_ms
 *   - count
 *  The key is:
 *   - call_tree
 *   - level
 *
 * The result is a View with sampling Nodes, but nodes have fields:
 *  - time_ms (from a sampling Node)
 *  - samples (from a sampling Node)
 *  - samples_percent (from a sampling Node)
 *  - count (from a corresponding count Node)
 */
public class MergeSampleWithCalls {
    View doMerge(View sampling, View calls) {
        // Defensive checks
        if (sampling == null) return null;
        if (sampling.nodes == null) return sampling;

        // Build lookup map for calls by (call_tree, level)
        Map<String, List<Node>> groupedByCall = new HashMap<>();
        if (calls != null && calls.nodes != null) {
            for (Node root : calls.nodes) {
                collectByLevel(root, 0, groupedByCall);
            }
        }

        // Merge into a new View to avoid mutating input unexpectedly
        View result = new View();
        result.description = sampling.description;
        for (Node root : sampling.nodes) {
            result.nodes.add(mergeNode(root, 0, groupedByCall));
        }
        return result;
    }

    private void collectByLevel(Node node, int level, Map<String, List<Node>> map) {
        if (node == null) return;
        String key = key(node.call_tree, level);
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(node);
        if (node.children != null) {
            for (Node ch : node.children) {
                collectByLevel(ch, level + 1, map);
            }
        }
    }

    private Node mergeNode(Node sample, int level, Map<String, List<Node>> callsByKey) {
        Node merged = new Node();
        // copy sampling fields
        merged.call_tree = sample.call_tree;
        merged.time_ms = sample.time_ms;
        merged.samples = sample.samples;
        merged.samples_percent = sample.samples_percent;

        // find corresponding count node: prefer the one with the same call_tree at same level.
        Long count = null;
        List<Node> candidates = callsByKey.get(key(sample.call_tree, level));
        if (candidates != null && !candidates.isEmpty()) {
            // If multiple, choose max count (conservative) or first; choose max to be deterministic and meaningful.
            for (Node c : candidates) {
                if (c != null && c.count != null) {
                    if (count == null || c.count > count) count = c.count;
                }
            }
        }
        merged.count = count; // may remain null if no match

        // Recurse children
        if (sample.children != null) {
            for (Node ch : sample.children) {
                merged.children.add(mergeNode(ch, level + 1, callsByKey));
            }
        }
        return merged;
    }

    private String key(String callTree, int level) {
        return level + "|" + (callTree == null ? "" : callTree);
    }
}
