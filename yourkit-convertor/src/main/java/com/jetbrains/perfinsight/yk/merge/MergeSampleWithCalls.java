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
 *
 *  The count nodes have fields:
 *   - time_ms
 *   - avg_time_ms
 *   - count
 *
 * Algorithm (optimized & hierarchical):
 *  - For each sampling node, find a matching calls node in the current scope by call_tree only (level is ignored).
 *  - Once a sampling node is matched with a calls node, its children are matched only among the children of that calls node.
 *  - If a sampling node has no match in the current scope, its children are matched starting from the calls roots again.
 *  - When multiple matches exist in a scope, pick the one with the maximum non-null count; if all counts are null, pick the first.
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

        List<Node> callsRoots = (calls == null || calls.nodes == null) ? Collections.emptyList() : calls.nodes;

        // Merge into a new View to avoid mutating input unexpectedly
        View result = new View();
        result.description = sampling.description;
        for (Node root : sampling.nodes) {
            result.nodes.add(mergeNodeScoped(root, callsRoots, callsRoots));
        }
        return result;
    }

    private Node mergeNodeScoped(Node sample, List<Node> scope, List<Node> callsRoots) {
        Node merged = new Node();
        // copy sampling fields
        merged.call_tree = sample.call_tree;
        merged.time_ms = sample.time_ms;
        merged.samples = sample.samples;
        merged.samples_percent = sample.samples_percent;

        // find corresponding count node by call_tree within the current scope
        Node matched = pickMatching(scope, sample.call_tree);
        merged.count = (matched == null) ? null : matched.count;

        // Determine next scope: children of matched calls node; if no match, restart from calls roots
        List<Node> nextScope = (matched == null || matched.children == null) ? callsRoots : matched.children;

        // Recurse children
        if (sample.children != null) {
            for (Node ch : sample.children) {
                merged.children.add(mergeNodeScoped(ch, nextScope, callsRoots));
            }
        }
        return merged;
    }

    private Node pickMatching(List<Node> scope, String callTree) {
        if (scope == null || scope.isEmpty()) return null;
        Node best = null;
        Long bestCount = null;
        for (Node n : scope) {
            if (Objects.equals(n.call_tree, callTree)) {
                if (best == null) {
                    best = n;
                    bestCount = n.count;
                } else {
                    // prefer larger non-null count
                    if (n.count != null) {
                        if (bestCount == null || n.count > bestCount) {
                            best = n;
                            bestCount = n.count;
                        }
                    }
                }
            }
        }
        return best;
    }
}
