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

    }
}
