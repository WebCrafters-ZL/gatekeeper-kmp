package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.CardholderAccessLogResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Repositório para o domínio do Portador (Cardholder).
 * Encapsula chamadas de rede para endpoints '/api/cardholder/'.
 */
class CardholderRepository(private val client: HttpClient = KtorClient.httpClient) {

    /**
     * Busca o histórico de acessos do próprio usuário autenticado.
     * @param token O token JWT do portador autenticado.
     */
    suspend fun getOwnAccessLogs(token: String, page: Int = 0, size: Int = 20): List<CardholderAccessLogResponse> {
        return client.get("api/cardholder/access-logs") {
            header(HttpHeaders.Authorization, "Bearer $token")
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
    }
}