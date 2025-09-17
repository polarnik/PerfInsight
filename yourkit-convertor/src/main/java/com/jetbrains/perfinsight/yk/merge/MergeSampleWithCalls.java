package com.jetbrains.perfinsight.yk.merge;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger LOG = Logger.getLogger(MergeSampleWithCalls.class.getName());

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
        // copy parsed java location/method info
        merged.javaFile = sample.javaFile;
        merged.javaFile_line_number = sample.javaFile_line_number;
        merged.javaMethod = sample.javaMethod;
        merged.javaClass = sample.javaClass;
        merged.time_ms = sample.time_ms;
        merged.samples = sample.samples;
        merged.samples_percent = sample.samples_percent;

        // find corresponding count node by call_tree within the current scope
        Node matched = pickMatching(scope, sample);
        merged.count = (matched == null) ? null : matched.count;

        // Determine next scope: children of matched calls node; if no match, consider skip-level fuzzy or restart from calls roots
        List<Node> nextScope;
        boolean skippedLevel = false;
        if (matched == null) {
            // New fuzzy level: Sampling may have an extra intermediate node that's absent in Counting.
            // If any child of the current sampling node matches something in the current scope,
            // we keep the current scope for children (skip this level) and log a WARN.
            Node childMatch = null;
            if (sample.children != null) {
                for (Node ch : sample.children) {
                    childMatch = pickMatching(scope, ch);
                    if (childMatch != null) break;
                }
            }
            if (childMatch != null) {
                skippedLevel = true;
                nextScope = scope; // don't narrow the scope; effectively skip this level
                final String sampleCt = sample.call_tree;
                final String childCt = childMatch.call_tree;
                LOG.log(Level.WARNING, () -> String.format("Skip-level merge: sampling node without counterpart is bypassed. sampling='%s'; first child matched in calls as '%s'", sampleCt, childCt));
            } else {
                nextScope = callsRoots; // original fallback behavior
            }
        } else {
            nextScope = (matched.children == null) ? callsRoots : matched.children;
        }

        // Recurse children
        if (sample.children != null) {
            for (Node ch : sample.children) {
                merged.children.add(mergeNodeScoped(ch, nextScope, callsRoots));
            }
        }
        return merged;
    }

    private Node pickMatching(List<Node> scope, Node sample) {
        if (scope == null || scope.isEmpty() || sample == null) return null;
        // 1) Strict match by call_tree
        Node strict = pickBestBy(scope, n -> Objects.equals(n.call_tree, sample.call_tree));
        if (strict != null) return strict;
        // 2) Fuzzy fallback: match by javaFile + javaMethod, ignoring line number
        if (sample.javaFile != null && sample.javaMethod != null) {
            Node fuzzy = pickBestBy(scope, n -> Objects.equals(n.javaFile, sample.javaFile)
                    && Objects.equals(n.javaMethod, sample.javaMethod));
            if (fuzzy != null) {
                if (!Objects.equals(fuzzy.call_tree, sample.call_tree)) {
                    // Log warning about mismatch in call_tree (likely due to line number difference)
                    LOG.log(Level.WARNING, () -> String.format("Fuzzy merge: call_tree mismatch (line number tolerated). sample='%s' vs calls='%s'", sample.call_tree, fuzzy.call_tree));
                }
                return fuzzy;
            }
        }
        return null;
    }

    private interface Matcher { boolean test(Node n); }

    private Node pickBestBy(List<Node> scope, Matcher matcher) {
        Node best = null;
        Long bestCount = null;
        for (Node n : scope) {
            if (matcher.test(n)) {
                if (best == null) {
                    best = n;
                    bestCount = n.count;
                } else if (n.count != null && (bestCount == null || n.count > bestCount)) {
                    best = n;
                    bestCount = n.count;
                }
            }
        }
        return best;
    }
}
