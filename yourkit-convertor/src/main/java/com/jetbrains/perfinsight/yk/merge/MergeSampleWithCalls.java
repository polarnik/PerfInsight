package com.jetbrains.perfinsight.yk.merge;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class iterates by sampling Nodes and merges them with corresponding calls Nodes.
 * Sampling nodes have: time_ms, samples, samples_percent.
 * Counting nodes have: time_ms, avg_time_ms, count.
 *
 * Strategy (scoped, normalized, and fuzzy):
 *  - First try to match by normalized call_tree (ignore line numbers).
 *  - Then try javaFile + javaMethod.
 *  - Then try javaClass + javaMethod.
 *  - If not found at current scope level, search recursively within the scope subtree.
 *  - When multiple matches exist, pick the one with the highest non-null count.
 */
public class MergeSampleWithCalls {
    private static final Logger LOG = Logger.getLogger(MergeSampleWithCalls.class.getName());

    View doMerge(View sampling, View calls) {
        if (sampling == null) return null;
        if (sampling.nodes == null) return sampling;
        List<Node> callsRoots = (calls == null || calls.nodes == null) ? Collections.emptyList() : calls.nodes;

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
        merged.javaFile = sample.javaFile;
        merged.javaFile_line_number = sample.javaFile_line_number;
        merged.javaMethod = sample.javaMethod;
        merged.javaClass = sample.javaClass;
        merged.time_ms = sample.time_ms;
        merged.self_time_ms = sample.self_time_ms;
        merged.self_time_percent = sample.self_time_percent;
        merged.samples = sample.samples;
        merged.self_samples = sample.self_samples;
        merged.self_samples_percent = sample.self_samples_percent;
        merged.samples_percent = sample.samples_percent;

        // find corresponding count node within the current scope (and its subtree)
        Node matched = pickMatching(scope, sample);
        merged.count = (matched == null) ? null : matched.count;
        merged.avg_time_ms = (matched == null) ? null : matched.avg_time_ms;

        // Next scope: children of matched node if any; otherwise try to keep scope if children can match; else restart from roots
        List<Node> nextScope;
        if (matched == null) {
            Node childMatch = null;
            if (sample.children != null) {
                for (Node ch : sample.children) {
                    childMatch = pickMatching(scope, ch);
                    if (childMatch != null) break;
                }
            }
            if (childMatch != null) {
                nextScope = scope; // skip-level: keep scope
                final String sampleCt = sample.call_tree;
                final String childCt = childMatch.call_tree;
                LOG.log(Level.FINE, () -> String.format("Skip-level merge: sampling node without counterpart is bypassed. sampling='%s'; first child matched in calls as '%s'", sampleCt, childCt));
            } else {
                nextScope = callsRoots; // fallback
            }
        } else {
            nextScope = (matched.children == null || matched.children.isEmpty()) ? callsRoots : matched.children;
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

        // 1) Strict by normalized call_tree among scope level
        Node strict = pickBestBy(scope, n -> Objects.equals(n.call_tree, sample.call_tree));
        if (strict != null) return strict;


        // 2) Fuzzy by javaFile + javaClass + javaFile_line_number
        if (sample.javaFile_line_number != null && sample.javaFile != null) {
            Node fuzzy2 = pickBestBy(scope, n ->
                Objects.nonNull(n.javaFile_line_number)
                    && Objects.equals(n.javaClass, sample.javaClass)
                    && Objects.equals(n.javaFile, sample.javaFile)
                    && Objects.equals(n.javaFile_line_number, sample.javaFile_line_number)
            );
            if (fuzzy2 != null) return fuzzy2;
        }

        // 3) Fuzzy by javaFile + javaMethod
        if (sample.javaFile != null && sample.javaMethod != null) {
            Node fuzzy = pickBestBy(scope, n -> Objects.equals(n.javaFile, sample.javaFile)
                && Objects.equals(n.javaMethod, sample.javaMethod));
            if (fuzzy != null) return fuzzy;
        }

        // 4) Strict by normalized call_tree among scope level
        final String normSampleCt = normalizeCallTree(sample.call_tree);
        Node non_strict = pickBestBy(scope, n -> Objects.equals(normalizeCallTree(n.call_tree), normSampleCt));
        if (non_strict != null) return non_strict;


        // 5) Recursive search in subtree of scope
        Node recursive = pickBestBy(flatten(scope), n -> {
            if (n == null) return false;
            if (Objects.equals(n.call_tree, sample.call_tree)) return true;

            boolean check2 = Objects.nonNull(n.javaFile_line_number)
                && Objects.nonNull(n.javaFile)
                && Objects.equals(n.javaClass, sample.javaClass)
                && Objects.equals(n.javaFile, sample.javaFile)
                && Objects.equals(n.javaFile_line_number, sample.javaFile_line_number);
            if (check2) return true;

            if (Objects.equals(normalizeCallTree(n.call_tree), normSampleCt)) return true;

            boolean jfjm = sample.javaFile != null && sample.javaMethod != null
                    && Objects.equals(n.javaFile, sample.javaFile)
                    && Objects.equals(n.javaMethod, sample.javaMethod);
            if (jfjm) return true;

            boolean jcjm = sample.javaClass != null && sample.javaMethod != null
                    && Objects.equals(n.javaClass, sample.javaClass)
                    && Objects.equals(n.javaMethod, sample.javaMethod);
            return jcjm;
        });
        return recursive;
    }

    private String normalizeCallTree(String ct) {
        if (ct == null) return null;
        String s = ct.trim();
        int space = s.indexOf(' ');
        if (space <= 0) return s; // no expected pattern
        String left = s.substring(0, space); // file:line or file
        String right = s.substring(space + 1).trim();
        int colon = left.lastIndexOf(':');
        if (colon > 0) {
            left = left.substring(0, colon); // drop :line
        }
        return left + " " + right;
    }

    private List<Node> flatten(List<Node> scope) {
        List<Node> out = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        for (Node n : scope) if (n != null) stack.push(n);
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            out.add(n);
            if (n.children != null) {
                for (Node ch : n.children) {
                    if (ch != null) stack.push(ch);
                }
            }
        }
        return out;
    }

    private interface Matcher { boolean test(Node n); }

    private Node pickBestBy(List<Node> scope, Matcher matcher) {
        Node best = null;
        Long bestCount = null;
        for (Node n : scope) {
            if (n != null && matcher.test(n)) {
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
