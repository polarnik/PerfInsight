package com.jetbrains.perfinsight.yk.merge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jetbrains.perfinsight.yk.calculate.CountingFieldsCalculator;
import com.jetbrains.perfinsight.yk.calculate.SamplingFieldsCalculator;
import com.jetbrains.perfinsight.yk.calculate.SelfSamplesCalculator;
import com.jetbrains.perfinsight.yk.filter.Filter;
import com.jetbrains.perfinsight.yk.filter.FilterSampingBySamplesPercent;
import com.jetbrains.perfinsight.yk.filter.FilterSamplingByTimePercent;
import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.StackTrace;
import com.jetbrains.perfinsight.yk.model.View;
import com.jetbrains.perfinsight.yk.split.StackTraceSplitter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test covering the pipeline described in the issue:
 * 1) load sampling view from resources/sampling/Call-tree-all-threads-merged-WEB.xml
 * 2) apply FilterSampingBySamplesPercent with 1%
 * 3) serialize result to a file
 * 4) check that nodes count in file is less than original
 * 5) load count view from resources/count/Call-tree-all-threads-merged-WEB.xml
 * 6) merge with MergeSampleWithCalls
 * 7) apply CountingFieldsCalculator and SamplingFieldsCalculator
 * 8) save the result into a new file
 */
public class MergeSamplingAndCountIntegrationTest {

    private View readFromClasspath(String resource) throws Exception {
        JAXBContext context = JAXBContext.newInstance(View.class, Node.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        try (InputStream is = getClass().getResourceAsStream(resource)) {
            assertNotNull(is, "Resource not found: " + resource);
            return (View) unmarshaller.unmarshal(is);
        }
    }

    private void writeToFile(View view, Path path) throws Exception {
        JAXBContext context = JAXBContext.newInstance(View.class, Node.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(view, path.toFile());
    }

    private View readFromFile(Path path) throws Exception {
        JAXBContext context = JAXBContext.newInstance(View.class, Node.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        try (InputStream is = Files.newInputStream(path)) {
            return (View) unmarshaller.unmarshal(is);
        }
    }

    private void writeJsonToFile(View view, Path path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(path.toFile(), view);
    }

    private View readJsonFromFile(Path path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.toFile(), View.class);
    }

    private int countNodes(View view) {
        if (view == null || view.nodes == null) return 0;
        int sum = 0;
        for (Node root : view.nodes) {
            sum += countNodes(root);
        }
        return sum;
    }

    private int countNodes(Node node) {
        int c = 1;
        if (node.children != null) {
            for (Node ch : node.children) c += countNodes(ch);
        }
        return c;
    }

    private int countNodesWithCount(View view) {
        if (view == null || view.nodes == null) return 0;
        int sum = 0;
        for (Node root : view.nodes) {
            sum += countNodesWithCount(root);
        }
        return sum;
    }

    private int countNodesWithCount(Node node) {
        int c = (node.count != null) ? 1 : 0;
        if (node.children != null) {
            for (Node ch : node.children) c += countNodesWithCount(ch);
        }
        return c;
    }

    @Test
    public void fullPipeline_sampling_filter_merge_calculate_and_serialize() throws Exception {
        String currentTimestamp = String.valueOf(new Date().toInstant().getEpochSecond());
        // Build directory for temp files under project root
        Path buildDir = Paths.get(System.getProperty("user.dir"))
                .resolve("build")
                .resolve("tmp");
        Files.createDirectories(buildDir);
        // 1) Load sampling view
        View sampling = readFromClasspath("/sampling/Call-tree-all-threads-merged-WEB.xml");
        assertNotNull(sampling);
        int originalNodes = countNodes(sampling);
        assertTrue(originalNodes > 0, "Sampling view should contain nodes");

        // 2) Apply filter 1%
        Filter filter = new FilterSampingBySamplesPercent();
        View filteredSampling = filter.doFilter(sampling, 1.0);
        assertNotNull(filteredSampling);
        new SelfSamplesCalculator().doCacculate(filteredSampling);

        // 3) Serialize filtered to file (under build)
        Path tmpFiltered = buildDir.resolve(currentTimestamp + "-filtered-sampling.xml");
        writeToFile(filteredSampling, tmpFiltered);
        System.out.println("[TEST] Filtered sampling written to: " + tmpFiltered.toAbsolutePath());
        assertTrue(Files.exists(tmpFiltered));

        // 4) Read back and verify node count decreased
        View filteredBack = readFromFile(tmpFiltered);
        int filteredNodes = countNodes(filteredBack);
        assertTrue(filteredNodes < originalNodes, "Filtered nodes count must be less than original");

        // 5) Load count view
        View countView = readFromClasspath("/count/Call-tree-all-threads-merged-WEB.xml");
        assertNotNull(countView);
        new CountingFieldsCalculator().doCacculate(countView);


        // 5.1) Filter
        Filter filterCount = new FilterSamplingByTimePercent();
        View filteredCountView = filterCount.doFilter(countView, 1.0);
        Path tmpFilteredCount = buildDir.resolve(currentTimestamp + "-filtered-counting.xml");
        writeToFile(filteredCountView, tmpFilteredCount);
        System.out.println("[TEST] Filtered counting written to: " + tmpFilteredCount.toAbsolutePath());
        assertTrue(Files.exists(tmpFilteredCount));


        // 6) Merge sampling and count
        MergeSampleWithCalls merger = new MergeSampleWithCalls();
        View merged = merger.doMerge(filteredBack, filteredCountView);
        assertNotNull(merged);

        // 7) Apply calculators
        new CountingFieldsCalculator().doCacculate(merged);
        new SamplingFieldsCalculator().doCacculate(merged);
        new SelfSamplesCalculator().doCacculate(merged);

        // 8) Save final result
        Path tmpMerged = buildDir.resolve(currentTimestamp + "-merged.xml");
        writeToFile(merged, tmpMerged);
        System.out.println("[TEST] Merged view written to: " + tmpMerged.toAbsolutePath());
        assertTrue(Files.exists(tmpMerged));

        // 8.1) Also serialize final result to JSON
        Path tmpMergedJson = buildDir.resolve(currentTimestamp + "-merged.json");
        writeJsonToFile(merged, tmpMergedJson);
        System.out.println("[TEST] Merged view (JSON) written to: " + tmpMergedJson.toAbsolutePath());
        assertTrue(Files.exists(tmpMergedJson));

        // Optional sanity: unmarshal back (XML)
        View mergedBack = readFromFile(tmpMerged);
        assertNotNull(mergedBack);
        assertTrue(countNodes(mergedBack) > 0);

        // Optional sanity: read back JSON and verify
        View mergedJsonBack = readJsonFromFile(tmpMergedJson);
        assertNotNull(mergedJsonBack);
        assertTrue(countNodes(mergedJsonBack) > 0);

        // 9) Compute merged fraction (nodes with non-null count)
        int total = countNodes(mergedBack);
        int mergedWithCount = countNodesWithCount(mergedBack);
        double fraction = total > 0 ? (mergedWithCount * 1.0) / total : 0.0;
        System.out.println("[TEST] Merged nodes with count: " + mergedWithCount + "/" + total + " (" + String.format("%.1f", fraction * 100) + "%)");
        //assertTrue(fraction >= 0.90, "At least 80% nodes must be merged with non-null count");

        List<List<Node>> stack_traces = new StackTraceSplitter().split(merged);
        System.out.println(stack_traces.size());

        List< StackTrace> stack_traces2 = new StackTraceSplitter().getStackTraces(merged);
        System.out.println(stack_traces2.size());

//        // Cleanup temp files (best-effort)
//        try { Files.deleteIfExists(tmpFiltered); } catch (Exception ignored) {}
//        try { Files.deleteIfExists(tmpMerged); } catch (Exception ignored) {}
    }
}
