# PerfInsight
[HKTN25-30](https://youtrack.jetbrains.com/issue/HKTN25-30/PerfInsight-tool-for-JVM-Profiles) 🔬 PerfInsight tool for JVM Profiles

Our tool speeds up performance optimization and reduces time consuming tasks for senior developers.
It distills all valuable information, extracts profile data, aligns the data with the source code, and then generates ai suggestions for optimal usage to fix bottlenecks.
Our team has resolved 200+ performance issues manually and now we have automated this process completely.

We would love to help you as well

# Problem

AI Agent Junie can work as a static code analyzer:
- summarize a source code
- find patterns and anti-patterns

but it cannot work with profiling data like:
- JFR files
- async-profiler files
- YourKit profiler files

because the files are too large

# Solution

We can create a pipeline and a set of tools for preparation and extraction performance context about the source code:

0. ⚙️ Start benchmarks
1. ⚙️ Collect performance profiles
2. 🔬 Analyze Profiles
    * Extract thread pools from profile results
    * Extract slow functions from profile results
    * Get system version info
    * Get source code for the slow classes and methods
3. 🔮 Find performance antipatterns in the selected source code
4. 🔮 Generate suggestions for improving performance
5. 🔁 Revalidate

# How To

## Performance Snapshots Comparison

PerfInsight automates the following steps:
- compare performance snapshots
- generate performance problem descriptions
- generate performance problem suggestions

From 30 MiByte compressed StackTraces to two clear performance improvement suggestions.

1. YourKit API: get performance snapshots without and with degradation. See attachments from the issue [JT-91297](https://youtrack.jetbrains.com/issue/JT-91297/Investigate-performance-impact-of-search-features-computation?backToIssues=false) Investigate performance impact of search features computation
2. Convert the snapshots to the CSV format. See the module **demo-yourkit-convertor**
3. Compare snapshots via the module **core**: `java -jar perfinsight.jar <baseline-csv-file> <degradation-csv-file>`, see the module **snapshot-analyzer**
4. The **core** module will generage the Performance Problem descriptions in the Markdown format via the AI Agent. See the module **adviser**

There are results:
- [JT-91664](https://youtrack.jetbrains.com/issue/JT-91664/Performance-Optimization-Suggestions-for-getFeatures) Performance Optimization Suggestions for getFeatures
- [JT-91662](https://youtrack.jetbrains.com/issue/JT-91662/Performance-Analysis-and-Suggestions-for-buildIssueTree) Performance Analysis and Suggestions for buildIssueTree

## Performance Snapshots Merging

PerfInsight automates the following steps:
- merge and distill performance snapshots
- generate performance problem descriptions
- generate performance problem suggestions

From 220 MiByte compressed StackTraces to one clear performance improvement suggestions.

1. Junit and Gatling send requests, see the module **demo-benchmark**
   2. `./gradlew :demo-benchmark:test --tests com.jetbrains.perfinsight.demobenchmark.PerformanceTest.GET_api_issues_50_rps`
   3. `./gradlew :demo-benchmark:test --tests com.jetbrains.perfinsight.demobenchmark.PerformanceTest.GET_api_issues_100_calls`
1. YourKit API: get performance snapshots with sampling and method counting metrics.
2. Convert the snapshots into the XML format. See the module **demo-yourkit-convertor**
3. Merge and distill snapshots via the module **yourkit-convertor**: `./gradlew :yourkit-convertor:test --tests com.jetbrains.perfinsight.yk.merge.MergeSamplingAndCountIntegrationTest.fullPipeline_sampling_filter_merge_calculate_and_serialize`
4. Generage the Performance Problem descriptions. See the module **adviser**

There is a result:
- [JT-91665](https://youtrack.jetbrains.com/issue/JT-91665/Serialization-Performance-Bottleneck-Due-to-Inflexible-Pagination-Logic) Serialization Performance Bottleneck Due to Inflexible Pagination Logic

## Stack Trace Format

The stack trace format is based on JVM Stack Traces with additional information:
- Performance metrics about the whole StackTrace:
  - example: `Performance Problem with 5216 samples and 100478.0 ms`
- Performance metrics about the current line:
  - example: `time: 2.120882241575161%	loop: 28333.333333333332`

<details>

<summary>Example</summary>

```java
Performance Problem with 5216 samples and 100478.0 ms:
 	at com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serializeTypedContents(List, JsonGenerator, SerializerProvider)(IndexedListSerializer.java:92)
	at jetbrains.gap.resource.pojo.GapBeanSerializer.serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)(IndexedListSerializer.java:181)		loop: 1650.0
	at jetbrains.gap.resource.pojo.GapBeanSerializer.doSerializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)(GapBeanSerializer.kt:85)
	at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)(GapBeanSerializer.kt:72)
	at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFieldsFiltered(Object, JsonGenerator, SerializerProvider)(BeanSerializerBase.java:655)
	at jetbrains.gap.resource.pojo.FilteringPropertyFilter.serializeAsField(Object, JsonGenerator, SerializerProvider, PropertyWriter)(BeanSerializerBase.java:825)		loop: 71.63636363636364
	at jetbrains.gap.resource.pojo.FilterByProvidesWriter.serializeAsField(Object, Page, JsonGenerator, SerializerProvider)(FilteringPropertyFilter.kt:86)
	at jetbrains.gap.resource.pojo.FilterByProvidesWriter.doWrite(JsonGenerator, SerializerProvider, JsonSerializer, Object)(FilterByProvidesWriter.kt:81)
	at com.fasterxml.jackson.databind.ser.std.CollectionSerializer.serialize(Object, JsonGenerator, SerializerProvider)(FilterByProvidesWriter.kt:128)
	at com.fasterxml.jackson.databind.ser.std.CollectionSerializer.serialize(Collection, JsonGenerator, SerializerProvider)(CollectionSerializer.java:25)
	at com.fasterxml.jackson.databind.ser.std.CollectionSerializer.serializeContents(Collection, JsonGenerator, SerializerProvider)(CollectionSerializer.java:111)
	at jetbrains.gap.resource.pojo.GapBeanSerializer.serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)(CollectionSerializer.java:147)
	at jetbrains.gap.resource.pojo.GapBeanSerializer.doSerializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)(GapBeanSerializer.kt:85)
	at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeWithType(Object, JsonGenerator, SerializerProvider, TypeSerializer)(GapBeanSerializer.kt:72)
	at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFieldsFiltered(Object, JsonGenerator, SerializerProvider)(BeanSerializerBase.java:655)
	at jetbrains.gap.resource.pojo.FilteringPropertyFilter.serializeAsField(Object, JsonGenerator, SerializerProvider, PropertyWriter)(BeanSerializerBase.java:825)	time: 2.120882241575161%	loop: 28333.333333333332
	at jetbrains.gap.resource.pojo.FilterByProvidesWriter.serializeAsField(Object, Page, JsonGenerator, SerializerProvider)(FilteringPropertyFilter.kt:86)
	at java.lang.reflect.Method.invoke(Object, Object[])(FilterByProvidesWriter.kt:34)
	at jdk.internal.reflect.DirectMethodHandleAccessor.invoke(Object, Object[])(Method.java:580)
	at jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(Object, Object[])(DirectMethodHandleAccessor.java:103)
	at java.lang.invoke.Invokers$Holder.invokeExact_MT(Object, Object, Object)(DirectMethodHandleAccessor.java:153)	time: 7.394875522442304%
	at java.lang.invoke.LambdaForm$MH.0x00000008029b2c00.invoke(Object, Object)(Invokers$Holder:null)
	at java.lang.invoke.DirectMethodHandle$Holder.invokeVirtual(Object, Object)(LambdaForm$MH:null)
	at jetbrains.charisma.persistent.IssueComment.getTextPreview()(DirectMethodHandle$Holder:null)
	at jetbrains.gap.resource.metadata.ReadOnlyDelegateImpl.getValue(Object, KProperty)(IssueComment.kt:40)
	at jetbrains.gap.resource.metadata.ReadOnlyDelegateImpl.getValue(Entity, KProperty)(ReadOnlyDelegates.kt:33)
	at jetbrains.charisma.persistent.IssueComment$textPreview$2.invoke()(ReadOnlyDelegates.kt:39)
	at jetbrains.charisma.persistent.IssueComment$textPreview$2.invoke()(IssueComment.kt:40)	time: 2.3142107996503984%
	at jetbrains.youtrack.markup.MarkupProcessorFactoryImpl$Builder.render(XdTextContainer)(IssueComment.kt:45)
	at jetbrains.youtrack.markup.CachingMarkdownProcessor.render(XdTextContainer, boolean, boolean, boolean, boolean)(MarkupProcessorFactoryImpl.kt:128)
	at jetbrains.youtrack.markup.MarkdownProcessor.render(XdTextContainer, boolean, boolean, boolean, boolean)(CachingMarkdownProcessor.kt:46)
	at jetbrains.youtrack.markup.MarkdownProcessingContextKt.markdownContext(XdTextContainer, boolean, boolean, boolean, Function0)(MarkdownProcessor.kt:161)		loop: 1666.6666666666667
	at jetbrains.youtrack.markup.MarkdownProcessingContext.execute(XdTextContainer, boolean, boolean, boolean, Function0)(MarkdownProcessingContext.kt:55)
	at jetbrains.youtrack.markup.MarkdownProcessor$render$2$1.invoke()(MarkdownProcessingContext.kt:38)
	at jetbrains.youtrack.markup.MarkdownProcessor$render$2$1.invoke()(MarkdownProcessor.kt:161)
	at jetbrains.youtrack.markup.MarkdownProcessor$Engine.render(String)(MarkdownProcessor.kt:162)
	at org.commonmark.renderer.html.HtmlRenderer.render(Node)(MarkdownProcessor.kt:58)
	at org.commonmark.renderer.html.HtmlRenderer.render(Node, Appendable)(HtmlRenderer.java:77)	time: 6.4956510920339765%
	at org.commonmark.renderer.html.HtmlRenderer$RendererContext.render(Node)(HtmlRenderer.java:68)
	at org.commonmark.internal.renderer.NodeRendererMap.render(Node)(HtmlRenderer.java:296)
	at org.commonmark.renderer.html.CoreHtmlNodeRenderer.render(Node)(NodeRendererMap.java:23)
	at org.commonmark.node.Document.accept(Visitor)(CoreHtmlNodeRenderer.java:49)
	at org.commonmark.renderer.html.CoreHtmlNodeRenderer.visit(Document)(Document.java:7)
	at org.commonmark.renderer.html.CoreHtmlNodeRenderer.visitChildren(Node)(CoreHtmlNodeRenderer.java:55)
	at org.commonmark.renderer.html.HtmlRenderer$RendererContext.render(Node)(CoreHtmlNodeRenderer.java:252)		loop: 11.0
	at org.commonmark.internal.renderer.NodeRendererMap.render(Node)(HtmlRenderer.java:296)
	at org.commonmark.renderer.html.CoreHtmlNodeRenderer.render(Node)(NodeRendererMap.java:23)
	at org.commonmark.node.Paragraph.accept(Visitor)(CoreHtmlNodeRenderer.java:49)
	at org.commonmark.renderer.html.CoreHtmlNodeRenderer.visit(Paragraph)(Paragraph.java:10)	time: 1.8621274533559486%
	at org.commonmark.renderer.html.CoreHtmlNodeRenderer.visitChildren(Node)(CoreHtmlNodeRenderer.java:75)
	at org.commonmark.renderer.html.HtmlRenderer$RendererContext.render(Node)(CoreHtmlNodeRenderer.java:252)
	at org.commonmark.internal.renderer.NodeRendererMap.render(Node)(HtmlRenderer.java:296)
	at jetbrains.youtrack.markup.renderer.EntityLinkRenderer.render(Node)(NodeRendererMap.java:23)	time: 9.327900726961992%
	at jetbrains.youtrack.markup.extensions.IssueLink.getNavigable()(EntityLinkRenderer.kt:25)
	at kotlin.SynchronizedLazyImpl.getValue()(MarkdownEntityLinks.kt:63)	time: 1.9730118672701107%
	at jetbrains.youtrack.markup.extensions.IssueLink$navigable$2.invoke()(LazyJVM.kt:83)
	at jetbrains.youtrack.markup.extensions.IssueLink$navigable$2.invoke()(MarkdownEntityLinks.kt:63)
	at jetbrains.youtrack.markup.extensions.IssueLink.getAccessible()(MarkdownEntityLinks.kt:63)
	at kotlin.SynchronizedLazyImpl.getValue()(MarkdownEntityLinks.kt:62)
	at jetbrains.youtrack.markup.extensions.IssueLink$accessible$2.invoke()(LazyJVM.kt:83)
	at jetbrains.youtrack.markup.extensions.IssueLink$accessible$2.invoke()(MarkdownEntityLinks.kt:62)
	at jetbrains.youtrack.persistent.XdIssueExtKt.isAccessible$default(XdIssue, Operation, XdUser, int, Object)(MarkdownEntityLinks.kt:62)	time: 1.4898892736522593%
	at jetbrains.youtrack.persistent.XdIssueExtKt.isAccessible(XdIssue, Operation, XdUser)(XdIssueExt.kt:150)
	at jetbrains.youtrack.persistent.security.IssueSecurity.isAccessible(XdIssue, Operation, XdUser)(XdIssueExt.kt:151)
	at jetbrains.youtrack.persistent.security.IssueSecurity.isAccessible(XdIssue, Operation, XdUser, boolean)(IssueSecurity.kt:31)	time: 19.5073698856472%
	at jetbrains.youtrack.persistent.security.IssueSecurity.isReadAccessibleInProject(SecurityCompatible, XdUser, XdProject)(IssueSecurity.kt:65)	time: 12.972316002700877%
	at jetbrains.youtrack.core.security.AcessUtilsKt.isPermitted(SecurityCompatible, XdUser)(IssueSecurity.kt:138)
	at kotlinx.dnq.query.XdQueryKt.lastOrNull(XdQuery)(AcessUtils.kt:23)	time: 9.926231926822071%
	at jetbrains.exodus.entitystore.iterate.EntityFromLinksIterable.getLast()(XdQuery.kt:630)	time: 35.30760663041342%
	at jetbrains.exodus.env.CursorImpl.getSearchKeyRange(ByteIterable)(EntityFromLinksIterable.java:109)	time: 4.739720478023091%
	at jetbrains.exodus.tree.patricia.PatriciaCursorDecorator.getSearchKeyRange(ByteIterable)(CursorImpl.java:110)	time: 25.770784605570913%
	at jetbrains.exodus.tree.TreeCursor.getSearchKeyRange(ByteIterable)(PatriciaCursorDecorator.java:237)
	at jetbrains.exodus.tree.TreeCursor.moveTo(ByteIterable, ByteIterable, boolean)(TreeCursor.java:148)
	at jetbrains.exodus.tree.patricia.PatriciaTraverser.moveToRange(ByteIterable, ByteIterable)(TreeCursor.java:187)	time: 100.0%
```

</details>
