package com.jetbrains.perfinsight.demobenchmark.simulation;

import com.jetbrains.perfinsight.demobenchmark.scenario.YouTrackScenario;
import io.gatling.javaapi.core.Simulation;

public class GET_api_issues_50_rps extends Simulation {
    {
        setUp(
            new YouTrackScenario().get_api_issues_50_rps()
        );
    }
}
