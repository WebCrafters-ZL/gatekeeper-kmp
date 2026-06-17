package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Repositório para o domínio de Autenticação.
 * Encapsula todas as chamadas de rede para os endpoints públicos em '/api/auth/'.
 */
class AuthRepository(private val client: HttpClient = KtorClient.httpClient) {

    /**
     * Realiza o login do usuário.
     */
    suspend fun login(request: LoginRequest): AuthResponse {
        return client.post("api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    /**
     * Inicia o fluxo de recuperação de senha.
     */
    suspend fun forgotPassword(request: ForgotPasswordRequest) {
        client.post("api/auth/forgot-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    /**
     * Conclui o fluxo de recuperação de senha.
     */
    suspend fun resetPassword(request: ResetPasswordRequest) {
        client.post("api/auth/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
    
    /**
     * Valida um código OTP (One-Time Password).
     */
    suspend fun validateOtp(request: ValidateOtpRequest) {
        client.post("api/auth/validate-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    /**
     * Permite que um novo usuário configure sua senha pela primeira vez.
     */
    suspend fun setupPassword(request: SetupPasswordRequest) {
        client.post("api/auth/setup-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}