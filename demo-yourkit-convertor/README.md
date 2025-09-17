Profiling results are 4 GiBytes files.

The YouTrack HTTP StackTraces start with the following lines:

```java
org.glassfish.jersey.servlet.WebComponent.service(WebComponent.java)
jetbrains.youtrack.webapp.servlets.GapRestServlet.service(GapRestServlet.kt:85)
org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:319)
org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:205)
org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:799)
org.eclipse.jetty.servlet.ServletHandler$ChainEnd.doFilter(ServletHandler.java:1656)
jetbrains.youtrack.webapp.filters.ClearPrincipalFilter.doFilter(ClearPrincipalFilter.kt:16)
jetbrains.youtrack.webapp.filters.FilterAdapter.doFilter(FilterAdapter.kt:45)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.webapp.filters.SecurityResponseHeadersFilter.doFilter(SecurityResponseHeadersFilter.kt:29)
jetbrains.youtrack.webapp.filters.FilterAdapter.doFilter(FilterAdapter.kt:45)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.gaprest.filters.HttpContextAccessorFilter.doFilter(HttpContextAccessorFilter.kt:22)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.spring.controller.BaseApplicationFilter.doFilter(BaseApplicationFilter.kt:23)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.webapp.filters.MdcProviderServletFilter.doFilter(MdcProviderServletFilter.kt:17)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.webapp.filters.SitemapFilter.doFilter(SitemapFilter.kt:16)
jetbrains.youtrack.webapp.filters.FilterAdapter.doFilter(FilterAdapter.kt:45)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.webapp.filters.CorsRequestFilter.doFilter(CorsRequestFilter.kt:59)
jetbrains.youtrack.webapp.filters.FilterAdapter.doFilter(FilterAdapter.kt:45)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.webapp.filters.CloudRedirectRootRequestFilter.doFilter(CloudRedirectRootRequestFilter.kt:14)
jetbrains.youtrack.webapp.filters.FilterAdapter.doFilter(FilterAdapter.kt:45)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
jetbrains.youtrack.webapp.filters.CloudForwardFilter.doFilter(CloudForwardFilter.kt:15)
jetbrains.youtrack.webapp.filters.FilterAdapter.doFilter(FilterAdapter.kt:45)
org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:193)
org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1626)
org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:552)
org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:143)
org.eclipse.jetty.server.handler.gzip.GzipHandler.handle(GzipHandler.java:772)
org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:600)
org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:127)
org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:235)
org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:1624)
org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:233)
org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1440)
org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:188)
org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:505)
org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:1594)
org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:186)
org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1355)
org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
org.eclipse.jetty.server.handler.ContextHandlerCollection.handle(ContextHandlerCollection.java:234)
org.eclipse.jetty.server.handler.HandlerCollection.handle(HandlerCollection.java:146)
org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:127)
org.eclipse.jetty.server.Server.handle(Server.java:516)
org.eclipse.jetty.server.HttpChannel.lambda$handle$1(HttpChannel.java:487)
org.eclipse.jetty.server.HttpChannel.dispatch(HttpChannel.java:732)
org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:479)
org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:277)
org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:311)
org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:105)
org.eclipse.jetty.io.ChannelEndPoint$1.run(ChannelEndPoint.java:104)
org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.runTask(EatWhatYouKill.java:338)
org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.doProduce(EatWhatYouKill.java:315)
org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.tryProduce(EatWhatYouKill.java:173)
org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.run(EatWhatYouKill.java:131)
org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:409)
org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:883)
org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1034)
java.lang.Thread.runWith(Thread.java:1596)
java.lang.Thread.run(Thread.java:1583)
```

The filtering module filters out:
1. All stack traces with the `org.glassfish.jersey.servlet.WebComponent.service(WebComponent.java)` only
2. Distill the stack traces to the valuable 100 lines

Filters:
- the first line is:
  - `<view description="Call tree (all threads merged)">` or
  - `<view description="Call tree (by thread)">`
- the root node is:
  - `<All threads>` or
  - `qtp.+`
- the call tree includes:
  - `org.glassfish.jersey.servlet.WebComponent.service(WebComponent.java)`

### Короткий ответ (вывести строки между метками, включая сами метки, в новый файл)

- sed (самый простой способ):
  - Все такие блоки (если в файле их несколько):
    - `sed -n '/ААААААА/,/BBBBBBBB/p' input.txt > output.txt`
  - Только первый найденный блок:
    - `sed -n '/ААААААА/,/BBBBBBBB/{p;/BBBBBBBB/q}' input.txt > output.txt`

- awk (поиск как подстроки, без регэкспов):
  - Все такие блоки:
    - `awk 'index($0,"ААААААА"){p=1} p{print} index($0,"BBBBBBBB"){p=0}' input.txt > output.txt`
  - Только первый блок:
    - `awk 'index($0,"ААААААА"){p=1} p{print} index($0,"BBBBBBBB"){exit}' input.txt > output.txt`

### Пояснения
- Команда `sed -n '/start/,/end/p'` печатает все строки от первой, где совпал шаблон `start`, до строки, где совпал шаблон `end`, включая обе.
- Если в файле встречается несколько пар меток, и нужен только первый участок, используйте вариант с `{p; /end/q}` или в `awk` — `exit`.
- Если метки могут содержать спецсимволы регулярных выражений (например, `.` `[` `*` и т.п.), их нужно экранировать в `sed`. Либо используйте `awk` с `index`, который ищет простую подстроку и не требует экранирования.
- Для корректной работы с кириллицей убедитесь, что локаль — UTF‑8 (например,
