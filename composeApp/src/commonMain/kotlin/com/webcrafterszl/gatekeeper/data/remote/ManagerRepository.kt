package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Repositório para o domínio do Gestor (Manager).
 * Encapsula chamadas de rede para endpoints '/api/manager/'.
 * Muitas dessas operações (como bloquear credenciais) disparam eventos MQTT no backend.
 */
class ManagerRepository(private val client: HttpClient = KtorClient.httpClient) {

    // --- Access Logs ---

    suspend fun getAccessLogs(token: String, page: Int = 0, size: Int = 20): List<AccessLogResponse> {
        return client.get("api/manager/access-logs") {
            header(HttpHeaders.Authorization, "Bearer $token")
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
    }

    // --- Access Points ---

    suspend fun getAccessPoints(token: String): List<AccessPointResponse> {
        return client.get("api/manager/access-points") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun createAccessPoint(token: String, request: CreateAccessPointRequest): AccessPointResponse {
        return client.post("api/manager/access-points") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // --- Cardholders ---

    suspend fun getCardholders(token: String): List<CardholderResponse> {
        return client.get("api/manager/cardholders") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun createCardholder(token: String, request: CreateCardholderRequest): CardholderResponse {
        return client.post("api/manager/cardholders") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateCardholder(token: String, id: Long, request: UpdateCardholderRequest): CardholderResponse {
        return client.patch("api/manager/cardholders/$id") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // --- RFID Credentials ---

    suspend fun getRfidCredentials(token: String): List<RfidCredentialResponse> {
        return client.get("api/manager/rfid-credentials") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun createRfidCredential(token: String, request: CreateRfidCredentialRequest): RfidCredentialResponse {
        return client.post("api/manager/rfid-credentials") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    /**
     * Bloqueia ou desbloqueia uma credencial.
     * Esta ação dispara uma sincronização de cache via MQTT para os dispositivos IoT no backend.
     */
    suspend fun updateCredentialStatus(token: String, id: Long, isBlocked: Boolean): RfidCredentialResponse {
        return client.patch("api/manager/rfid-credentials/$id/status") {
            header(HttpHeaders.Authorization, "Bearer $token")
            url { parameters.append("isBlocked", isBlocked.toString()) }
        }.body()
    }
}