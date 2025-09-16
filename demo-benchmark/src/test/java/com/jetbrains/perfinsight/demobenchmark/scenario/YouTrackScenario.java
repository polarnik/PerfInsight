package com.jetbrains.perfinsight.demobenchmark.scenario;
import com.jetbrains.perfinsight.demobenchmark.protocol.HttpUserConnection;
import com.jetbrains.perfinsight.demobenchmark.scenario.chain.GET_api_issues_ID;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.ImmutablePair;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.util.HashMap;

import static com.jetbrains.perfinsight.demobenchmark.scenario.UserEndpoints.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class YouTrackScenario {

    private final HashMap<UserEndpoints, Double> endpointsPercentMap = getUserEndpointPercentMap();
    private final HashMap<UserEndpoints, Pair<Integer, ChainBuilder>> endpointsChainMap = getUserEndpointsCount();

    String youtrackUserId_token_path = System.getProperty("youtrackUserId_token_path");
    String ISSSUE_ID_path = System.getProperty("ISSSUE_ID_path");
    String REFERRING_QUERY_path = System.getProperty("REFERRING_QUERY_path");

    private final HttpUserConnection protocolBuilders = new HttpUserConnection();

    private HashMap<UserEndpoints, Double> getUserEndpointPercentMap() {
        Integer totalCount = getUserEndpointsTotalCount();
        HashMap<UserEndpoints, Double> percents = new HashMap<>();
        getUserEndpointsCount().forEach((endpoint, count_and_chain) -> {
            percents.put(endpoint, getPercent(100.0 * count_and_chain.getLeft().doubleValue() / totalCount.doubleValue()));
        });
        return percents;
    }

    private Integer getUserEndpointsTotalCount() {
        return getUserEndpointsCount().values().stream().mapToInt(Pair::getLeft).sum();
    }



    private HashMap<UserEndpoints, Pair<Integer, ChainBuilder>> getUserEndpointsCount() {

        HashMap<UserEndpoints, Pair<Integer, ChainBuilder>> counts = new HashMap<>();
        counts.put(GET__api_issues_ID, new ImmutablePair<>(2482436, new GET_api_issues_ID().build()) );
//        counts.put("GET /api/issues", 1677095);
//        counts.put("GET /api/config", 1600009);
//        counts.put("GET /api/articles/{ID}", 709797);
//        counts.put("GET /api/workItems", 703899);
//        counts.put("POST /api/analytics", 590999);
//        counts.put("GET /api/users/me", 586217);
//        counts.put("GET /api/permissions/cache", 578592);
//        counts.put("GET /api/inbox/folders", 484804);
//        counts.put("GET /api/files/{ID}", 449479);
//        counts.put("GET /oauth", 449479);
//        counts.put("GET /api/issues/{ID}/links", 347720);
//        counts.put("GET /api/issues/{ID}/sprints", 331445);
//        counts.put("GET /api/issues/{ID}/activitiesPage", 309677);
//        counts.put("POST /api/grazie/gec", 286302);
//        counts.put("POST /api/search/assist", 272182);
//        counts.put("GET /api/users/me/profiles/analytics", 252107);
//        counts.put("POST /api/issues/{ID}/draftComment", 244119);
//        counts.put("GET /api/admin/globalSettings", 243529);
//        counts.put("GET /api/admin/timeTrackingSettings/workTimeSettings", 240071);
//        counts.put("GET /api/users/me/profiles/grazie", 237791);
//        counts.put("GET /api/admin/widgets/general", 237707);
//        counts.put("GET /api/appResources/{APP_RESOURCE}", 231151);
//        counts.put("POST /api/sortedArticles", 228204);
//        counts.put("POST /api/reports/{ID}/status", 221802);
//        counts.put("GET /api/admin/projects", 218296);
//        counts.put("POST /api/users/me/recent/issues", 198357);
//        counts.put("GET /api/issueLinkTypes", 171008);
//        counts.put("POST /api/issuesGetter", 147836);
//        counts.put("POST /api/issueListSubscription", 143602);
//        counts.put("POST /api/users/me/profiles/appearance", 139553);
//        counts.put("POST /api/markup", 111504);
//        counts.put("GET /api/savedQueries", 105638);
//        counts.put("GET /api/sortedIssues", 99255);
//        counts.put("GET /manifest.json", 94994);
//        counts.put("POST /api/users/me/drafts/{ID}", 89361);
//        counts.put("GET /issues", 88523);
//        counts.put("GET /api/tags", 85396);
//        counts.put("POST /api/issuesGetter/count", 80087);
//        counts.put("GET /api/users/notifications", 77853);
//        counts.put("GET /api/articles/{ID}/activitiesPage", 77693);
//        counts.put("GET /api/filterFields", 76452);
//        counts.put("POST /api/admin/integrations/vcsHostingProcessors", 68978);
//        counts.put("GET /api/admin/projects/{ID}/fields/{ID}/bundle/values", 61667);
//        counts.put("GET /youtrack-wide-dark.svg", 58880);
//        counts.put("POST /api/articleViews", 56288);
//        counts.put("GET /api/inbox/threads", 53501);
//        counts.put("GET /star.png", 53319);
//        counts.put("GET /api/users/me/recent/articles", 45419);
//        counts.put("GET /api/users/me/recent/issues", 44693);
//        counts.put("GET /api/admin/projects/{ID}", 41812);
//        counts.put("GET /api/users/me/articleDrafts", 40793);
//        counts.put("POST /api/vcsHooksReceiver/github/{ID}", 34265);
//        counts.put("GET /api/users/me/drafts", 33622);
//        counts.put("HEAD /issue/{ID}", 32228);
//        counts.put("POST /api/users/me/articleDrafts/{ID}", 28994);
//        counts.put("POST /api/mention", 27362);
//        counts.put("POST /api/searchPage", 24694);
//        counts.put("POST /api/projectDocuments/similarTextBased", 24221);
//        counts.put("GET /api/articles", 23807);
//        counts.put("GET /api/agiles/{ID}/sprints/{ID}", 21839);
//        counts.put("POST /api/users/me/recent/articles", 21275);
//        counts.put("POST /api/commands", 20409);
//        counts.put("GET /api/issueFolders/{ID}/sortOrder/issues", 20294);
//        counts.put("POST /api/users/me/profiles/articles", 16807);
//        counts.put("GET /api/users/me/profiles/issuesList/visibleFields", 13702);
//        counts.put("POST /api/users/me/profiles/recent/recentSearches", 12789);
//        counts.put("GET /api/activities", 11018);
//        counts.put("POST /api/issues/similar", 10302);
//        counts.put("PUT /api/issues/{ID}/draftComment", 10075);
//        counts.put("GET /api/suggestedActions", 9972);
//        counts.put("POST /api/visibilityGroups", 9752);
//        counts.put("POST /api/token", 5637);
//        counts.put("POST /api/users/me/profiles/general", 5131);
//        counts.put("POST /api/commands/assist", 4736);
//        counts.put("POST /api/users/me/drafts", 4401);
//        counts.put("POST /api/issues/", 4360);
//        counts.put("GET /api/admin/projects/{ID}/relevantTags", 4355);
//        counts.put("GET /api/agiles", 4148);
//        counts.put("POST /api/issues/{ID}/attachments", 3715);
//        counts.put("POST /api/issues", 2527);
//        counts.put("GET /api/issueFolders", 2194);
//        counts.put("POST /api/issues/{ID}/draftComment/attachments", 2072);
//        counts.put("POST /api/users/me/profiles/tips", 2030);
//        counts.put("POST /api/users/me/profiles/issuesList", 1728);
//        counts.put("POST /api/actions/applicable", 1376);
//        counts.put("POST /api/inbox/lastSeen", 1368);
//        counts.put("DELETE /api/users/me/drafts/{ID}", 992);
//        counts.put("POST /api/actions/apply/{ID}", 892);
//        counts.put("POST /api/users/me/articleDrafts/", 889);
//        counts.put("GET /api/users/me/profiles/recent", 871);
//        counts.put("POST /api/articles/", 794);
//        counts.put("GET /api/admin/workflows", 290);
//        counts.put("GET /api/filterFields/tag/values", 148);
//        counts.put("GET /api/activitiesPage", 132);
//        counts.put("POST /api/openai/v1/chat/completions", 102);
//        counts.put("GET /api/suggestedActions/{ID}", 89);
//        counts.put("GET /api/admin/customFieldSettings/customFields", 78);
        return counts;
    }

    private double getPercent(double percent) {
        return Math.max(0.0, Math.min(100.0, percent));
    }

    private ScenarioBuilder get_api_issues_ID_Scenario() {
        return CoreDsl.scenario("GET__api_issues_ID")
            .feed(csv(youtrackUserId_token_path).circular())
            .feed(csv(ISSSUE_ID_path).circular())
            .feed(csv(REFERRING_QUERY_path).circular())
            .exec(endpointsChainMap.get(GET__api_issues_ID).getRight())
            ;
    }

    public PopulationBuilder get_api_issues_ID_50_rps() {
        return get_api_issues_ID_Scenario()
            .injectOpen(CoreDsl.constantUsersPerSec(20).during(Duration.ofMinutes(10)))
            .throttle(
                reachRps(50).in(10),
                holdFor(Duration.ofMinutes(10))
            )
            .protocols(protocolBuilders.build())
            ;
    }
}
