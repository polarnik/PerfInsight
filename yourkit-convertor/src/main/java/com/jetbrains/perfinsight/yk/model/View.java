package com.jetbrains.perfinsight.yk.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "view")
@XmlAccessorType(XmlAccessType.FIELD)
public class View {
    @XmlAttribute(name = "description")
    public String description;

    // One or more top-level nodes under <view>
    @XmlElement(name = "node")
    public List<Node> nodes = new ArrayList<>();

    public View() {}
}
