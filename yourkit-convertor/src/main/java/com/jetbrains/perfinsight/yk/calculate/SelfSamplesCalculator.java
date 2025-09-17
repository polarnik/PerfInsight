package com.jetbrains.perfinsight.yk.calculate;

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
        return null;
    }
}
