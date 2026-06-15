package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.AccessLogResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

/**
 * Repositório para chamadas de rede exclusivas do perfil MANAGER.
 */
class ManagerRepository(
    private val baseUrl: String = "https://api.gatekeeper.com",
    private val client: HttpClient = KtorClient.client
) {
    /**
     * Busca uma lista paginada de todos os logs de acesso do sistema.
     * Requer um token de autenticação.
     */
    suspend fun listAccessLogs(token: String, page: Int = 0, size: Int = 20): List<AccessLogResponse> {
        return client.get("$baseUrl/api/manager/access-logs") {
            // Adiciona o cabeçalho de autorização em cada chamada que precisa dele
            header(HttpHeaders.Authorization, "Bearer $token")
            
            // Adiciona os parâmetros de paginação na URL
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
    }
}