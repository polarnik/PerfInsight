package com.jetbrains.perfinsight.yk.filter;

import com.jetbrains.perfinsight.yk.model.View;

/**
 * The class get the samples field from the root node, and calculate the percentage of samples for all child nodes.
 * If the percentage of samples is greater than the minPercent, the node is included in the result.
 *
 * For example, the root node has 100 samples, and the child node has 50 samples.
 * The percentage of samples for the child node is 50%.
 * The minPercent is 50%.
 * The child node is included in the result.
 *
 * For example, the root node has 100 samples, and the child node has 20 samples.
 * The percentage of samples for the child node is 20%.
 * The minPercent is 50%.
 * The child node is excluded from the result.
 */
public class FilterSampingBySamplesPercent implements Filter {
    @Override
    public View doFilter(View view, Double minPercent) {
        return null;
    }
}
