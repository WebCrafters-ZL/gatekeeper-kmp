package com.webcrafterszl.gatekeeper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.webcrafterszl.gatekeeper.data.model.ConviteVisitante
import com.webcrafterszl.gatekeeper.data.remote.RepositorioRemoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Simulação do ID do usuário logado.
private const val LOGGED_IN_USER_ID = 1

class FormularioConviteViewModel(
    private val repositorio: RepositorioRemoto = RepositorioRemoto(),
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    fun enviarConvite(
        nomeCompleto: String,
        email: String,
        cpf: String,
        dataVisita: String
    ) {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            // Validação básica
            if (nomeCompleto.isBlank() || email.isBlank() || cpf.isBlank() || dataVisita.isBlank()) {
                errorMessage = "Por favor, preencha todos os campos."
                isLoading = false
                return@launch
            }

            try {
                val convite = ConviteVisitante(
                    nomeCompleto = nomeCompleto,
                    email = email,
                    cpf = cpf,
                    dataVisita = dataVisita,
                    convidadoPorId = LOGGED_IN_USER_ID
                )
                repositorio.enviarConvitePorEmail(convite)
                isSuccess = true
            } catch (e: Exception) {
                errorMessage = "Erro ao enviar convite: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun onErrorMessageShown() {
        errorMessage = null
    }

    fun onSuccessMessageShown() {
        isSuccess = false
    }
}