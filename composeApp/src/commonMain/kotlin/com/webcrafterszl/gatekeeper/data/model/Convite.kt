package com.webcrafterszl.gatekeeper.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Convite(
    override val id: Int = 0,
    val token: String,
    val dataVisita: String, // Usaremos String por simplicidade, ex: "dd/MM/yyyy"
    val criadoPorId: Int, // ID do usuário que gerou o convite
    val visitanteId: Int? = null, // ID do visitante preenchido, nulo se pendente
    val nomeVisitante: String? = null // Para exibição rápida na lista
) : Identificavel