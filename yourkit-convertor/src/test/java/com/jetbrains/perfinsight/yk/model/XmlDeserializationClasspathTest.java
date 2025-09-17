package com.jetbrains.perfinsight.yk.model;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class XmlDeserializationClasspathTest {

    private View readFromClasspath(String resource) throws Exception {
        JAXBContext context = JAXBContext.newInstance(View.class, Node.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        try (InputStream is = getClass().getResourceAsStream(resource)) {
            assertNotNull(is, "Resource not found: " + resource);
            return (View) unmarshaller.unmarshal(is);
        }
    }

    @Test
    public void parseCount() throws Exception {
        View view = readFromClasspath("/count.xml");
        assertNotNull(view);
        assertEquals("Call tree (all threads merged)", view.description);
        assertNotNull(view.nodes);
        assertEquals(1, view.nodes.size());
        Node root = view.nodes.get(0);
        assertNotNull(root.call_tree);
        assertEquals(1, root.children.size());
    }

    @Test
    public void parseSample() throws Exception {
        View view = readFromClasspath("/sample.xml");
        assertNotNull(view);
        assertTrue(view.nodes.size() >= 2);
        // First root node should have one child
        Node root1 = view.nodes.get(0);
        assertEquals(1, root1.children.size());
        // Second root node should have nested children chain depth 2
        Node root2 = view.nodes.get(1);
        assertEquals(1, root2.children.size());
        assertEquals(1, root2.children.get(0).children.size());
    }
}
