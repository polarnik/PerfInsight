package com.jetbrains.perfinsight.demobenchmark.scenario;
import com.jetbrains.perfinsight.demobenchmark.protocol.HttpUserConnection;
import com.jetbrains.perfinsight.demobenchmark.scenario.chain.GET_api_issues;
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
        counts.put(GET__api_issues, new ImmutablePair<>(1677095, new GET_api_issues().build()));
//        counts.put("GET /api/config", 1600009);
//        counts.put("GET /api/articles/{ID}", 709797);
//        counts.put("GET /api/workItems", 703899);
//        counts.put("POST /api/analytics", 590999);
//        counts.put("GET /api/users/me", 586217);
//        counts.put("GET /api/permissions/cache", 578592);
//        counts.put("GET /api/inbox/folders", 484804);
//        counts.put("GET /api/files/{ID}", 449479);
        return counts;
    }

    private double getPercent(double percent) {
        return Math.max(0.0, Math.min(100.0, percent));
    }

    private ScenarioBuilder all_endpoints_Scenario() {
        return CoreDsl.scenario("all_endpoints")
            .feed(csv(youtrackUserId_token_path).circular())
            .feed(csv(ISSSUE_ID_path).circular())
            .feed(csv(REFERRING_QUERY_path).circular())
            .randomSwitch().on(
                percent(endpointsPercentMap.get(GET__api_issues_ID))
                    .then(endpointsChainMap.get(GET__api_issues_ID).getRight()),
                percent(endpointsPercentMap.get(GET__api_issues))
                    .then(endpointsChainMap.get(GET__api_issues).getRight())
            )
            ;
    }

    private ScenarioBuilder get_api_issues_ID_Scenario() {
        return CoreDsl.scenario("GET__api_issues_ID")
            .feed(csv(youtrackUserId_token_path).circular())
            .feed(csv(ISSSUE_ID_path).circular())
            .feed(csv(REFERRING_QUERY_path).circular())
            .exec(endpointsChainMap.get(GET__api_issues_ID).getRight())
            ;
    }

    private ScenarioBuilder get_api_issues_Scenario() {
        return CoreDsl.scenario("GET__api_issues")
            .feed(csv(youtrackUserId_token_path).circular())
            .exec(endpointsChainMap.get(GET__api_issues).getRight())
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

    public PopulationBuilder get_api_issues_50_rps() {
        return get_api_issues_Scenario()
            .injectOpen(CoreDsl.constantUsersPerSec(20).during(Duration.ofMinutes(10)))
            .throttle(
                reachRps(50).in(10),
                holdFor(Duration.ofMinutes(10 + 1))
            )
            .protocols(protocolBuilders.build())
            ;
    }
    public PopulationBuilder get_api_issues_100_calls() {
        int calls = 100;
        int one_call_duration_s = 4;
        return get_api_issues_Scenario()
            .injectOpen(CoreDsl.constantUsersPerSec(1.0 / one_call_duration_s)
                .during(Duration.ofSeconds(calls * one_call_duration_s)))
            .protocols(protocolBuilders.build())
            ;
    }

    public PopulationBuilder all_endpoints() {
        return all_endpoints_Scenario()
            .injectOpen(CoreDsl.constantUsersPerSec(20).during(Duration.ofMinutes(2)))
            .throttle(
                reachRps(100).in(10),
                holdFor(Duration.ofMinutes(2))
            )
            .protocols(protocolBuilders.build())
            ;
    }
}
