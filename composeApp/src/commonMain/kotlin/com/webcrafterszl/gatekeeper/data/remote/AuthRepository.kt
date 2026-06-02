package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.AuthResponse
import com.webcrafterszl.gatekeeper.data.model.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Repositório responsável pelas chamadas de rede relacionadas à autenticação.
 * Ele encapsula a lógica de comunicação com os endpoints de '/api/auth/'.
 */
class AuthRepository(
    // Injetamos o cliente Ktor para facilitar os testes e a reutilização.
    private val client: HttpClient = KtorClient.httpClient 
) {

    /**
     * Envia as credenciais do usuário para o back-end para tentar realizar o login.
     *
     * @param request O objeto LoginRequest contendo e-mail e senha.
     * @return Um objeto AuthResponse contendo a mensagem e o token JWT em caso de sucesso.
     */
    suspend fun fazerLogin(request: LoginRequest): AuthResponse {
        // Realiza uma chamada POST para a rota 'api/auth/login'.
        // A URL base ("http://10.0.2.2:8000/" no Android e "http://localhost:8000/" no desktop/web)
        // já está configurada no KtorClient.
        return client.post("api/auth/login") {
            // Define o corpo da requisição com o nosso objeto de login.
            // O Ktor se encarrega de serializá-lo para JSON.
            setBody(request)
        }.body() // O Ktor desserializa a resposta JSON para o nosso data class AuthResponse.
    }
}