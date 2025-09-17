package com.jetbrains.perfinsight.demobenchmark.scenario.chain;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;

import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.group;
import static io.gatling.javaapi.core.CoreDsl.percent;
import static io.gatling.javaapi.http.HttpDsl.http;

public class GET_api_issues {
    public ChainBuilder build() {
        // query=Assignee:+me+sort+by:+updated+#Unresolved&$top=100&fields=id,idReadable,updated,created,tags(color(foreground,background),name),project(shortName),links(value,direction,issues(idReadable),linkType(name,sourceToTarget,targetToSource),id),comments(id,textPreview,created,updated,author(name,login),deleted),summary,wikifiedDescription,customFields(name,color,value(name,minutes,presentation,markdownText,color(background,foreground)),id,projectCustomField(emptyFieldText)),resolved,attachments(name,url),reporter(login)
        String top1_fields = "id,idReadable,updated,created,tags(color(foreground,background),name),project(shortName),links(value,direction,issues(idReadable),linkType(name,sourceToTarget,targetToSource),id),comments(id,textPreview,created,updated,author(name,login),deleted),summary,wikifiedDescription,customFields(name,color,value(name,minutes,presentation,markdownText,color(background,foreground)),id,projectCustomField(emptyFieldText)),resolved,attachments(name,url),reporter(login)";
        ChainBuilder getIssues_top1_6286 = group("TOP-1").on(
            http("GET /api/issues")
                .get("/api/issues")
                .header("Authorization", "Bearer #{token}")
                .header("user-agent", "YouTrack IDE Plugin")
                .queryParamSeq(List.of(
                    Map.entry("query", "Assignee: me sort by: updated #Unresolved"),
                    Map.entry("$top", 100),
                    Map.entry("fields", top1_fields)
                ))

        );
        return group("GET /api/issues").on(CoreDsl.randomSwitch().on(
                percent(100.0).then(getIssues_top1_6286)
            )
        );
    }
}
