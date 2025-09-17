package com.jetbrains.perfinsight.yk.calculate;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

/**
 * Calculate two fields:
 * - self_time_ms
 * - self_samples
 *
 * self_samples is a difference between samples and sum of samples for the children nodes.
 *
 * self_time_ms is a difference between time_ms and sum of time_ms for the children nodes
 */
public class SelfSamplesCalculator implements Calculator {
    @Override
    public View doCacculate(View view) {
        if (view == null || view.nodes == null) return view;
        for (Node root : view.nodes) {
            apply(root);
        }
        return view;
    }

    private void apply(Node node) {
        if (node == null) return;
        // Recurse first to ensure children are processed
        if (node.children != null) {
            for (Node ch : node.children) apply(ch);
        }

        // Compute self_samples
        if (node.samples == null) {
            node.self_samples = null;
        } else {
            long sumChildrenSamples = 0L;
            if (node.children != null) {
                for (Node ch : node.children) {
                    if (ch != null && ch.samples != null) sumChildrenSamples += ch.samples;
                }
            }
            long diff = node.samples - sumChildrenSamples;
            node.self_samples = Math.max(0L, diff);
            node.self_samples_percent = node.samples == 0 ? 0.0 : 100.0 * node.self_samples / node.samples;
        }

        // Compute self_time_ms
        if (node.time_ms == null) {
            node.self_time_ms = null;
        } else {
            double sumChildrenTime = 0.0;
            if (node.children != null) {
                for (Node ch : node.children) {
                    if (ch != null && ch.time_ms != null) sumChildrenTime += ch.time_ms;
                }
            }
            double diff = node.time_ms - sumChildrenTime;
            // clamp tiny negative due to float errors and never negative overall
            node.self_time_ms = diff <= 0 ? 0.0 : diff;
            node.self_time_percent = node.time_ms == 0 ? 0.0 : 100.0 * node.self_time_ms / node.time_ms;
        }
    }
}
