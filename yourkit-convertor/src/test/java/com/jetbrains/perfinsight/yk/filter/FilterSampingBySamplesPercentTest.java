package com.jetbrains.perfinsight.yk.filter;

import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FilterSampingBySamplesPercentTest {

    private View readFromClasspath(String resource) throws Exception {
        JAXBContext context = JAXBContext.newInstance(View.class, Node.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        try (InputStream is = getClass().getResourceAsStream(resource)) {
            assertNotNull(is, "Resource not found: " + resource);
            return (View) unmarshaller.unmarshal(is);
        }
    }

    @Test
    public void filterBySamplesPercent_removes_deep_nodes_below_threshold() throws Exception {
        View view = readFromClasspath("/sample.2.xml");
        assertNotNull(view);
        assertEquals(1, view.nodes.size());

        Filter filter = new FilterSampingBySamplesPercent();
        View filtered = filter.doFilter(view, 2.0);
        assertNotNull(filtered);
        assertEquals(1, filtered.nodes.size());

        // Collect call_tree strings from the filtered tree
        List<String> callTrees = collectCallTrees(filtered);

        // Must contain the first two entries
        assertTrue(callTrees.stream().anyMatch(s -> s.equals("GapRestServlet.kt:85 org.glassfish.jersey.servlet.WebComponent.service(URI, URI, HttpServletRequest, HttpServletResponse)")));
        assertTrue(callTrees.stream().anyMatch(s -> s.equals("WebComponent.java:346 org.glassfish.jersey.servlet.WebComponent.serviceImpl(URI, URI, HttpServletRequest, HttpServletResponse)")));

        // Must NOT contain the next three deep nodes
        assertFalse(callTrees.stream().anyMatch(s -> s.equals("WebComponent.java:394 org.glassfish.jersey.server.ApplicationHandler.handle(ContainerRequest)")));
        assertFalse(callTrees.stream().anyMatch(s -> s.equals("ApplicationHandler.java:684 org.glassfish.jersey.server.ServerRuntime.process(ContainerRequest)")));
        assertFalse(callTrees.stream().anyMatch(s -> s.equals("ServerRuntime.java:234 org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestContext, Runnable)")));
    }

    private List<String> collectCallTrees(View v) {
        return v.nodes.stream().flatMap(n -> collect(n).stream()).collect(Collectors.toList());
    }

    private List<String> collect(Node node) {
        List<String> res = new java.util.ArrayList<>();
        res.add(node.call_tree);
        for (Node ch : node.children) {
            res.addAll(collect(ch));
        }
        return res;
    }
}
