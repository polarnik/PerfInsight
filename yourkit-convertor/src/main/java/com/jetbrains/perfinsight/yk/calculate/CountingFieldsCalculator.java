package com.jetbrains.perfinsight.yk.calculate;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

/**
 * Calculates the field count_multiplicator for the nodes.
 *
 * Rules:
 * - For a node with a parent: count_multiplicator = node.count / parent.count
 * - For a root node (no parent): count_multiplicator = 1
 * - If node.count is null -> count_multiplicator = null
 * - If parent.count is null -> count_multiplicator = null
 * - If parent.count is 0 -> count_multiplicator = null (avoid division by zero)
 */
public class CountingFieldsCalculator implements Calculator{
    @Override
    public View doCacculate(View view) {
        if (view == null || view.nodes == null) return view;
        for (Node root : view.nodes) {
            apply(root, root);
        }
        return view;
    }

    private void apply(Node node, Node root) {
        if (node == null) return;

        // Compute multiplicator according to the rules
        if (node.count == null) {
            node.count_multiplicator = null;

        } else if (root.count == null || root.count == 0L) {
            node.count_multiplicator = null;
        } else {
            node.count_multiplicator = node.count.doubleValue() / root.count.doubleValue();
        }

        // Recurse into children
        if (node.children != null) {
            for (Node ch : node.children) {
                apply(ch, root);
            }
        }
    }
}
