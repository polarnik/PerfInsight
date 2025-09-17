package com.jetbrains.perfinsight.yk.filter;

import com.jetbrains.perfinsight.yk.model.View;

public interface Filter {
    View doFilter(View view, Double minPercent);
}
