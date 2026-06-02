package com.webcrafterszl.gatekeeper.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class BackendConnectivityProbe(
    private val client: HttpClient = KtorClient.client,
) {
    suspend fun ping(): String {
        return runCatching {
            val response = client.get("actuator/health")
            val responseBody = response.bodyAsText().trim()

            buildString {
                append("[Gatekeeper][Ping] Front-end conectado ao back-end em ")
                append(backendBaseUrl())
                append("actuator/health -> HTTP ")
                append(response.status.value)

                if (responseBody.isNotEmpty()) {
                    append(" | resposta: ")
                    append(responseBody)
                }
            }
        }.getOrElse { throwable ->
            "[Gatekeeper][Ping] Falha ao conectar ao back-end em ${backendBaseUrl()}actuator/health: ${throwable.message}"
        }
    }
}

