package com.jetbrains.perfinsight.yk.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Node {
    // attributes of <node>
    @XmlAttribute(name = "call_tree")
    public String call_tree;

    @XmlAttribute(name = "time_ms")
    public Long time_ms;

    @XmlAttribute(name = "samples")
    public Long samples;

    @XmlAttribute(name = "avg_time_ms")
    public Long avg_time_ms;

    @XmlAttribute(name = "count")
    public Long count;

    /**
     * There is a relation:
     * (count field of current node) / (count field of parent node).
     *
     * For example, the parent count is 100, the current count is 1000,
     * the multiplicator is 1000 / 100 = 10.0.
     */
    @XmlAttribute(name = "count_multiplicator")
    public Double count_multiplicator;

    // child <node> elements (0..n)
    @XmlElement(name = "node")
    public List<Node> children = new ArrayList<>();

    public Node() {}
}
