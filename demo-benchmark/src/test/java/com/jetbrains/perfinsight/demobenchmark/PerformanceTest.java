package com.jetbrains.perfinsight.demobenchmark;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.jetbrains.perfinsight.demobenchmark.simulation.All_endpoints;
import com.jetbrains.perfinsight.demobenchmark.simulation.GET_api_issues_100_calls;
import com.jetbrains.perfinsight.demobenchmark.simulation.GET_api_issues_50_rps;
import com.jetbrains.perfinsight.demobenchmark.simulation.GET_api_issues_ID_50_rps;
import io.gatling.app.Gatling;
import io.gatling.shared.cli.CliOption;
import io.gatling.shared.cli.GatlingCliOptions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class PerformanceTest {
    void runSimulation(Class simulationClass) {
        runSimulation(simulationClass, "");
    }

    void runSimulation(Class simulationClass, String description) {
        String[] gatlingArgs = {
            config(GatlingCliOptions.Simulation, simulationClass.getCanonicalName()),
            config(GatlingCliOptions.ResultsFolder, "build/gatling-report"),
            config(GatlingCliOptions.RunDescription, description)
        };
        Gatling.main(gatlingArgs);
    }
    private String config(CliOption option, String value) {
        return "--" + option.longName + "=" + value;
    }


    @Test @Tag("GET_api_issues_ID")
    public void GET_api_issues_ID_50_rps() {
        System.setProperty("youtrack", "http://localhost:8080");
        System.setProperty("gatling.ssl.useOpenSsl", "false");
        System.setProperty("gatling.data.console.writePeriod", "5");
        System.setProperty("gatling.http.requestTimeout", "20000");
        System.setProperty("gatling.charting.indicators.lowerBound", "500");
        System.setProperty("gatling.charting.indicators.higherBound", "1000");
        System.setProperty("youtrackUserId_token_path", "data/token.users.csv");
        System.setProperty("ISSSUE_ID_path", "data/ISSUE_ID.csv");
        System.setProperty("REFERRING_QUERY_path", "data/REFERRING_QUERY.csv");

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        runSimulation(GET_api_issues_ID_50_rps.class);
    }


    @Test
    public void GET_api_issues_50_rps() {
        System.setProperty("youtrack", "http://localhost:8080");
        System.setProperty("gatling.ssl.useOpenSsl", "false");
        System.setProperty("gatling.data.console.writePeriod", "30");
        System.setProperty("gatling.http.requestTimeout", "20000");
        System.setProperty("gatling.charting.indicators.lowerBound", "500");
        System.setProperty("gatling.charting.indicators.higherBound", "1000");
        System.setProperty("youtrackUserId_token_path", "data/USER_TOKEN_WITH_TASKS.csv");
        System.setProperty("proxy_host", "172.31.18.225");
        System.setProperty("proxy_port", "3128");

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        runSimulation(GET_api_issues_50_rps.class);
    }

    @Test
    public void GET_api_issues_100_calls() {
        System.setProperty("youtrack", "http://localhost:8080");
        System.setProperty("gatling.ssl.useOpenSsl", "false");
        System.setProperty("gatling.data.console.writePeriod", "10");
        System.setProperty("gatling.http.requestTimeout", "90000");
        System.setProperty("gatling.charting.indicators.lowerBound", "500");
        System.setProperty("gatling.charting.indicators.higherBound", "1000");
        System.setProperty("youtrackUserId_token_path", "data/USER_TOKEN_WITH_TASKS.csv");
        System.setProperty("proxy_host", "172.31.18.225");
        System.setProperty("proxy_port", "3128");

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        runSimulation(GET_api_issues_100_calls.class);
    }

    @Test
    public void All_endpoints() {
        System.setProperty("youtrack", "http://localhost:8080");
        System.setProperty("gatling.ssl.useOpenSsl", "false");
        System.setProperty("gatling.data.console.writePeriod", "30");
        System.setProperty("gatling.http.requestTimeout", "20000");
        System.setProperty("gatling.charting.indicators.lowerBound", "500");
        System.setProperty("gatling.charting.indicators.higherBound", "1000");
        System.setProperty("youtrackUserId_token_path", "data/token.users.csv");
        System.setProperty("ISSSUE_ID_path", "data/ISSUE_ID.csv");
        System.setProperty("REFERRING_QUERY_path", "data/REFERRING_QUERY.csv");

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        runSimulation(All_endpoints.class);
    }
}
