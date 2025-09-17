package com.jetbrains.perfinsight.yk.calculate;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

public class SamplingFieldsCalculator implements Calculator {
    @Override
    public View doCacculate(View view) {
        if (view == null || view.nodes == null) return view;
        for (Node root : view.nodes) {
            calculate_samples_percent(root, null);
        }
        return view;
    }

    private void calculate_samples_percent(Node node, Node parent) {
        if (node == null) return;

        if (node.samples == null) {
            node.samples_percent = null;
        } else if (parent == null) {
            node.samples_percent = 100.0;
        } else if (parent.samples == null || parent.samples == 0L) {
            node.samples_percent = null;
        } else {
            node.samples_percent = 100.0 * node.samples.doubleValue() / parent.samples.doubleValue();
        }

        if (node.children != null) {
            for (Node ch : node.children) {
                calculate_samples_percent(ch, node);
            }
        }
    }
}
