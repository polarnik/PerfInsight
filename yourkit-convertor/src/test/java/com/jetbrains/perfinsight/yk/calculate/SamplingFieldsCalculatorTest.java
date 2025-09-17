package com.jetbrains.perfinsight.yk.calculate;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SamplingFieldsCalculatorTest {

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
    public void calculatesSamplesPercentFromSamplingXml() throws Exception {
        View view = readFromClasspath("/sampling/Call-tree-all-threads-merged-WEB.xml");
        assertNotNull(view);
        new SamplingFieldsCalculator().doCacculate(view);

        // root
        Node root = view.nodes.get(0);
        assertEquals(100.0, root.samples_percent);

        // specified node should be 100.0 according to the issue
        String target = "WebComponent.java:346 org.glassfish.jersey.servlet.WebComponent.serviceImpl(URI, URI, HttpServletRequest, HttpServletResponse)";
        Node node = findByCallTree(root, target);
        assertNotNull(node, "Target node not found in sampling XML");
        assertEquals(100.0, node.samples_percent);
    }
}
