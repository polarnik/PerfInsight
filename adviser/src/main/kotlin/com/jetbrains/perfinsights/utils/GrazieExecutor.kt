package com.jetbrains.utils

import ai.grazie.api.gateway.client.SuspendableAPIGatewayClient
import ai.grazie.client.common.SuspendableClientWithBackoff
import ai.grazie.client.common.SuspendableHTTPClient
import ai.grazie.client.ktor.GrazieKtorHTTPClient
import ai.grazie.model.auth.GrazieAgent
import ai.grazie.model.auth.v5.AuthData
import ai.jetbrains.code.prompt.executor.clients.grazie.koog.GrazieLLMClient
import ai.jetbrains.code.prompt.executor.clients.grazie.koog.model.GrazieEnvironment
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor

/**
 * Creates a simple Grazie executor for interacting with AI models.
 *
 * @param token The Grazie API token
 * @return A SingleLLMPromptExecutor configured with the provided token
 */
fun simpleGrazieExecutor(token: String): SingleLLMPromptExecutor {
    val client = SuspendableAPIGatewayClient(
        GrazieEnvironment.Production.url,
        SuspendableHTTPClient.WithV5(
            SuspendableClientWithBackoff(
                GrazieKtorHTTPClient.Client.Default,
            ), AuthData(
                token,
                grazieAgent = GrazieAgent("performance-adviser", "dev")
            )
        )
    )

    val executor = SingleLLMPromptExecutor(GrazieLLMClient(client))
    return executor
}
