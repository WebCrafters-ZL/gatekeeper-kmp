package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.AppUserResponse
import com.webcrafterszl.gatekeeper.data.model.CreateManagerRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Repositório para o domínio de Administração.
 * Encapsula chamadas de rede para endpoints '/api/admin/', que exigem privilégios de ADMIN.
 */
class AdminRepository(private val client: HttpClient = KtorClient.httpClient) {

    /**
     * Cria um novo usuário com o perfil de Manager.
     * @param token O token JWT do administrador autenticado.
     * @param request O corpo da requisição com os dados do novo manager.
     * @return Os dados do usuário recém-criado.
     */
    suspend fun createManager(token: String, request: CreateManagerRequest): AppUserResponse {
        return client.post("api/admin/managers") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}