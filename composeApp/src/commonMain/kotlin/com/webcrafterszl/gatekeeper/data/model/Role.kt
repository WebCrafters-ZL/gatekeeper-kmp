package com.webcrafterszl.gatekeeper.data.model

import kotlinx.serialization.Serializable

/**
 * Define os perfis de acesso (roles) suportados pelo sistema,
 * espelhando a estrutura do back-end.
 */
@Serializable
enum class Role {
    ADMIN,
    MANAGER,
    CARDHOLDER
}