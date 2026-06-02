package com.webcrafterszl.gatekeeper.data.model

import kotlinx.serialization.Serializable

/**
 * Define os papéis (roles) de acesso suportados pelo back-end.
 */
@Serializable
enum class Role {
    ADMIN,
    MANAGER,
    CARDHOLDER
}

/**
 * DTO para requisição de login.
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * DTO para resposta de autenticação.
 */
@Serializable
data class AuthResponse(
    val message: String,
    val token: String
)

/**
 * DTO para requisição de validação do código OTP.
 */
@Serializable
data class ValidateOtpRequest(
    val code: String,
    val password: String,
    val email: String
)

/**
 * DTO que representa os dados retornados de um usuário da aplicação.
 */
@Serializable
data class AppUserResponse(
    val id: Long, // Usando Long para IDs para garantir compatibilidade com bancos de dados relacionais padrão
    val fullName: String,
    val email: String,
    val role: Role,
    val isActive: Boolean
)

/**
 * DTO que representa um ponto de acesso (ex: catraca, porta).
 */
@Serializable
data class AccessPointResponse(
    val id: Long,
    val mqttIdentifier: String,
    val locationDescription: String,
    val isUnderMaintenance: Boolean
)

/**
 * DTO que representa uma credencial RFID registrada.
 */
@Serializable
data class RfidCredentialResponse(
    val id: Long,
    val hexCode: String,
    val appUserId: Long,
    val isBlocked: Boolean
)

/**
 * DTO que representa o registro (log) de um acesso geral no sistema.
 */
@Serializable
data class AccessLogResponse(
    val id: Long,
    val tagRead: String,
    val accessPointId: Long,
    val timestamp: String, // String é o padrão mais seguro para ISO-8601 em APIs JSON com kotlinx.serialization
    val isGranted: Boolean,
    val denialReason: String? = null // Nullable pois se o acesso for garantido, não haverá motivo de negação
)

/**
 * DTO que representa o registro (log) de acesso específico visualizado por um Portador (Cardholder).
 */
@Serializable
data class CardholderAccessLogResponse(
    val id: Long,
    val tagRead: String,
    val accessPointId: Long,
    val timestamp: String,
    val isGranted: Boolean,
    val denialReason: String? = null
)