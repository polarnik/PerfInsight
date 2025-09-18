package com.jetbrains.perfinsight.yk.split;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.StackTrace;
import com.jetbrains.perfinsight.yk.model.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * The View has nodes.
 * The nodes can have children.
 * Some nodes don't have children, there are list-nodes.
 *
 * The StackTraceSplitter:
 * 1. Find the list-nodes (nodes with children)
 * 2. For each list-node, create a new list with the parent-nodes from the current list-node to the root
 */
public class StackTraceSplitter {
    public List<List<Node>> split(View view) {
        List<List<Node>> result = new ArrayList<>();

        List<Node> list_nodes = new ArrayList<>();
        find_list_nodes(view.nodes.getFirst(), list_nodes);

        for(Node list_node : list_nodes) {
            result.add(find_path_from_root_to_node(view.nodes.getFirst(), list_node));
        }
        return result;
    }

    public List<StackTrace> getStackTraces(View view) {
        List<List<Node>> stackTraces = split(view);
        List<StackTrace> result = new ArrayList<>();
        for (List<Node> listNodes : stackTraces) {
            StackTrace stackTrace = new StackTrace();
            stackTrace.nodes = listNodes;
            stackTrace.total_self_samples = listNodes.stream().mapToLong(node -> node.self_samples).sum();
            stackTrace.total_samples = listNodes.stream().mapToLong(node -> node.samples).sum();
            stackTrace.total_self_time_ms = listNodes.stream().mapToDouble(node -> node.self_time_ms).sum();
            stackTrace.total_time_ms = listNodes.stream().mapToDouble(node -> node.time_ms).sum();
            result.add(stackTrace);
        }
        result.sort((a, b) -> Double.compare(b.total_self_samples, a.total_self_samples)); // Sort in descending order

        return result;
    }

    /**
     * Find the list-nodes (nodes without children)
     */
    private void find_list_nodes(Node root, List<Node> list_nodes) {
        if (root == null) return;
        if (root.children == null || root.children.isEmpty()) {
            list_nodes.add(root);
        } else {
            for (Node child : root.children) {
                find_list_nodes(child, list_nodes);
            }
        }
    }

    private List<Node> find_path_from_root_to_node(Node root, Node node) {
        if(root.children == null || root.children.isEmpty()) {
            return null;
        } else {
            for(Node child : root.children) {
                if(child == node) {
                    List<Node> result = new ArrayList<>();
                    result.add(root);
                    result.add(node);
                    return result;
                } else {
                    List<Node> result = new ArrayList<>();
                    List<Node> tmp_result = find_path_from_root_to_node(child, node);
                    if(tmp_result != null) {
                        result.add(root);
                        result.addAll(tmp_result);
                        return result;
                    }
                }
            }
        }
        return null;
    }

}
