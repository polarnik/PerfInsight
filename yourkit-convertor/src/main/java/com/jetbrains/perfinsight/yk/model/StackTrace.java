package com.jetbrains.perfinsight.yk.model;

import java.util.List;

public class StackTrace {
    public Double total_self_time_ms;
    public Long total_self_samples;
    public List<Node> nodes;
    public Long total_samples;
    public Double total_time_ms;
}
