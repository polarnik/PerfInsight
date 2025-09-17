package com.jetbrains.perfinsight.demobenchmark.simulation;


import com.jetbrains.perfinsight.demobenchmark.scenario.YouTrackScenario;
import io.gatling.javaapi.core.Simulation;

public class All_endpoints extends Simulation {
    {
        setUp(
            new YouTrackScenario().all_endpoints()
        );
    }
}
