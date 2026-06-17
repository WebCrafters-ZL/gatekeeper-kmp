package com.webcrafterszl.gatekeeper.util

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

class AppOperationException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

fun Throwable.toUserMessage(operation: String? = null): String {
    if (this is CancellationException) throw this

    // Procura na cadeia de causas por uma exceção conhecida que nos dê contexto melhor
    var candidate: Throwable? = this
    var matched: Throwable? = null
    while (candidate != null) {
        when (candidate) {
            is ClientRequestException,
            is ServerResponseException,
            is ResponseException,
            is SerializationException -> {
                matched = candidate
                break
            }
        }
        candidate = candidate.cause
    }

    val subject = matched ?: this
    val prefix = operation?.trim()?.takeIf { it.isNotEmpty() }?.let { "$it: " }.orEmpty()
    val rawMessage = subject.message?.trim().orEmpty()
    val normalized = rawMessage.lowercase()

    val friendlyMessage = when (subject) {
        is ClientRequestException -> when (subject.response.status.value) {
            400 -> "A solicitação enviada é inválida."
            401 -> "E-mail ou senha incorretos."
            429 -> "Muitas tentativas. Tente novamente mais tarde."
            403 -> "Você não tem permissão para executar esta ação."
            404 -> "O recurso solicitado não foi encontrado."
            else -> "A solicitação ao servidor falhou (${subject.response.status.value})."
        }

        is ServerResponseException -> "O servidor encontrou um problema (${subject.response.status.value})."
        is ResponseException -> "Falha na comunicação com o servidor (${subject.response.status.value})."
        is SerializationException -> "A resposta do servidor veio em um formato inválido."
        else -> when {
            normalized.contains("clearttext") || normalized.contains("cleartext") ->
                "O Android bloqueou HTTP em texto puro. Habilite cleartext ou use HTTPS."

            normalized.contains("timed out") || normalized.contains("timeout") ->
                "A requisição expirou. Verifique se o backend está ativo e se a rede está respondendo."

            normalized.contains("failed to connect") ||
                normalized.contains("connection refused") ||
                normalized.contains("unknown host") ||
                normalized.contains("unresolved address") ->
                "Não foi possível conectar ao backend. Verifique se ele está rodando em http://localhost:8000/."

            rawMessage.isNotBlank() -> rawMessage
            else -> "Ocorreu um erro inesperado."
        }
    }

    return prefix + friendlyMessage
}
