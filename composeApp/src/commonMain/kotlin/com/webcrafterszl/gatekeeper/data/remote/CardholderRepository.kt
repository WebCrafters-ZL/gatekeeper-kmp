package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.CardholderAccessLogResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

/**
 * Repositório para chamadas de rede exclusivas do perfil CARDHOLDER.
 */
class CardholderRepository(
    private val baseUrl: String = "https://api.gatekeeper.com",
    private val client: HttpClient = KtorClient.client
) {
    /**
     * Busca a lista de logs de acesso do próprio usuário (cardholder).
     * Requer um token de autenticação.
     */
    suspend fun listOwnAccessLogs(token: String): List<CardholderAccessLogResponse> {
        return client.get("$baseUrl/api/cardholder/access-logs") {
            // Adiciona o cabeçalho de autorização
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }
}