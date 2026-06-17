package com.webcrafterszl.gatekeeper.util

import com.webcrafterszl.gatekeeper.data.model.Role
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Objeto utilitário para operações relacionadas a JSON Web Tokens (JWT).
 */
object JwtUtils {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Decodifica um token JWT e extrai o perfil (role) do usuário.
     * Esta função é segura para KMP, pois não depende de bibliotecas de criptografia pesadas,
     * apenas da estrutura padrão do JWT (Header.Payload.Signature).
     *
     * @param token O token JWT em formato String, recebido do back-end.
     * @return O [Role] do usuário (ADMIN, MANAGER, CARDHOLDER) ou null se o token for inválido.
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun decodeRole(token: String): Role? {
        println("JwtUtils: Tentando decodificar token: $token")
        return try {
            // 1. O token é dividido em 3 partes. A que nos interessa é a segunda (Payload).
            val parts = token.split('.')
            if (parts.size < 2) {
                println("JwtUtils: Token malformado - menos de 2 partes.")
                return null
            }
            var payload = parts[1]

            // O payload em JWT é base64url (sem padding). Ajustamos para base64 padrão antes de decodificar.
            payload = payload.replace('-', '+').replace('_', '/')
            val padding = payload.length % 4
            if (padding != 0) {
                payload += "=".repeat(4 - padding)
            }

            // 2. Decodificamos o payload de Base64 para uma String JSON.
            val decodedPayload = Base64.decode(payload).decodeToString()
            println("JwtUtils: Payload decodificado: $decodedPayload")

            // 3. Usamos kotlinx.serialization para parsear a String JSON de forma segura.
            val jsonObject = json.parseToJsonElement(decodedPayload).jsonObject
            
            // 4. Tentamos diversas estratégias para extrair o perfil (role) do payload,
            // porque diferentes back-ends podem usar chaves distintas (role, roles, authorities).
            fun extractRoleString(): String? {
                // 4.1 - Chave simples "role"
                jsonObject["role"]?.let { return it.jsonPrimitive.content }

                // 4.2 - Chave "roles" como array ["MANAGER"] ou ["ROLE_MANAGER"]
                jsonObject["roles"]?.let { element ->
                    val arr = element
                    try {
                        val first = arr.jsonArray.firstOrNull()?.jsonPrimitive?.content
                        if (first != null) return first
                    } catch (_: Exception) { }
                }

                // 4.3 - Chave "authorities" ou "authority" (Spring often uses "ROLE_XXX")
                jsonObject["authorities"]?.let { element ->
                    try {
                        val first = element.jsonArray.firstOrNull()?.jsonPrimitive?.content
                        if (first != null) return first
                    } catch (_: Exception) { }
                }
                jsonObject["authority"]?.let { return it.jsonPrimitive.content }

                // 4.4 - Chave "user" contendo o objeto com role dentro
                jsonObject["user"]?.let { userElem ->
                    try {
                        val userObj = userElem.jsonObject
                        userObj["role"]?.let { return it.jsonPrimitive.content }
                        userObj["roles"]?.let { r ->
                            try {
                                val first = r.jsonArray.firstOrNull()?.jsonPrimitive?.content
                                if (first != null) return first
                            } catch (_: Exception) { }
                        }
                    } catch (_: Exception) { }
                }

                return null
            }

            val roleStringRaw = extractRoleString()
            println("JwtUtils: Role extraída do payload (raw): $roleStringRaw")

            // 5. Normalizamos o valor extraído e convertemos para nosso Enum de Role.
            roleStringRaw?.let { raw ->
                // Remover prefixos comuns como "ROLE_" e transformar em upper-case simples.
                val normalized = raw.replace("ROLE_", "", ignoreCase = true).trim().uppercase()
                try {
                    return Role.valueOf(normalized)
                } catch (e: IllegalArgumentException) {
                    println("JwtUtils: Valor de role '$normalized' não corresponde a um enum Role válido. Erro: ${e.message}")
                }
            }

            null
        } catch (e: Exception) {
            // Se qualquer etapa falhar (ex: token malformado, Base64 inválido, JSON inválido),
            // a operação é considerada insegura e retorna nulo.
            println("JwtUtils: Erro geral ao decodificar o token JWT: ${e.message}")
            null
        }
    }
}