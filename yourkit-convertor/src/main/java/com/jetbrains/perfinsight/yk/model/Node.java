package com.jetbrains.perfinsight.yk.model;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Node {
    // attributes of <node>
    /**
     * @example call_tree="WriterInterceptorExecutor.java:242 jetbrains.gap.resource.pojo.GapFilteringJsonProvider.writeTo(Object, Class, Type, Annotation[], MediaType, MultivaluedMap, OutputStream)"
     */
    @XmlAttribute(name = "call_tree")
    public String call_tree;

    /**
     * @example javaFile="WriterInterceptorExecutor.java"
     */
    @XmlAttribute(name = "javaFile")
    public String javaFile;

    /**
     * @example javaFile_line_number=242
     */
    @XmlAttribute(name = "call_tree")
    public Long javaFile_line_number;

    /**
     * @example javaMethod="jetbrains.gap.resource.pojo.GapFilteringJsonProvider.writeTo(Object, Class, Type, Annotation[], MediaType, MultivaluedMap, OutputStream)"
     */
    @XmlAttribute(name = "javaMethod")
    public String javaMethod;

    /**
     * @example javaClass="jetbrains.gap.resource.pojo.GapFilteringJsonProvider"
     */
    @XmlAttribute(name = "javaClass")
    public String javaClass;

    @XmlAttribute(name = "time_ms")
    public Double time_ms;

    @XmlAttribute(name = "samples")
    public Long samples;

    /**
     * There is a relation:
     * 100 * (samples field of current node) / (samples field of parent node).
     *
     * For example, the parent count is 100, the current count is 90,
     * the multiplicator is 100 * 90 / 100 = 90.0.
     */
    @XmlAttribute(name = "samples_percent")
    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(DoubleLenientAdapter.class)
    public Double samples_percent;


    @XmlAttribute(name = "avg_time_ms")
    @XmlJavaTypeAdapter(DoubleLenientAdapter.class)
    public Double avg_time_ms;

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
