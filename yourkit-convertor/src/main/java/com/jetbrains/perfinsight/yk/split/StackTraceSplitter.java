package com.jetbrains.perfinsight.yk.split;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;

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
    List<List<Node>> split(View view) {}
}
