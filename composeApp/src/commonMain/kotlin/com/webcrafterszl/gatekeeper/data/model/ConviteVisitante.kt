package com.webcrafterszl.gatekeeper.data.model

import kotlinx.serialization.Serializable

/**
 * Representa os dados de um convite direto para um visitante específico para um único dia.
 */
@Serializable
data class ConviteVisitante(
    override val id: Int = 0,
    val nomeCompleto: String,
    val email: String,
    val cpf: String,
    val dataVisita: String, // Alterado para um único campo de data
    val convidadoPorId: Int // ID do usuário que está convidando
) : Identificavel