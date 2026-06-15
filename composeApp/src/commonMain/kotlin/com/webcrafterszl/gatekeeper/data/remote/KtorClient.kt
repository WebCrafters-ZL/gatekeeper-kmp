package com.webcrafterszl.gatekeeper.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Objeto singleton que fornece uma instância configurada do Ktor HttpClient.
 * Esta abordagem garante que o cliente de rede seja criado apenas uma vez e reutilizado em toda a aplicação.
 */
object KtorClient {

    // A URL base muda conforme a plataforma:
    // Android usa 10.0.2.2, enquanto desktop/web usam localhost.

    /**
     * O cliente Ktor configurado para fazer as chamadas de rede.
     */
    val client: HttpClient = HttpClient {
        // Instala o plugin ContentNegotiation para lidar com a serialização/desserialização de JSON.
        install(ContentNegotiation) {
            json(Json {
                // Ignora chaves no JSON de resposta que não existem no nosso data class.
                // Isso evita que o app quebre se o back-end adicionar novos campos.
                ignoreUnknownKeys = true
            })
        }

        // Instala o plugin defaultRequest para aplicar configurações padrão a todas as requisições.
        defaultRequest {
            // Define a URL base para todas as chamadas.
            url(backendBaseUrl())
            // Define o cabeçalho padrão para indicar que estamos enviando JSON.
            contentType(ContentType.Application.Json)
        }
    }

    val httpClient: HttpClient = client
}