package com.jetbrains.perfinsight.yk.calculate;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CountingFieldsCalculatorTest {

    private View readFromClasspath(String resource) throws Exception {
        JAXBContext context = JAXBContext.newInstance(View.class, Node.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        try (InputStream is = getClass().getResourceAsStream(resource)) {
            assertNotNull(is, "Resource not found: " + resource);
            return (View) unmarshaller.unmarshal(is);
        }
    }

    private Node findByCallTree(Node node, String callTree) {
        if (Objects.equals(node.call_tree, callTree)) return node;
        for (Node child : node.children) {
            Node found = findByCallTree(child, callTree);
            if (found != null) return found;
        }
        return null;
    }

    @Test
    public void calculatesCountMultiplicatorFromCountXml() throws Exception {
        View view = readFromClasspath("/count.xml");
        assertNotNull(view);
        new CountingFieldsCalculator().doCacculate(view);

        // Root node should have multiplicator 1 when count not null
        Node root = view.nodes.get(0);
        assertEquals(1.0, root.count_multiplicator);

        // Middle node: same count as root (108/108) => 1
        Node middle = root.children.get(0);
        assertEquals(1.0, middle.count_multiplicator);

        // Target node should be 1080/108 = 10.0
        String target = "WebComponent.java:1000 org.glassfish.jersey.servlet.WebComponent.serviceImpl(URI)";
        Node leaf = findByCallTree(root, target);
        assertNotNull(leaf, "Target node not found in test XML");
        assertEquals(10.0, leaf.count_multiplicator);
    }
}
