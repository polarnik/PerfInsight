package com.jetbrains.perfinsight.yk.merge;

import com.jetbrains.perfinsight.yk.calculate.CountingFieldsCalculator;
import com.jetbrains.perfinsight.yk.calculate.SamplingFieldsCalculator;
import com.jetbrains.perfinsight.yk.filter.Filter;
import com.jetbrains.perfinsight.yk.filter.FilterSampingBySamplesPercent;
import com.jetbrains.perfinsight.yk.filter.FilterSamplingByTimePercent;
import com.jetbrains.perfinsight.yk.model.Node;
import com.jetbrains.perfinsight.yk.model.View;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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

    @Test
    public void fullPipeline_sampling_filter_merge_calculate_and_serialize() throws Exception {
        // 1) Load sampling view
        View sampling = readFromClasspath("/sampling/Call-tree-all-threads-merged-WEB.xml");
        assertNotNull(sampling);
        int originalNodes = countNodes(sampling);
        assertTrue(originalNodes > 0, "Sampling view should contain nodes");

        // 2) Apply filter 1%
        Filter filter = new FilterSampingBySamplesPercent();
        View filteredSampling = filter.doFilter(sampling, 10.0);
        assertNotNull(filteredSampling);

        // 3) Serialize filtered to file
        Path tmpFiltered = Files.createTempFile("filtered-sampling-", ".xml");
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

        // 5.1) Filter
        Filter filterCount = new FilterSamplingByTimePercent();
        View filteredCountView = filterCount.doFilter(countView, 5.0);
        Path tmpFilteredCount = Files.createTempFile("filtered-counting-", ".xml");
        writeToFile(filteredCountView, tmpFilteredCount);
        System.out.println("[TEST] Filtered sampling written to: " + tmpFilteredCount.toAbsolutePath());
        assertTrue(Files.exists(tmpFilteredCount));


        // 6) Merge sampling and count
        MergeSampleWithCalls merger = new MergeSampleWithCalls();
        View merged = merger.doMerge(filteredBack, filteredCountView);
        assertNotNull(merged);

        // 7) Apply calculators
        new CountingFieldsCalculator().doCacculate(merged);
        new SamplingFieldsCalculator().doCacculate(merged);

        // 8) Save final result
        Path tmpMerged = Files.createTempFile("merged-", ".xml");
        writeToFile(merged, tmpMerged);
        System.out.println("[TEST] Merged view written to: " + tmpMerged.toAbsolutePath());
        assertTrue(Files.exists(tmpMerged));

        // Optional sanity: unmarshal back
        View mergedBack = readFromFile(tmpMerged);
        assertNotNull(mergedBack);
        assertTrue(countNodes(mergedBack) > 0);

//        // Cleanup temp files (best-effort)
//        try { Files.deleteIfExists(tmpFiltered); } catch (Exception ignored) {}
//        try { Files.deleteIfExists(tmpMerged); } catch (Exception ignored) {}
    }
}
