package com.jetbrains.perfinsight.yk.merge;

import com.jetbrains.perfinsight.yk.model.View;

/**
 * The class iterates by sampling Nodes and merges them with corresponding calls Nodes.
 * The sampling nodes have fields:
 *  - time_ms
 *  - samples
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
 *  - count (from a corresponding count Node)
 */
public class MergeSampleWithCalls {
    View doMerge(View sampling, View calls) {
        return null;
    }
}
